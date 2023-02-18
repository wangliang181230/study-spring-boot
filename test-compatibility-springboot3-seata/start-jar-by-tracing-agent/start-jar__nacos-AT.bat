title "test-compatibility-springboot3-seata"

java -Dspring.aot.enabled=true ^
	-agentlib:native-image-agent=config-output-dir=../src/main/resources/META-INF/native-image/cn.wangliang181230/test-compatibility-springboot3/ ^
	-Dserver.port=8081 ^
 ^
	-Dlogging.level.io.seata=DEBUG ^
	-Dlogging.level.io.seata.spring.aot.AotUtils=INFO ^
	-Dlogging.level.io.seata.core.rpc=INFO ^
 ^
	-Dseata.registry.type=nacos ^
	-Dseata.service.grouplist.default=x ^
 ^
	-Dseata.data-source-proxy-mode=AT ^
 ^
 -jar ../target/test-compatibility-springboot3-seata.jar


