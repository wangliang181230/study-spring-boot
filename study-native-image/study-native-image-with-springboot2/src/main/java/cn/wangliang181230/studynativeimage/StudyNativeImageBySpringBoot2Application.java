package cn.wangliang181230.studynativeimage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@SpringBootApplication
@RestController
public class StudyNativeImageBySpringBoot2Application {

	public static void main(String[] args) {
		System.out.println("\r\n\r\n\r\nargs: " + Arrays.toString(args) + "\r\n");
		SpringApplication.run(StudyNativeImageBySpringBoot2Application.class, args);
	}

	@GetMapping("/")
	public Object hello() {
		return "hello sb2";
	}
}
