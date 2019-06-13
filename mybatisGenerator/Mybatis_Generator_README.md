#Mybatis Generator使用说明

##必要文件
mybatis-3.5.1.jar 和 mysql-connector-java-5.1.38.jar 可以在maven中央仓库下载
https://mvnrepository.com/  
mybatis-generator-core-1.3.7.jar在github下载
https://github.com/mybatis/generator/releases

## 配置文件
generator-config.xml是针对本次数据库的配置文件  
generator-config-详细参数.xml是Mybatis Generator配置文件的详细说明  
## 使用方式：
* 命令  
在项目根目录下执行命令：  
java -jar mybatis-generator-core-1.3.7.jar -configfile src\resources\generator-config.xml -overwrite
* 运行测试下的MBGTest的test方法
##注意：
1.src文件夹要先创建  
2.如果重新生成，最好删除原来的Mapper.xml文件，防止内容追加导致的报错
