package cn.wangliang181230.auto1;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.context.annotation.Bean;

@AutoConfigureOrder(100)
@AutoConfigureBefore(name = "cn.wangliang181230.auto3.Test2AutoConfiguration")
public class Test1AutoConfiguration {

	public Test1AutoConfiguration() {
		System.out.println("1111111111111111");
	}

	@Bean
	public Bean1 bean1() {
		System.out.println("bean1");
		return new Bean1();
	}

}
