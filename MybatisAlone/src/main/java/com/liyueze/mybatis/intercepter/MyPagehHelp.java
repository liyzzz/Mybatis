package com.liyueze.mybatis.intercepter;

import org.apache.ibatis.builder.StaticSqlSource;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.lang.reflect.Field;
import java.util.Properties;

/**
 *
 * 自定义分页插件的实现：将mybatis根据RowBounds实现的逻辑翻页转变成物理翻页
 *
 * @Intercepts(@Signature())的作用
 * 申明要拦截的对象的方法
 * 可以拦截的对象有：
 * Executor(创建时间:sqlSession一起创建)
 * StatementHandle(执行 SQL 的过程，最常用的拦截对象)
 * ParameterHandler(SQL参数组装的过程)
 * ResultSetHandler(结果的组装)
 */
@Intercepts(@Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}))
public class MyPagehHelp implements Interceptor {

    private Integer pageSize;

    /**
     * 用于覆盖被拦截对象(注解表明的对象的方法)的原有方法（在调用代理对象Plugin的invoke()方法时被调用）
     *
     * @param invocation
     * @return
     * @throws Throwable
     */
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        System.out.println("将逻辑分页改为物理分页");
        Object[] args = invocation.getArgs();
        MappedStatement ms = (MappedStatement) args[0]; // MappedStatement
        BoundSql boundSql = ms.getBoundSql(args[1]); // Object parameter
        RowBounds rb = (RowBounds) args[2]; // RowBounds
        // RowBounds为空，无需分页
        if (rb == RowBounds.DEFAULT) {
            return invocation.proceed();
        }
        // 将原 RowBounds 参数设为 RowBounds.DEFAULT，关闭 MyBatis 内置的分页机制
        args[2] = RowBounds.DEFAULT;

        // 在SQL后加上limit语句
        String sql = boundSql.getSql();
        String limit = String.format("LIMIT %d,%d", rb.getOffset(), pageSize);
        sql = sql + " " + limit;

        // 自定义sqlSource
        SqlSource sqlSource = new StaticSqlSource(ms.getConfiguration(), sql, boundSql.getParameterMappings());

        // 修改原来的sqlSource
        Field field = MappedStatement.class.getDeclaredField("sqlSource");
        field.setAccessible(true);
        field.set(ms, sqlSource);

        return invocation.proceed();
    }

    /**
     * target是被拦截的对象，这个方法的作用是给被拦截的对象生成一个代理对象，并返回他
     * @param target
     * @return
     */
    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target,this);
    }

    /**
     * 读取在配置文件里参数的参数
     * @param properties
     */
    @Override
    public void setProperties(Properties properties) {
        if( properties.getProperty("pageSize")!=null){
            this.pageSize=Integer.parseInt( properties.getProperty("pageSize"));
        }
    }
}
