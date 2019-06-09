package com.liyueze.dbutils;


import com.liyueze.dbutils.dao.BlogDao;
/**
 * DbBUtils
 * 使用queryRunner进行数据库操作
 * 和原生的JDBC操作相比
 * 资源管理（注册驱动，获取连接，关闭连接等）都不用关注
 * 对操作数据的增删改查的方法进行了封装
 *
 *  但是还存在一些缺点
 *  SQL 语句都是写死在代码里面的，依旧存在硬编码的问题；
 *  参数只能按固定位置的顺序传入（数组），它是通过占位符去替换的，不能自动映射；（比如传入一个对象，根据对象中的一些属性值传入）
 *  查询没有缓存的功能
 *
 */
public class Main {
    public static void main(String[] args) throws Exception {
        HikariUtil.init();
        BlogDao.selectBlog(1);
        BlogDao.selectList();
    }
}
