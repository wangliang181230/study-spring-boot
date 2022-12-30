import org.springframework.boot.SpringApplicationAotProcessor;

public class SpringApplicationAotProcessorTest {

	public static void main(String[] args) throws Exception {
		args = new String[]{
				"cn.wangliang181230.seata.TestSeataApplication",
				"E:\\Workspace_Java\\wangliang181230\\study-spring-boot\\test-compatibility-springboot3-seata\\target\\spring-aot\\main\\sources",
				"E:\\Workspace_Java\\wangliang181230\\study-spring-boot\\test-compatibility-springboot3-seata\\target\\spring-aot\\main\\resources",
				"E:\\Workspace_Java\\wangliang181230\\study-spring-boot\\test-compatibility-springboot3-seata\\target\\spring-aot\\main\\classes",
				"cn.wangliang181230",
				"test-compatibility-springboot3-seata"
		};
		SpringApplicationAotProcessor.main(args);
	}

}
