package com.lagou.test;

import com.lagou.dao.IUserDao;
import com.lagou.io.Resources;
import com.lagou.pojo.User;
import com.lagou.sqlSession.SqlSession;
import com.lagou.sqlSession.SqlSessionFactory;
import com.lagou.sqlSession.SqlSessionFactoryBuilder;
import org.dom4j.DocumentException;
import org.junit.Before;
import org.junit.Test;

import java.beans.PropertyVetoException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;

public class IPersistenceTest {

    private SqlSession sqlSession;

    @Before
    public void before() throws DocumentException, PropertyVetoException, SQLException {
        InputStream resourceAsSteam = Resources.getResourceAsSteam("sqlMapConfig.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourceAsSteam);
        sqlSession = sqlSessionFactory.openSession();
    }

    @Test
    public void test_select() throws Exception {

        //调用
        User user = new User();
        user.setId(1);
        user.setUsername("张三");
      /*  User user2 = sqlSession.selectOne("user.selectOne", user);

        System.out.println(user2);*/

       /* List<User> users = sqlSession.selectList("user.selectList");
        for (User user1 : users) {
            System.out.println(user1);
        }*/

        IUserDao userDao = sqlSession.getMapper(IUserDao.class);

        List<User> all = userDao.findAll();
        for (User user1 : all) {
            System.out.println(user1);
        }
    }

    @Test
    public void test_delete() throws Exception {
        User user = new User();
        user.setId(2);
        user.setUsername("张八");

        IUserDao userDao = sqlSession.getMapper(IUserDao.class);
        int count = userDao.delete(user);
        System.out.println(count);

    }

    @Test
    public void test_insert() throws Exception {
        User user = new User();
        user.setId(2);
        user.setUsername("张八");

        IUserDao userDao = sqlSession.getMapper(IUserDao.class);
        int count = userDao.insert(user);
        System.out.println(count);

    }

    @Test
    public void test_update() throws Exception {
        User user = new User();
        user.setId(1);
        user.setUsername("琪格格");

        IUserDao userDao = sqlSession.getMapper(IUserDao.class);
        int count = userDao.update(user);
        System.out.println(count);

    }

}
