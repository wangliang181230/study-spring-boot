package cn.wangliang181230.studynativeimage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController2 {

	private static final Logger LOGGER = LoggerFactory.getLogger(TestController2.class);


	@MyAnnotation
	@GetMapping("/test1")
	public String test1() {
		LOGGER.info("test1");
		LOGGER.warn("test1");
		LOGGER.error("test1");
		return "test1";
	}

	@MyAnnotation
	@GetMapping("/test2")
	public String test2(@RequestParam(required = false) String s) {
		LOGGER.info("test2: {}", s);
		LOGGER.warn("test2: {}", s);
		LOGGER.error("test2: {}", s);
		return "test2: " + s;
	}
}
