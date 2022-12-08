package cn.wangliang181230.test.beandefinitionregistrypostprocessor.autoconfigure;

import cn.wangliang181230.test.beandefinitionregistrypostprocessor.MyBeanDefinitionRegistryPostProcessor;
import cn.wangliang181230.test.beandefinitionregistrypostprocessor.MyProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.ConfigurableEnvironment;

public class MyBeanDefinitionRegistryPostProcessorAutoConfiguration {

	@Bean
	public MyBeanDefinitionRegistryPostProcessor myBeanDefinitionRegistryPostProcessor(MyProperties myProperties, ConfigurableEnvironment environment) {
		return new MyBeanDefinitionRegistryPostProcessor(myProperties, environment);
	}
}
