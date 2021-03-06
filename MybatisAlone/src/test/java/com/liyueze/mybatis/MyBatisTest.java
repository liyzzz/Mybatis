package com.liyueze.mybatis;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.liyueze.mybatis.entry.Blog;
import com.liyueze.mybatis.entry.associate.AuthorAndBlog;
import com.liyueze.mybatis.entry.associate.BlogAndAuthor;
import com.liyueze.mybatis.entry.associate.BlogAndComment;
import com.liyueze.mybatis.mapper.BlogMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.RowBounds;
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

    /**
     * 逻辑分页
     * @throws IOException
     */
    @Test
    public void testSelectByRowBounds() throws IOException {
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

        SqlSession session = sqlSessionFactory.openSession();
        try {
            BlogMapper mapper = session.getMapper(BlogMapper.class);
            int start = 0; // offset
            int pageSize = 5; // limit
            RowBounds rb = new RowBounds(start, pageSize);
            List<Blog> list = mapper.selectBlogList(rb); // 使用逻辑分页
            for(Blog b :list){
                System.out.println(b);
            }
        } finally {
            session.close();
        }
    }

    /**
     * 物理分页
     * 使用pageHelp插件
     * 使用方法：
     * 首先需要maven引入jar包
     * 在mybatis-config.xml配置plugin
     * 如下例使用
     * 详细示例见https://github.com/pagehelper/Mybatis-PageHelper/blob/master/wikis/zh/HowToUse.md官方文档
     * @throws IOException
     */
    @Test
    public void testSelectByPageHelp() throws IOException {
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

        SqlSession session = sqlSessionFactory.openSession();
        try {
            BlogMapper mapper = session.getMapper(BlogMapper.class);
            PageHelper.offsetPage(1, 5);
            List<Blog> blogs = mapper.selectBlogList();
            //用PageInfo对结果进行包装
            PageInfo page = new PageInfo(blogs);
            //测试PageInfo全部属性
            System.out.println(blogs);
            //PageInfo包含了非常全面的分页属性
            //获取当前页有记录
            System.out.println(page.getPageNum());
            //获取最后一页页码
            System.out.println(page.getLastPage());
        } finally {
            session.close();
        }
    }


    /**
     * 测试自己写的分页插件和log插件
     * @throws IOException
     */
    @Test
    public void testSelectByMyRowBounds() throws IOException {
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

        SqlSession session = sqlSessionFactory.openSession();
        try {
            BlogMapper mapper = session.getMapper(BlogMapper.class);
            int start = 0; // offset
            int pageSize = -1; // 这里的limit可以随意设置，会被配置里的pageSize覆盖
            RowBounds rb = new RowBounds(start, pageSize);
            List<Blog> list = mapper.selectBlogList(rb); // 使用逻辑分页
            for(Blog b :list){
                System.out.println(b);
            }
        } finally {
            session.close();
        }
    }


}
