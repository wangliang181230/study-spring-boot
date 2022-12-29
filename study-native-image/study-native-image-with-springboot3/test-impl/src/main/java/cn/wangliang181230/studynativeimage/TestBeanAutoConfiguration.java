package cn.wangliang181230.studynativeimage;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(value = "test.bean.enabled", matchIfMissing = true)
public class TestBeanAutoConfiguration {

	@Bean
	@ConditionalOnProperty(value = "test.bean.type", havingValue = "A")
	public ITestBean myTestBeanA() {
		System.out.println("myTestBeanA");
		return new TestBeanImplA();
	}

	@Bean
	@ConditionalOnProperty(value = "test.bean.type", havingValue = "B")
	public ITestBean myTestBeanB() {
		System.out.println("myTestBeanB");
		return new TestBeanImplB();
	}
}
