title "test-compatibility-springboot3-seata_XA"

start target/test-compatibility-springboot3-seata.exe ^
	-Dserver.port=8081 ^
	-Dlogging.level.io.seata=DEBUG ^
	-Dlogging.level.io.seata.spring.aot.AotUtils=INFO ^
	-Dlogging.level.io.seata.core.rpc=INFO ^
	-Dseata.data-source-proxy-mode=XA ^
	-Dseata.registry.type=nacos ^
	-Dseata.registry.nacos.server-addr=192.168.1.103:8848

