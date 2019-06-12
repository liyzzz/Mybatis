package com.liyueze.mybatis.objectFacoty;

import com.liyueze.mybatis.entry.Blog;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

/**
 * DefaultObjectFactory 有两种构造方法：
 * 一种是无参构造函数，一种是有参构造函数，无参构造函数调用了有参构造函数
 * 需要在mybatis-config.xml中注册
 * 在返回的结果集转换为实体类的时候时，需要创建对象，这时就会调用该类create()方法
 * 测试用例：测试下任意一个返回blog对象的查询
 */
public class MyObjectFacoty extends DefaultObjectFactory {
    private List<Class> createClass=new ArrayList<>();
    @Override
    public Object create(Class type) {
        System.out.println("我的工厂创建对象方法：" + type);
        if (createClass.contains(type)) {
            Blog blog = (Blog) super.create(type);
            //如果做对属性的赋值操作，会被mybatis中创建好对象利用反射把查询出来的值覆盖(mybatis中DefaultResultSetHandler中getRowValue方法)
            blog.setName("My object factory");
            blog.setBid(1111);
            blog.setAuthorId(2222);
            return blog;
        }
        //如果不是Blog类型就调用默认的构造方法
        Object result = super.create(type);
        return result;
    }

    //有参构造
    @Override
    public <T> T create(Class<T> type, List<Class<?>> constructorArgTypes, List<Object> constructorArgs) {
        return super.create(type, constructorArgTypes, constructorArgs);
    }

    //为自定义ObjectFactory设置配置参数
    @Override
    public void setProperties(Properties properties) {
        //迭代遍历所有配置参数
        Iterator iterator = properties.keySet().iterator();
        while (iterator.hasNext()) {
            String keyValue = String.valueOf(iterator.next());
            System.out.println(keyValue+":"+properties.getProperty(keyValue));
            try {
                createClass.add(Class.forName(properties.getProperty(keyValue)));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        super.setProperties(properties);
    }

}


