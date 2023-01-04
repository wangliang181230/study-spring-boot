package cn.wangliang181230.studynativeimage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@SpringBootApplication
@RestController
@EnableAspectJAutoProxy
public class StudyNativeImageBySpringBoot3Application {

	public static void main(String[] args) {
		TestController.ARGS = args;
		System.out.println("\r\n\r\n\r\nargs: " + Arrays.toString(args) + "\r\n");
		SpringApplication.run(StudyNativeImageBySpringBoot3Application.class, args);
	}
}
