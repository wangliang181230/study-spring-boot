/*
 *  Copyright 1999-2019 Seata.io Group.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package io.seata.spring.boot.autoconfigure.provider;

import io.seata.common.exception.ShouldNeverHappenException;
import io.seata.common.holder.ObjectHolder;
import io.seata.config.Configuration;
import io.seata.config.ExtConfigurationProvider;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.lang.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import static io.seata.common.Constants.OBJECT_KEY_SPRING_CONFIGURABLE_ENVIRONMENT;
import static io.seata.common.util.StringFormatUtils.DOT;
import static io.seata.spring.boot.autoconfigure.StarterConstants.PROPERTY_BEAN_MAP;
import static io.seata.spring.boot.autoconfigure.StarterConstants.SEATA_PREFIX;
import static io.seata.spring.boot.autoconfigure.StarterConstants.SERVICE_PREFIX;
import static io.seata.spring.boot.autoconfigure.StarterConstants.SPECIAL_KEY_GROUPLIST;
import static io.seata.spring.boot.autoconfigure.StarterConstants.SPECIAL_KEY_SERVICE;
import static io.seata.spring.boot.autoconfigure.StarterConstants.SPECIAL_KEY_VGROUP_MAPPING;

/**
 * @author xingfudeshi@gmail.com
 * @author funkye
 */
public class SpringBootConfigurationProvider implements ExtConfigurationProvider {

	private static final Logger LOGGER = LoggerFactory.getLogger(SpringBootConfigurationProvider.class);

	private static final String INTERCEPT_METHOD_PREFIX = "get";

	private static final Map<String, Object> PROPERTY_BEAN_INSTANCE_MAP = new HashMap<>(64);

