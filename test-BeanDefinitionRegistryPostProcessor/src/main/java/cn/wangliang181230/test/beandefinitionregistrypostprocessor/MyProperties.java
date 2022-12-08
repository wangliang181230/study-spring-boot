package cn.wangliang181230.test.beandefinitionregistrypostprocessor;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
@ConfigurationProperties(prefix = "my")
public class MyProperties {

	private String name;


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
