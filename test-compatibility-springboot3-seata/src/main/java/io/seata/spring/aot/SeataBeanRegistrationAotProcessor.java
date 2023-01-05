package io.seata.spring.aot;

import io.seata.common.util.ReflectionUtil;
import io.seata.rm.tcc.api.LocalTCC;
import io.seata.spring.annotation.GlobalTransactionScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aot.generate.GenerationContext;
import org.springframework.aot.hint.ReflectionHints;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.beans.factory.aot.BeanRegistrationAotContribution;
import org.springframework.beans.factory.aot.BeanRegistrationAotProcessor;
import org.springframework.beans.factory.aot.BeanRegistrationCode;
import org.springframework.beans.factory.support.RegisteredBean;

import java.util.Set;

import static io.seata.spring.aot.AotUtils.ALL_MEMBER_CATEGORIES;
import static org.springframework.aot.hint.MemberCategory.INVOKE_PUBLIC_METHODS;

class SeataBeanRegistrationAotProcessor implements BeanRegistrationAotProcessor {

	private static final Logger LOGGER = LoggerFactory.getLogger(SeataBeanRegistrationAotProcessor.class);

	@Override
	public BeanRegistrationAotContribution processAheadOfTime(RegisteredBean registeredBean) {
		Class<?> beanClass = registeredBean.getBeanClass();
		if (GlobalTransactionScanner.isTccAutoProxy(beanClass)) {
			return new SeataTccBeanRegistrationAotContribution(beanClass);
		}
		return null;
	}

	private static class SeataTccBeanRegistrationAotContribution implements BeanRegistrationAotContribution {

		private final Class<?> beanClass;

		public SeataTccBeanRegistrationAotContribution(Class<?> beanClass) {
			this.beanClass = beanClass;
		}

		@Override
		public void applyTo(GenerationContext generationContext, BeanRegistrationCode beanRegistrationCode) {
			RuntimeHints hints = generationContext.getRuntimeHints();
			ReflectionHints reflectionHints = hints.reflection();

			// register the bean class to reflectively access the method
			reflectionHints.registerType(beanClass);

			Set<Class<?>> interfaceClasses = ReflectionUtil.getInterfaces(beanClass);
			for (Class<?> interClass : interfaceClasses) {
				if (interClass.isAnnotationPresent(LocalTCC.class)) {
					// register the @LocalTCC interface to reflectively access the method
					LOGGER.info("Registering @LocalTCC interface [{}] to reflectively access the method", interClass.getName());
					reflectionHints.registerType(interClass, INVOKE_PUBLIC_METHODS);
					AotUtils.registerAllOfClass(interClass, false, reflectionHints, ALL_MEMBER_CATEGORIES);
				}
			}
		}
	}
}
