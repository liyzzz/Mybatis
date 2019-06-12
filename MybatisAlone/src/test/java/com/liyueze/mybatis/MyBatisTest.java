package com.liyueze.mybatis;


import com.liyueze.mybatis.entry.Blog;
import com.liyueze.mybatis.entry.associate.AuthorAndBlog;
import com.liyueze.mybatis.entry.associate.BlogAndAuthor;
import com.liyueze.mybatis.entry.associate.BlogAndComment;
import com.liyueze.mybatis.mapper.BlogMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;


public class MyBatisTest {
    /**
     * 使用iBatis方式
     * @throws IOException
     */
    @Test
    public void testStatement() throws IOException {
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession session = sqlSessionFactory.openSession();
        try {
            Blog blog = (Blog) session.selectOne("com.liyueze.mybatis.mapper.BlogMapper.selectBlogById", 1);
            System.out.println(blog);
        } finally {
            session.close();
        }
    }
    /**
     * 通过 SqlSession.getMapper(XXXMapper.class)  接口方式
     * @throws IOException
     */
    @Test
    public void testSelect() throws IOException {
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

        SqlSession session = sqlSessionFactory.openSession(); // ExecutorType.BATCH
        try {
            BlogMapper mapper = session.getMapper(BlogMapper.class);
            Blog blog = mapper.selectBlogById(1);
            System.out.println(blog);
        } finally {
            session.close();
        }
    }

    /**
     * 测试插入
     * @throws IOException
     */
    @Test
    public void testInsert() throws IOException {
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

        SqlSession session = sqlSessionFactory.openSession();
        try {
            BlogMapper mapper = session.getMapper(BlogMapper.class);
            Blog blog = new Blog();
            blog.setBid(16);
            blog.setName(new String("1111"));
            blog.setAuthorId(1111);
            System.out.println(mapper.insertBlog(blog));
            session.commit();
        } finally {
            session.close();
        }
    }

    /**
     * 测试environment
     * @throws IOException
     */
    @Test
    public void testEnvironment() throws IOException {
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
//        根据mybatis-config.xml配置文件中配置的environment的ID来选择数据源
//        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream,"test");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream,"development");
        SqlSession session = sqlSessionFactory.openSession();
        try {
            BlogMapper mapper = session.getMapper(BlogMapper.class);
            System.out.println(mapper.selectBlogById(1));
        } finally {
            session.close();
        }
    }

    /**
     * 一对一，一篇文章对应一个作者
     * 嵌套结果，不存在N+1问题
     */
    @Test
    public void testSelectBlogWithAuthorResult() throws IOException {
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession session = sqlSessionFactory.openSession();
        BlogMapper mapper = session.getMapper(BlogMapper.class);

        BlogAndAuthor blog = mapper.selectBlogWithAuthorResult(1);
        System.out.println("-----------:"+blog);
    }

    /**
     * 一对一，一篇文章对应一个作者
     * 嵌套查询，会有N+1的问题
     */
    @Test
    public void testSelectBlogWithAuthorQuery() throws IOException {
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession session = sqlSessionFactory.openSession();
        BlogMapper mapper = session.getMapper(BlogMapper.class);

        BlogAndAuthor blog = mapper.selectBlogWithAuthorQuery(1);
        System.out.println("-----------:"+blog.getClass());
        // 如果开启了延迟加载，会在使用的时候才发出SQL
        // equals,clone,hashCode,toString也会触发延迟加载
        // System.out.println("-----------调用toString方法:"+blog);
        System.out.println("-----------getAuthor:"+blog.getAuthor().toString());
        // 如果 aggressiveLazyLoading = true ，也会触发加载，否则不会
        //System.out.println("-----------getName:"+blog.getName());
    }

    /**
     * 一对多关联查询：查询某一篇文章和对应多条评论
     * @throws IOException
     */
    @Test
    public void testSelectBlogWithComment() throws IOException {
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

        SqlSession session = sqlSessionFactory.openSession();
        try {
            BlogMapper mapper = session.getMapper(BlogMapper.class);
            BlogAndComment blog = mapper.selectBlogWithCommentById(1);
            System.out.println(blog);
        } finally {
            session.close();
        }
    }

    /**
     * 多对多关联查询：查询每个作者的文章的评论
     * @throws IOException
     */
    @Test
    public void testSelectAuthorWithBlog() throws IOException {
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

        SqlSession session = sqlSessionFactory.openSession();
        try {
            BlogMapper mapper = session.getMapper(BlogMapper.class);
            List<AuthorAndBlog> authors = mapper.selectAuthorWithBlog();
            for (AuthorAndBlog author : authors){
                System.out.println(author);
            }
        } finally {
            session.close();
        }
    }



}
