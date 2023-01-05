package cn.wangliang181230.seata;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

	private static final Logger LOGGER = LoggerFactory.getLogger(TestController.class);

	@Autowired
	private TestService testService;


	@GetMapping("/")
	public Object test(@RequestParam(value = "test", required = false) String test) throws InterruptedException {
		try {
			return testService.test(test, 10000L);
		} catch (MyRuntimeException e) {
			return e.getMessage();
		} catch (Exception e) {
			LOGGER.error("测试异常", e);
			return e.toString();
		}
	}

}
