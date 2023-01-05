package cn.wangliang181230.seata;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ServiceTest {

	@Autowired
	private TestService testService;


	@Test
	public void test() throws InterruptedException {
		testService.clean();

		testService.test(null, 0);
		Assertions.assertEquals(1, testService.count());

		testService.clean();

		try {
			Assertions.assertEquals(0, testService.count());
			testService.test("1", 0);
			Assertions.assertEquals(1, testService.count());
		} catch (Exception e) {
			Thread.sleep(2000L);
			Assertions.assertEquals(0, testService.count());
		}

		testService.clean();

		try {
			Assertions.assertEquals(0, testService.count());
			testService.test("2", 0);
			Assertions.assertEquals(1, testService.count());
		} catch (Exception e) {
			Thread.sleep(2000L);
			Assertions.assertEquals(0, testService.count());
		}
	}

}
