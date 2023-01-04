title "study-native-image-with-springboot3"

start study-native-image-with-springboot3-application/target/study-native-image-with-springboot3-application.exe ^
      cn.wangliang181230.studynativeimage.TestBeanImplA ^
      cn.wangliang181230.studynativeimage.TestBeanImplB ^
      cn.wangliang181230.studynativeimage.TestBeanImplXxxx ^
      -Dserver.port=8091 ^
      -Dtest.bean.enabled=false ^
      -Dtest.bean.type=A ^
      -Dtest.value=1234 ^
      -DfilterOrder=11 ^
      -Dfilter-order=22
