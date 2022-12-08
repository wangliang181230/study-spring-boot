package cn.wangliang181230.test.beandefinitionregistrypostprocessor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.util.Assert;

//@Component
public class MyBeanDefinitionRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor {

	private MyProperties myProperties;

	private ConfigurableEnvironment environment;


	public MyBeanDefinitionRegistryPostProcessor(MyProperties myProperties, ConfigurableEnvironment environment) {
		this.myProperties = myProperties;
		this.environment = environment;
		Assert.notNull(environment, "Environment must not be null");
		Assert.notNull(myProperties.getName(), "'myProperties.getName()' must be not null");
	}


	@Override
	public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {
		// do nothing
	}

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
		// do nothing
	}
}
