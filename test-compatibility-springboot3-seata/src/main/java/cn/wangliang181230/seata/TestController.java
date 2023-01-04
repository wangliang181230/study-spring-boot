package cn.wangliang181230.seata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

	@Autowired
	private TestService testService;


	@GetMapping("/")
	public Object test(@RequestParam(value = "testError", required = false) Boolean testError,
					   @RequestParam(value = "testError2", required = false) Boolean testError2
	) throws InterruptedException {
		return testService.test(testError, testError2, 10000L);
	}

}
