package cn.wangliang181230.auto3;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;

@AutoConfigureAfter(name = "cn.wangliang181230.auto3.Test2AutoConfiguration")
public class Test3AutoConfiguration {

	public Test3AutoConfiguration() {
		System.out.println("33333333");
	}

	@Bean
	public Bean3 bean3() {
		return new Bean3();
	}

}
