package com.liyueze.dbutils;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;


public class HikariUtil {
    private static final String PROPERTY_PATH = "/hikari.properties";
    private static final Logger LOGGER = LoggerFactory.getLogger(HikariUtil.class);
    private static HikariDataSource dataSource;
    private static QueryRunner queryRunner;

    /**
     * 静态innit方法加载数据源（c3p0）配置
     */
    public static void init() {
        HikariConfig config = new HikariConfig(PROPERTY_PATH);
        config.setDriverClassName("com.mysql.cj.jdbc.MysqlDataSource");
        dataSource = new HikariDataSource(config);
        queryRunner = new QueryRunner(dataSource);
    }
//    QueryRunner中提供对sql语句操作的API
    public static QueryRunner getQueryRunner() {
        check();
        return queryRunner;
    }

    public static Connection getConnection() {
        check();
        try {
            Connection connection = dataSource.getConnection();
            return connection;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void close(Connection connection) {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void check() {
        if (dataSource == null || queryRunner == null) {
            throw new RuntimeException("DataSource has not been init");
        }
    }
}