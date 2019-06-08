package com.liyueze.dbutils.dao;


import com.liyueze.dbutils.HikariUtil;
import com.liyueze.dbutils.entry.Blog;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.SQLException;
import java.util.List;

/**
 * DbBUtils
 * 使用queryRunner进行操作
 * 比有原生的JDBC操作
 */
public class BlogDao {

    private static QueryRunner queryRunner;
//    静态块获取代dbUtils的queryRunner
    static {
        queryRunner = HikariUtil.getQueryRunner();
    }

    // 返回单个对象，通过new BeanHandler<>(Class<?> clazz)来设置封装
    public static void selectBlog(Integer bid) throws SQLException {
        String sql = "select * from blog where bid = ? ";
        Object[] params = new Object[]{bid};
        Blog blog = queryRunner.query(sql, new BeanHandler<>(Blog.class), params);
        System.out.println(blog);
    }

    //返回列表，通过new BeanListHandler<>(Class<?> clazz)来设置List的泛型
    public static void selectList() throws SQLException {
        String sql = "select * from blog";
        List<Blog> list = queryRunner.query(sql, new BeanListHandler<>(Blog.class));
        list.forEach(System.out::println);
    }
}
