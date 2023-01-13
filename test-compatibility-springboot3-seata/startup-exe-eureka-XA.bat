title "test-compatibility-springboot3-seata_XA"

start target/test-compatibility-springboot3-seata.exe ^
	-Dserver.port=8081 ^
	-Dlogging.level.io.seata=DEBUG ^
	-Dseata.registry.type=eureka ^
	-Dseata.registry.eureka.service-url=http://192.168.1.103:8761/eureka ^
	-Dseata.service.vgroup-mapping.my_group=seata-server2 ^
	-Dseata.data-source-proxy-mode=XA

