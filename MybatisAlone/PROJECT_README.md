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
    * Executor:用来执行mapper中的SQL
        * SimpleExecutor:
            ```
            每一次执行都会创建一个新的 Statement 对象
            对应JDBC Statement
            配置:<setting name="defaultExecutorType" value="SIMPLE"/>
            ```
        * ReuseExecutor:(如果未配置，默认使用)
           ```
            不会每一次调用都去创建一个 Statement 对象，
            而是会重复利用以前创建好的（如果SQL相同的话）
            对应JDBC prepared statements
            配置:<setting name="defaultExecutorType" value="REUSE"/>
            ```
        * BatchExecutor:(见[批量操作](#批量操作) )
    * lazyLoadingEnabled(见[关联查询](#关联查询)中一对一查询中的嵌套查询)
    * aggressiveLazyLoading(见[关联查询](#关联查询)中一对一查询中的嵌套查询)
    * proxyFactory(见[关联查询](#关联查询)中一对一查询中的嵌套查询)        

* typeAliases
```
TypeAlias是给类型起别名
```
* objectFactory(重点)
```
当我们把数据库返回的结果集转换为实体类的时候，需要创建对象的实例.
ObjectFactory，专门用来创建对象的实例.
ObjectFactory先工作创建对象，然后再是TypeHandler工作.
自定义的objectFactory示例：
见objectFactory包下MyObjectFactory(有具体的配置说明)
测试:任意一个返回blog对象的查询
```
* typeHandlers(重点)
```
typeHandlers用来将java对象和数据库类型相互转换
Mybatis默认的typeHandles都注册在TypeHandlerRegistry中
自定义的typeHandler示例：
见typeHandler包下MyTypeHandler(有具体的配置说明)
测试见testSelect方法和testInsert
```
* environments
```
environments标签用来管理数据库的环境，比如我们可以有开发环境、测试环境、生产环境的数据库。
可以在不同的环境中使用不同的数据库地址或者类型
一个environment标签就是一个数据源，代表一个数据库。这里面有两个关键的标
签，一个是事务管理器，一个是数据源
配置见mybatis-config.xml
测试用例见testEnvironment方法
```
* mappers
```
<mappers>标签配置的是我们的映射器，也就是Mapper.xml的路径
具体的使用方式见他人博客总结：https://www.cnblogs.com/deolin/p/8195565.htm
```
### 映射配置文件标签(见BlogMapper.xml)
* cache 
* cache-ref 
* resultMap 
```
 用来描述如何从数据库结果集中来加载对象。
```
* sql
```
 可被其他语句引用的可重用语句块。
```
增删改查标签：
* insert 
* update 
* delete 
* select 
### 动态SQL
解决SQL拼接的问题
* if
```
 需要判断的时候，条件写在test中(只有一个条件)
```
* choose (when, otherwise) 
```
  需要选择一个条件的时候（多个条件）
```
* trim(where, set) 
```
需要去掉或加上sql语句中where、and、逗号之类的符号的时候。
例如BlogMapper.xml中Example_Where_Clause
```
* foreach
```
 需要遍历集合的时候
 属性有：item，collection，separator，open，close，index
 具体每个属性的使用方式见博客https://www.cnblogs.com/zzgno1/p/4184601.html
```
### 批量操作
* 在代码里写for循环操作数据(每次循环都要和数据库创建连接，非常损耗性能,严禁使用)
* 使用动态SQL的标签
```
例如：

 sql中批量插入的语法是：
 insert into tbl_emp (emp_id, emp_name, gender,email, d_id) 
 values ( ?,?,?,?,? ) , ( ?,?,?,?,? ) , ( ?,?,?,?,? ) , ( ?,?,?,?,? ) , 
 (?,?,?,?,?),(?,?,?,?,?),(?,?,?,?,?),(?,?,?,?,?),(?,?,?,?,?)
 那么就可以用foreacha动态拼接sql就可以了
 
 sql中批量更新的语句是
 update tbl_emp set 
 emp_name = 
    case emp_id
        when ? then ? 
        when ? then ? 
        when ? then ? 
        end ,
 gender = 
    case emp_id 
        when ? then ? 
        when ? then ? 
        when ? then ? 
        end 
 where emp_id in ( ? , ? )
 解释一下:
 上面是根据emp_id去更新emp_id和gender两个字段
 gender = 
     case emp_id 
         when ? then ? 
 意思是：当emp_id等于when后面问号的值时，gender就更新为then 后面的值
 
 mapper中可以这样实现：
 <update id="updateBatch"> 
    update tbl_emp 
    set emp_name = 
    <foreach collection="list" item="emps" index="index" 
    separator=" " open="case emp_id" close="end"> 
        when #{emps.empId} then #{emps.empName} 
    </foreach> 
    ,gender = 
    <foreach collection="list" item="emps" index="index" 
    separator=" " open="case emp_id" close="end"> 
        when #{emps.empId} then #{emps.gender} 
    </foreach> 
    where emp_id in 
    <foreach collection="list" item="emps" index="index" 
    separator="," open="(" close=")">
        #{emps.empId} 
    </foreach>
 </update> 
```
* BatchExecutor
```
 MyBatis的动态标签的批量操作也是存在一定的缺点的，
 比如数据量特别大的时候，拼接出来的SQL语句过大。
 这个时候就需要BatchExecutor
 BatchExecutor需要在一级标签settings中配置
 <setting name="defaultExecutorType" value="BATCH" />
 BatchExecutor底层是对JDBC中addBatch方法的封装（见测试目录下jdbc.JdbcTest类的testJdbcBatch方法）
 注意：
 1.该执行器不能执行select
 2.BatchExecutor的事务是没法自动提交的。
 因为BatchExecutor只有在调用了SqlSession的commit方法的时候，才会去执行executeBatch方法。   
```
### 数据库表关系
* 一对一
```
 例如
 一个文章对应一个作者(映射为java实体类见com.liyueze.mybatis.entry.associate.BlogAndAuthor)
```
* 一对多
```
 例如
 一个作者对应多个一个文章(映射为java实体类见com.liyueze.mybatis.entry.associate.AuthorAndBlog)
```
* 多对多
```
 例如
 一个文章对应多个评论用户
 一个评论用户对应多个文章
```
### 关联查询
* 一对一的关联查询
    * 嵌套结果
    ```
    一个sql查出来，用resultMap手动装载结果
    例如MyBatisTest下testSelectBlogWithAuthorResult方法
    ```
    * 嵌套查询
    ```
    分两次查询
    例如:MyBatisTest下testSelectBlogWithAuthorQuery方法
    当我们查询了博客文章信息之后，会再发送一条SQL到数据库查询作者信息
    这时会有N+1问题：
    当我们查询了博客文章信息之后，如果返回了N条数据，就需要再发送N条SQL到数据库查询作者信息
    解决方案：
    在MyBatis里面可以通过开启延迟加载的开关来解决这个问题(当调用getAuthor()时才会去查询)
    在settings中配置lazyLoadingEnabled,aggressiveLazyLoading,proxyFactory
    使用规则见mybatis-config.xml
    原理：
    mybatis不仅会做mapper接口的实现的代理。
    还会对查询返回的entry实例动手脚(mybatis中DefaultResultSetHandler.createResultObject()方法)
    AuthorAndBlog实例被代理了,getAuthor方法是被代理的方法
    ```
 

 
     