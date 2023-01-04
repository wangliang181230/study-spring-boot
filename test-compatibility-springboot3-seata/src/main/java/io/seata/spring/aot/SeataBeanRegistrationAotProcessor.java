package io.seata.spring.aot;

import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.aot.generate.GenerationContext;
import org.springframework.aot.hint.ProxyHints;
import org.springframework.beans.factory.aot.BeanRegistrationAotContribution;
import org.springframework.beans.factory.aot.BeanRegistrationAotProcessor;
import org.springframework.beans.factory.aot.BeanRegistrationCode;
import org.springframework.beans.factory.support.RegisteredBean;

import javax.sql.DataSource;

class SeataBeanRegistrationAotProcessor implements BeanRegistrationAotProcessor {

	@Override
	public BeanRegistrationAotContribution processAheadOfTime(RegisteredBean registeredBean) {
		Class<?> beanClass = registeredBean.getBeanClass();
		if (DataSource.class.isAssignableFrom(beanClass)) {
			return new SeataBeanRegistrationAotContribution(beanClass);
		}
		return null;
	}

	private static class SeataBeanRegistrationAotContribution implements BeanRegistrationAotContribution {

		private final Class<?> beanClass;

		public SeataBeanRegistrationAotContribution(Class<?> beanClass) {
			this.beanClass = beanClass;
		}

		@Override
		public void applyTo(GenerationContext generationContext, BeanRegistrationCode beanRegistrationCode) {
			System.out.println("registerJdkProxy: " + beanClass.getName());
			ProxyHints proxyHints = generationContext.getRuntimeHints().proxies();
			proxyHints.registerJdkProxy(AopProxyUtils.completeJdkProxyInterfaces(beanClass));
		}
	}
}
