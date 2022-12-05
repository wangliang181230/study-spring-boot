package cn.wangliang181230.auto2;

import com.alibaba.fastjson.JSON;
import org.springframework.context.annotation.Bean;

public class Test2AutoConfiguration {

	private static final String TAG = JSON.parse("{\"name\":\"wangliang\"}").toString();


	public Test2AutoConfiguration() {
		System.out.println("22222222222");
	}

	@Bean
	public Bean2 bean2() {
		System.out.println("bean2");
		System.out.println(JSON.parse("{\"name\":\"wangliang\"}"));
		return new Bean2();
	}

}
