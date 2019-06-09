# MybatisAlone模块
## 数据源  
* 项目中使用示例了三种数据库连接池的配置
    * c3p0-config.xml 是成c3p0的配置,使用示例见测试下c3p0包
    * hikari.properties 是成hikari的配置,使用示例在dbutils包下
    * com.liyueze.datasourcef包下DruidDataSourceFactory是成druid的配置
## 数据库操作
* 使用JDBC进行数据库操作，见test下
* 使用Aepache DbUtils进行数据库操作（见dbutils包）
## 什么是ORM
    O：对象——M：映射——R：关系型数据库
    
    ORM 的全拼是 Object Relational Mapping，也就是对象与关系的映射，对象是程
    序里面的对象，关系是它与数据库里面的数据的关系。也就是说，ORM 框架帮助我们解
    决的问题是程序对象和关系型数据库的相互映射的问题。
## Hibernate的问题（不一定是缺点，这样看具体的业务场景）
* 不能指定部分字段  
  比如使用 get()、save() 、update()这种方式，实际操作的是所有字段，
 没有办法指定部分字段，都是表的全字段，换句话说就是不够灵活
 * 无法自定义SQL,优化困难  
 Hibernate自动生成 SQL 的方式，如果我们要去做一些优化的话，是非常困难的，也
 就是说可能会出现性能比较差的问题
 * 不支持动态 SQL  
 分表中的表名变化，以及条件、参数（因为SQL自动生成）
 
     