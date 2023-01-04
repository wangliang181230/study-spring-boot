package cn.wangliang181230.studynativeimage;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class MyConfiguration {

	@Bean
	public AFilter aFilter() {
		return new AFilter();
	}

}
