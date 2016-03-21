1)create database platform
2)mvn clean
3)mvn eclipse:eclipse -Dwtpversion=1.0
4)mvn package -Dmaven.test.skip=true
5)mvn dependency:tree 可以查看依赖树