title "test-compatibility-springboot3-seata_XA"

start target/test-compatibility-springboot3-seata.exe ^
    -Dserver.port=8081 ^
    -Dlogging.level.io.seata=DEBUG ^
    -Dseata.data-source-proxy-mode=XA
