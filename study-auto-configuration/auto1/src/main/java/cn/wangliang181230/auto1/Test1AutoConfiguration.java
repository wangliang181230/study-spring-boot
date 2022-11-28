package cn.wangliang181230.auto1;

import cn.wangliang181230.auto3.Bean3;
import cn.wangliang181230.auto3.Test3AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;

@ConditionalOnBean(Bean3.class)
@AutoConfigureAfter(Test3AutoConfiguration.class)
public class Test1AutoConfiguration {

	public Test1AutoConfiguration() {
		System.out.println("1111111111111111");
	}

	@Bean
	public Bean1 bean1() {
		return new Bean1();
	}

}
