# SSM整合的web示例(使用restful风格)
## 使用说明
* 首先应使用test下的DruidTest对数据库连接进行加密，将加密信息修改至配置文件中
* MvcTest 是使用MockMvc来模拟请求测试controller
## mybatis整合spring原理
* 1.在applicationContext.xml文件中申明sqlSessionFactoryBean
```
    <bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
            <property name="configLocation" value="classpath:mybatis-config.xml"></property>
            <property name="mapperLocations" value="classpath:mapper/*.xml"></property>
            <property name="dataSource" ref="dataSource"/>
    </bean>
    
    spring创建SqlSessionFactoryBean的bean，该bean是一个FactoryBean
    getObject方法会返回读取mybatis的配置信息的defaultSqlSessionFactory(mybatis中的默认SQL回话工厂)
```
* 2.配置mapper接口的扫描
```
    <bean id="mapperScanner" class="org.mybatis.spring.mapper.MapperScannerConfigurer">
        <property name="basePackage" value="com.liyueze.crud.dao"/>
    </bean>
    
    MapperScannerConfigurer实现BeanDefinitionRegistryPostProcessor(自定义注册bean的逻辑(逻辑在BeanDefinitionRegistryPostProcessor方法中))
    
    在ClassPathMapperScanner类中的processBeanDefinitions方法，有这样一句关键的代码：
    
    definition.setBeanClass(this.mapperFactoryBean.getClass());
    
    说明注入在service层的dao实际上是一个mapperFactoryBean，而不是mapper接口
    而mapperFactoryBean也实现了factoryBean
    观察getObject方法，发现了单独使用mybatis时的代码：getSqlSession().getMapper(this.mapperInterface);
   
   这里又发现了一个有意思的现象，getSqlSession返回的不是mybaits中的sqlSession,而是sqlSessionTemplate
   
   mybatis中的sqlSession是线程不安全的，只能一个请求创建一个sqlSession
   而sqlSessionTemplate是线程安全的。
   sqlSessionTemplate会根据设置的SqlSessionFatory，每个mapper接口创建一个sqlSessionTemplate
   
   sqlSessionTemplate对象有一个很重要的属性  sqlSessionProxy
   这个  sqlSessionProxy是对原生mybatis的SQLSession的代理
   查看sqlSessionTemplate的构造方法，发现这样一句代码
   
   this.sqlSessionProxy = (SqlSession) newProxyInstance(
           SqlSessionFactory.class.getClassLoader(),
           new Class[] { SqlSession.class },
           new SqlSessionInterceptor());
   
   被代理类是SqlSession,代理类是SqlSessionInterceptor，该类的invoke方法是真正的执行方法
   
   这时发现了mybatis中原生的SQLSession
   
   SqlSession sqlSession = getSqlSession(
             SqlSessionTemplate.this.sqlSessionFactory,
             SqlSessionTemplate.this.executorType,
             SqlSessionTemplate.this.exceptionTranslator);
   
   getSqlSession方法就是为了解决线程安全的问题
   
   getSqlSession会从事务（TransactionSynchronizationManager）中获得sqlSession
   
   TransactionSynchronizationManager的属性：
   private static final ThreadLocal<Map<Object, Object>> resources =
   			new NamedThreadLocal<>("Transactional resources");
   保存着所有的SQLSession
```
