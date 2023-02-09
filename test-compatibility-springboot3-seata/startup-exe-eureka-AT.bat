title "test-compatibility-springboot3-seata"

start target/test-compatibility-springboot3-seata.exe ^
	-Dserver.port=8081 ^
	-Dlogging.level.io.seata=DEBUG ^
	-Dlogging.level.io.seata.spring.aot.AotUtils=INFO ^
	-Dlogging.level.io.seata.core.rpc=INFO ^
	-Dseata.data-source-proxy-mode=AT ^
	-Dseata.registry.type=eureka ^
	-Dseata.registry.eureka.service-url=http://192.168.1.103:8761/eureka ^
	-Dseata.service.vgroup-mapping.my_group=seata-server2

