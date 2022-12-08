package cn.wangliang181230.test.beandefinitionregistrypostprocessor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.util.Assert;

//@Component
public class MyBeanDefinitionRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor {

	private ConfigurableEnvironment environment;

	private MyProperties myProperties;


	public MyBeanDefinitionRegistryPostProcessor(ConfigurableEnvironment environment) {
		this.environment = environment;
	}

	public MyBeanDefinitionRegistryPostProcessor(MyProperties myProperties) {
		Assert.notNull(myProperties.getName(), "'myProperties.getName()' must be not null");
		this.myProperties = myProperties;
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
