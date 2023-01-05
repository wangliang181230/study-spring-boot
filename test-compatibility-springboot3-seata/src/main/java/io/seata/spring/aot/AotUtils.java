package io.seata.spring.aot;

import io.seata.common.util.ReflectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.ReflectionHints;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.springframework.aot.hint.MemberCategory.DECLARED_CLASSES;
import static org.springframework.aot.hint.MemberCategory.DECLARED_FIELDS;
import static org.springframework.aot.hint.MemberCategory.INTROSPECT_DECLARED_CONSTRUCTORS;
import static org.springframework.aot.hint.MemberCategory.INTROSPECT_DECLARED_METHODS;
import static org.springframework.aot.hint.MemberCategory.INTROSPECT_PUBLIC_CONSTRUCTORS;
import static org.springframework.aot.hint.MemberCategory.INTROSPECT_PUBLIC_METHODS;
import static org.springframework.aot.hint.MemberCategory.INVOKE_DECLARED_CONSTRUCTORS;
import static org.springframework.aot.hint.MemberCategory.INVOKE_DECLARED_METHODS;
import static org.springframework.aot.hint.MemberCategory.INVOKE_PUBLIC_CONSTRUCTORS;
import static org.springframework.aot.hint.MemberCategory.INVOKE_PUBLIC_METHODS;
import static org.springframework.aot.hint.MemberCategory.PUBLIC_CLASSES;
import static org.springframework.aot.hint.MemberCategory.PUBLIC_FIELDS;

public abstract class AotUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(AotUtils.class);

	public static final String SPRING_AOT_PROCESSING = "spring.aot.processing";


	public static final MemberCategory[] ALL_MEMBER_CATEGORIES = new MemberCategory[]{
			INTROSPECT_PUBLIC_CONSTRUCTORS, INVOKE_PUBLIC_CONSTRUCTORS,
			INTROSPECT_DECLARED_CONSTRUCTORS, INVOKE_DECLARED_CONSTRUCTORS,
			PUBLIC_FIELDS, DECLARED_FIELDS,
			INTROSPECT_PUBLIC_METHODS, INVOKE_PUBLIC_METHODS,
			INTROSPECT_DECLARED_METHODS, INVOKE_DECLARED_METHODS,
			PUBLIC_CLASSES, DECLARED_CLASSES
	};


	/**
	 * 判断是否在spring-aot的处理过程中
	 *
	 * @return true:是 | false:否
	 */
	public static boolean isSpringAotProcessing() {
		return "true".equalsIgnoreCase(System.getProperty(SPRING_AOT_PROCESSING));
	}

	/**
	 * 递归将类及其父类、接口、属性都注册到反射提示中
	 *
	 * @param clazz
	 * @param registerSelf
	 * @param reflectionHints
	 * @param memberCategories
	 */
	public static void registerAllOfClass(Class<?> clazz, boolean registerSelf, ReflectionHints reflectionHints, MemberCategory... memberCategories) {
		registerAllOfClassInternal(clazz, registerSelf, new HashSet<>(), reflectionHints, memberCategories);
	}

	/**
	 * 递归将类及其父类、接口、属性都注册到反射提示中
	 *
	 * @param clazz
	 * @param registerSelf
	 * @param cache            缓存，防止重复注册
	 * @param reflectionHints
	 * @param memberCategories
	 */
	private static void registerAllOfClassInternal(Class clazz, boolean registerSelf, Set<Class<?>> cache, ReflectionHints reflectionHints, MemberCategory... memberCategories) {
		if (clazz == null) {
			return;
		}

		if (clazz.isPrimitive() || clazz.isEnum() || clazz.isAnnotation()
				|| clazz.equals(Object.class)
				|| clazz.equals(String.class)
				|| clazz.equals(Character.class)
				|| clazz.equals(Boolean.class)
				|| clazz.equals(Short.class)
				|| clazz.equals(Integer.class)
				|| clazz.equals(Long.class)
				|| clazz.equals(BigInteger.class)
				|| clazz.equals(BigDecimal.class)
				|| clazz.equals(Serializable.class)
				|| clazz.equals(Class.class)
				|| clazz.equals(Type.class)
				|| Map.class.isAssignableFrom(clazz)
				|| Collection.class.isAssignableFrom(clazz)
		) {
			if (clazz.getClassLoader() != null) {
				LOGGER.warn("The skip class has classLoader: {}", clazz.getName());
			}
			return;
		}

		if (clazz.isArray()) {
			registerAllOfClassInternal(clazz.getComponentType(), true, cache, reflectionHints, memberCategories);
			return;
		}

		if (cache.contains(clazz)) {
			return;
		}
		cache.add(clazz);

		// 注册类自己（加if是为了测试用）
		if (registerSelf) {
			reflectionHints.registerType(clazz, memberCategories);
			LOGGER.info("register class '{}' to reflection hints with member categories: {}", clazz.getName(), memberCategories);
		}

		// 注册类的接口
		Set<Class<?>> interfaceClasses = ReflectionUtil.getInterfaces(clazz);
		for (Class<?> interfaceClass : interfaceClasses) {
			if (!interfaceClass.equals(clazz)) {
				registerAllOfClassInternal(interfaceClass, true, cache, reflectionHints, memberCategories);
			}
		}

		// 注册类的父类
		registerAllOfClassInternal(clazz.getSuperclass(), true, cache, reflectionHints, memberCategories);

		// 注册类的属性类型
		Field[] fields = ReflectionUtil.getAllFields(clazz);
		for (Field field : fields) {
			registerAllOfClassInternal(field.getType(), true, cache, reflectionHints, memberCategories);
		}

		// 注册方法的参数
		Method[] methods = clazz.getMethods();
		for (Method method : methods) {
			Class<?>[] parameterTypes = method.getParameterTypes();
			for (Class<?> parameterType : parameterTypes) {
				registerAllOfClassInternal(parameterType, true, cache, reflectionHints, memberCategories);
			}
		}
	}

	private static boolean isJavaClass(Class<?> clazz) {
		if (clazz == null) {
			return true;
		}

		return clazz.getClassLoader() == null;
	}
}
