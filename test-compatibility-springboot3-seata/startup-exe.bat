title "test-compatibility-springboot3-seata"

start target/test-compatibility-springboot3-seata.exe ^
    --server.port=8081 ^
    -Dseata.data-source-proxy-mode=XA