	@Override
	public Configuration provide(Configuration originalConfiguration) {
		return (Configuration)Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{Configuration.class}
				, (proxy, method, args) -> {
					if (method.getName().startsWith(INTERCEPT_METHOD_PREFIX) && args.length > 0) {
						Object result;
						String rawDataId = (String)args[0];
						result = originalConfiguration.getConfigFromSys(rawDataId);
						if (null == result) {
							String newDataId = convertDataId(rawDataId);
							if (args.length == 1) {
								result = get(newDataId);
							} else {
								result = get(newDataId, args[1]);
							}
						}
						if (result != null) {
							// If the return type is String,need to convert the object to string
							if (method.getReturnType().equals(String.class)) {
								return String.valueOf(result);
							} else {
								return result;
							}
						}
					}

					return method.invoke(originalConfiguration, args);
				}
		);
	}

	private Object get(String dataId, Object defaultValue) throws IllegalAccessException, InvocationTargetException {
		Object result = get(dataId);
		if (result == null) {
			return defaultValue;
		}
		return result;
	}

	private Object get(String dataId) throws IllegalAccessException, InvocationTargetException {
		String propertyPrefix = getPropertyPrefix(dataId);
		String propertySuffix = getPropertySuffix(dataId);
		Class<?> propertyClass = PROPERTY_BEAN_MAP.get(propertyPrefix);
		Object valueObject = null;
		if (propertyClass != null) {
			valueObject = getFieldValue(
					Objects.requireNonNull(PROPERTY_BEAN_INSTANCE_MAP.computeIfAbsent(propertyPrefix, k -> {
						try {
							return propertyClass.newInstance();
						} catch (InstantiationException | IllegalAccessException e) {
							LOGGER.warn("PropertyClass for prefix: [" + propertyPrefix + "] should not be null. error :" + e.getMessage(), e);
							return null;
						}
					})), propertySuffix, dataId);
		} else {
			throw new ShouldNeverHappenException("PropertyClass for prefix: [" + propertyPrefix + "] should not be null.");
		}

		return valueObject;
	}

	/**
	 * get field value
	 *
	 * @param object
	 * @param fieldName
	 * @param dataId
	 * @return java.lang.Object
	 * @author xingfudeshi@gmail.com
	 */
	private Object getFieldValue(Object object, String fieldName, String dataId) throws IllegalAccessException, InvocationTargetException {
		ValueAndType defaultValueType = this.getDefaultValue(object, fieldName);
		if (defaultValueType == null) {
			return getConfig(dataId, null, String.class);
		} else {
			return getConfig(dataId, defaultValueType.getValue(), defaultValueType.getType());
		}
	}

	@Nullable
	private ValueAndType getDefaultValue(Object object, String fieldName) throws IllegalAccessException, InvocationTargetException {
		Field[] fields = object.getClass().getDeclaredFields();
		if (fields.length > 0) {
			Optional<Field> fieldOptional = Stream.of(fields)
					.filter(f -> f.getName().equalsIgnoreCase(fieldName))
					.findAny();
			if (fieldOptional.isPresent()) {
				Field field = fieldOptional.get();
				if (isMapOrColl(field.getType())) {
					return null;
				}
				field.setAccessible(true);
				return new ValueAndType(field.get(object), field.getType());
			}
		}

		String getMethodName = "get" + String.valueOf(fieldName.charAt(0)).toUpperCase() + fieldName.substring(1);
		try {
			Method getMethod = object.getClass().getMethod(getMethodName);
			if (isMapOrColl(getMethod.getReturnType())) {
				return null;
			}
			return new ValueAndType(getMethod.invoke(object), getMethod.getReturnType());
		} catch (NoSuchMethodException e) {
			//LOGGER.warn("NoSuchMethodException: {}.{}()", object.getClass().getSimpleName(), getMethodName);
		}

		getMethodName = "is" + String.valueOf(fieldName.charAt(0)).toUpperCase() + fieldName.substring(1);
		try {
			Method getMethod = object.getClass().getMethod(getMethodName);
			return new ValueAndType(getMethod.invoke(object), getMethod.getReturnType());
		} catch (NoSuchMethodException e) {
			LOGGER.warn("NoSuchMethodException: {}.{}()", object.getClass().getSimpleName(), getMethodName);
		}

		return null;
	}


	private boolean isMapOrColl(Class<?> type) {
		return Map.class.isAssignableFrom(type) || Collection.class.isAssignableFrom(type);
	}

	/**
	 * convert data id
	 *
	 * @param rawDataId
	 * @return dataId
	 */
	private String convertDataId(String rawDataId) {
		if (rawDataId.endsWith(SPECIAL_KEY_GROUPLIST)) {
			String suffix = StringUtils.removeStart(StringUtils.removeEnd(rawDataId, DOT + SPECIAL_KEY_GROUPLIST),
					SPECIAL_KEY_SERVICE + DOT);
			// change the format of default.grouplist to grouplist.default
			return SERVICE_PREFIX + DOT + SPECIAL_KEY_GROUPLIST + DOT + suffix;
		}
		return SEATA_PREFIX + DOT + rawDataId;
	}

	/**
	 * Get property prefix
	 *
	 * @param dataId
	 * @return propertyPrefix
	 */
	private String getPropertyPrefix(String dataId) {
		if (dataId.contains(SPECIAL_KEY_VGROUP_MAPPING)) {
			return SERVICE_PREFIX;
		}
		if (dataId.contains(SPECIAL_KEY_GROUPLIST)) {
			return SERVICE_PREFIX;
		}
		return StringUtils.substringBeforeLast(dataId, String.valueOf(DOT));
	}

	/**
	 * Get property suffix
	 *
	 * @param dataId
	 * @return propertySuffix
	 */
	private String getPropertySuffix(String dataId) {
		if (dataId.contains(SPECIAL_KEY_VGROUP_MAPPING)) {
			return SPECIAL_KEY_VGROUP_MAPPING;
		}
		if (dataId.contains(SPECIAL_KEY_GROUPLIST)) {
			return SPECIAL_KEY_GROUPLIST;
		}
		return StringUtils.substringAfterLast(dataId, String.valueOf(DOT));
	}

	/**
	 * get spring config
	 *
	 * @param dataId       data id
	 * @param defaultValue default value
	 * @param type         type
	 * @return object
	 */
	private Object getConfig(String dataId, Object defaultValue, Class<?> type) {
		ConfigurableEnvironment environment =
				(ConfigurableEnvironment)ObjectHolder.INSTANCE.getObject(OBJECT_KEY_SPRING_CONFIGURABLE_ENVIRONMENT);
		Object value = environment.getProperty(dataId, type);
		if (value == null) {
			value = environment.getProperty(io.seata.common.util.StringUtils.hump2Line(dataId), type);
		}
		return value != null ? value : defaultValue;
	}

	private static class ValueAndType {
		private Object value;
		private Class<?> type;


		public ValueAndType(Object value, Class<?> type) {
			this.value = value;
			this.type = type;
		}


		public Object getValue() {
			return value;
		}

		public void setValue(Object value) {
			this.value = value;
		}

		public Class<?> getType() {
			return type;
		}

		public void setType(Class<?> type) {
			this.type = type;
		}
	}

}
