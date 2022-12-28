package cn.wangliang181230.studynativeimage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
@RestController
public class StudyNativeImageBySpringBoot3Application {

	private static String[] ARGS;

	public static void main(String[] args) {
		ARGS = args;
		System.out.println("\r\n\r\n\r\nargs: " + Arrays.toString(args) + "\r\n");
		SpringApplication.run(StudyNativeImageBySpringBoot3Application.class, args);
	}

	@GetMapping("/")
	public Object test() {
		Map<String, Object> map = new HashMap<>();
		map.put("args", ARGS);
		map.put("env", System.getenv());
		map.put("properties", System.getProperties());
		return map;
	}
}
