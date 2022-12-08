package cn.wangliang181230.test.beandefinitionregistrypostprocessor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class MyBeanDefinitionRegistryPostProcessorConfiguration {

//	@Bean
//	public MyBeanDefinitionRegistryPostProcessor myBeanDefinitionRegistryPostProcessor(ConfigurableEnvironment environment) {
//		return new MyBeanDefinitionRegistryPostProcessor(environment);
//	}

	@Bean
	public MyBeanDefinitionRegistryPostProcessor myBeanDefinitionRegistryPostProcessor(MyProperties myProperties) {
		return new MyBeanDefinitionRegistryPostProcessor(myProperties);
	}
}
