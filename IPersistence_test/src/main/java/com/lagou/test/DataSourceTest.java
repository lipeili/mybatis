package com.lagou.test;

import com.mchange.v2.c3p0.DataSources;
import junit.framework.Assert;
import org.junit.Test;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @Description TODO
 * @Date 2020-01-05 21:08
 * @Created by videopls
 */
public class DataSourceTest {

    @Test
    public void testC3p0 () throws SQLException, PropertyVetoException {
//        ComboPooledDataSource dataSource = new ComboPooledDataSource();
//        dataSource.setDriverClass("com.mysql.jdbc.Driver");
//        dataSource.setJdbcUrl("jdbc:mysql:///zdy_mybatis?useUnicode=true&characterEncoding=utf8");
//        dataSource.setUser("root");
//        dataSource.setPassword("root123456");

        DataSource unpooledDataSource = DataSources.unpooledDataSource("jdbc:mysql:///zdy_mybatis?useUnicode=true&characterEncoding=utf8",
                "root","root123456");
        Connection connection1 = unpooledDataSource.getConnection();

        DataSource dataSource = DataSources.pooledDataSource(unpooledDataSource);
        Connection connection = dataSource.getConnection();
        Assert.assertNotNull(connection);
    }

}
