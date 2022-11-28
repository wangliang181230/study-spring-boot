package cn.wangliang181230.auto2;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Bean;

@AutoConfigureBefore(name = "cn.wangliang181230.auto3.Test3AutoConfiguration")
public class Test2AutoConfiguration {

	public Test2AutoConfiguration() {
		System.out.println("22222222222");
	}

	@Bean
	public Bean2 bean2() {
		return new Bean2();
	}

}
