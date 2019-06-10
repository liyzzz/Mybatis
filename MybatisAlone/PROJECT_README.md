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
## Hibernate的问题（不一定是缺点，这要看具体的业务场景）
* 不能指定部分字段  
  比如使用 get()、save() 、update()这种方式，实际操作的是所有字段，
 没有办法指定部分字段，都是表的全字段，换句话说就是不够灵活
 * 无法自定义SQL,优化困难  
 Hibernate自动生成 SQL 的方式，如果我们要去做一些优化的话，是非常困难的，也
 就是说可能会出现性能比较差的问题
 * 不支持动态 SQL  
 分表中的表名变化，以及条件、参数（因为SQL自动生成）
 ## Mybatis
 ### 四个重要的对象
 * SqlSessionFactoryBuiler  
 ```
 加载解析配置文件,构建 SqlSessionFactory,是单例。它的生命周期只存在于构建方法的局部 
 ```
 * SqlSessionFactory
 ```  
    SqlSessionFactory 是用来创建SqlSession的，每次应用程序访问数据库，都需要
    创建一个会话(既：JDBC中的连接)。因为我们一直有创建会话的需要，所以 
    SqlSessionFactory 应该存在于应用的整个生命周期中（作用域是应用作用域）。
    创建SqlSession只需要一个实例来做这件事就行了，否则会产生很多的混乱，和浪费资源。
    所以SqlSession要采用单例模式。 
 ```
 * SqlSession 
 ```
    SqlSession是一个会话(连接)，因为它不是线程安全的，不能在线程间共享。所以我们在
    请求开始的时候创建一个SqlSession对象，在请求结束或者说方法执行完毕的时候要及
    时关闭它（一次请求或者操作中）。 
 ```
 * Mapper
 ```
Mapper（实际上是一个代理对象）是从SqlSession中获取的。
它的作用是发送 SQL来操作数据库的数据。它应该在一个 SqlSession事务方法之
内。

 ```
 ### 一级标签(示例见mybatis-config.xml)
 * configuration
 ```
configuration 是整个配置文件的根标签，对应着 MyBatis 里面配置类 Configuration(属性跟其子标签对应)

 ```
 * properties
```
为了避免直接把参数写死在 xml 配置文件中，我们可以把这些参数单独放在properties 文件中，
用 properties 标签引入进来，然后在xml 配置文件中用${}引用。
```
* settings

* typeAliases
```
TypeAlias是给类型起别名
```
* typeHandlers
```
typeHandlers用来将java对象和数据库类型相互转换
Mybatis默认的typeHandles都注册在TypeHandlerRegistry中
自定义的typeHandle见
```


 

 
     