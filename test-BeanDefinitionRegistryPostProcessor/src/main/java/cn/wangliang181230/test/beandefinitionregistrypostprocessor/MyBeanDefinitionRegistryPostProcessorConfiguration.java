package cn.wangliang181230.test.beandefinitionregistrypostprocessor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;

@Configuration(proxyBeanMethods = false)
public class MyBeanDefinitionRegistryPostProcessorConfiguration {

	@Bean
	public MyBeanDefinitionRegistryPostProcessor myBeanDefinitionRegistryPostProcessor(MyProperties myProperties, ConfigurableEnvironment environment) {
		return new MyBeanDefinitionRegistryPostProcessor(myProperties, environment);
	}
}
