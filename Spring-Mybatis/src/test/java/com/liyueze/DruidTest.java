package com.liyueze;

import com.alibaba.druid.filter.config.ConfigTools;

/**
 * Druid实现数据库密码加密
 *
 * 根据该方法生成的密码需要改动配置文件：
 * 在jdbc.properties文件中
 * druid.password=加密后的密码
 * druid.public.key=公钥
 * druid.filters=config
 *
 * 在applicationContext.xml文件中配置数据源时
 * property name="filters" value="${druid.filters}"
 * property name="connectionProperties" value="config.decrypt=true;config.decrypt.key=${druid.public.key}"
 */
public class DruidTest {
    public static void main(String[] args) throws Exception {
        //密码明文
        String password = "123456";
        System.out.println("密码[ "+password+" ]的加密信息如下：\n");

        String [] keyPair = ConfigTools.genKeyPair(512);
        //私钥
        String privateKey = keyPair[0];
        //公钥
        String publicKey = keyPair[1];
        //用私钥加密后的密文
        password = ConfigTools.encrypt(privateKey, password);

        System.out.println("privateKey:"+privateKey);
        System.out.println("publicKey:"+publicKey);
        System.out.println("password:"+password);
        String decryptPassword=ConfigTools.decrypt(publicKey, password);
        System.out.println("decryptPassword："+decryptPassword);

    }
}
