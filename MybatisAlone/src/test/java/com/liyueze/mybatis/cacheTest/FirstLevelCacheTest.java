package com.liyueze.mybatis.cacheTest;

import com.liyueze.mybatis.entry.Blog;
import com.liyueze.mybatis.mapper.BlogMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

public class FirstLevelCacheTest {
    /**
     * 测试一级缓存需要先关闭二级缓存，localCacheScope设置为SESSION
     * @throws IOException
     */
    @Test
    public void testCache() throws IOException {
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

        SqlSession session1 = sqlSessionFactory.openSession();
        SqlSession session2 = sqlSessionFactory.openSession();
        try {
            BlogMapper mapper0 = session1.getMapper(BlogMapper.class);
            BlogMapper mapper1 = session1.getMapper(BlogMapper.class);
            Blog blog = mapper0.selectBlogById(1);
            System.out.println(blog);

            System.out.println("第二次查询，相同会话，获取到缓存");
            System.out.println(mapper1.selectBlogById(1));

            System.out.println("第三次查询，不同会话，未获取到缓存");
            BlogMapper mapper2 = session2.getMapper(BlogMapper.class);
            System.out.println(mapper2.selectBlogById(1));

        } finally {
            session1.close();
        }
    }

    /**
     * 更新后缓存失效,一级缓存失效
     * @throws IOException
     */
    @Test
    public void testCacheInvalid() throws IOException {
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

        SqlSession session = sqlSessionFactory.openSession();
        try {
            BlogMapper mapper = session.getMapper(BlogMapper.class);
            System.out.println(mapper.selectBlogById(1));

            Blog blog = new Blog();
            blog.setBid(1);
            blog.setName("liyzzz");
            mapper.updateByPrimaryKey(blog);
            session.commit();

            // 相同会话执行了更新操作，缓存是否被清空？
            System.out.println("在执行更新操作之后，没有命中缓存");
            System.out.println(mapper.selectBlogById(1));

        } finally {
            session.close();
        }
    }

    /**
     * 一级缓存造成脏读
     * @throws IOException
     */
    @Test
    public void testCacheDirty() throws IOException {
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

        SqlSession session1 = sqlSessionFactory.openSession();
        SqlSession session2 = sqlSessionFactory.openSession();
        try {
            BlogMapper mapper0 = session1.getMapper(BlogMapper.class);
            BlogMapper mapper1 = session1.getMapper(BlogMapper.class);
            System.out.println(mapper0.selectBlogById(1));


            BlogMapper mapper2 = session2.getMapper(BlogMapper.class);
            Blog blog = new Blog();
            blog.setBid(1);
            blog.setName("Analyze");
            mapper2.updateByPrimaryKey(blog);
            session2.commit();

            // 不相同会话执行了更新操作，缓存是否被清空？
            System.out.println("不相同会话执行了更新操作，命中缓存，造成脏读");
            System.out.println(mapper1.selectBlogById(1));

        } finally {
            session1.close();
            session2.close();
        }
    }
}
