package com.liyueze.mybatis.typeHandler;

import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * java类型为String的类型转换
 * 需要在mybatis-config.xml中注册该TypeHandler
 * 注意:在字段上添加typeHandler属性才会生效(在BlogMapper.xml中BaseResultMap配置)
 */
public class MyTypeHandler extends BaseTypeHandler<String> {
    /**
     * Java类型到JDBC类型
     * 设置 String 类型的参数的时候调用
     * 注意只有在字段上添加typeHandler属性才会生效
     * 调用时期：例如 insert Blog表 name字段
     * @param preparedStatement 当前的PreparedStatement对象
     * @param i 当前参数的位置
     * @param parameter 当前参数的Java对象
     * @param jdbcType 当前参数的数据库类型
     * @throws SQLException
     */
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, String parameter, JdbcType jdbcType) throws SQLException {
        System.out.println("---------------setNonNullParameter1："+preparedStatement);
        preparedStatement.setString(i, parameter);
    }

    /**
     * JDBC类型到java类型
     * 注意只有在字段上添加typeHandler属性才会生效
     * @param resultSet 当前的结果集
     * @param columnName 当前的字段名称
     * @return T 转换后的Java对象
     * @throws SQLException
     */
    public String getNullableResult(ResultSet resultSet, String columnName) throws SQLException {
        System.out.println("---------------getNullableResult1："+columnName);
        return resultSet.getString(columnName);
    }


    /**
     * 一下两个方法都是更具下标获取时才会调用(不常用)
     * @param resultSet
     * @param i
     * @return
     * @throws SQLException
     */
    public String getNullableResult(ResultSet resultSet, int i) throws SQLException {
        return null;
    }

    public String getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        return null;
    }
}
