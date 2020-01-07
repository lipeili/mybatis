package com.lagou.sqlSession;

import com.lagou.mapping.SqlCommandType;
import com.lagou.pojo.Configuration;
import com.lagou.pojo.MappedStatement;

import java.lang.reflect.*;
import java.util.List;

public class DefaultSqlSession implements SqlSession {

    private Configuration configuration;

    public DefaultSqlSession(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public <E> List<E> selectList(MappedStatement mappedStatement, Object... params) throws Exception {

        //将要去完成对simpleExecutor里的query方法的调用
        simpleExecutor simpleExecutor = new simpleExecutor();

        return (List<E>) simpleExecutor.execute(configuration, mappedStatement, params);
    }

    @Override
    public <T> T selectOne(MappedStatement mappedStatement, Object... params) throws Exception {
        List<Object> objects = selectList(mappedStatement, params);
        if(objects.size()==1){
            return (T) objects.get(0);
        }else {
            throw new RuntimeException("查询结果为空或者返回结果过多");
        }
    }

    @Override
    public int update(MappedStatement mappedStatement, Object... params) throws Exception {
        simpleExecutor simpleExecutor = new simpleExecutor();
        return (Integer)simpleExecutor.execute(configuration, mappedStatement, params);
    }

    @Override
    public <T> T getMapper(Class<?> mapperClass) {
        // 使用JDK动态代理来为Dao接口生成代理对象，并返回

        Object proxyInstance = Proxy.newProxyInstance(DefaultSqlSession.class.getClassLoader(), new Class[]{mapperClass}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                // 底层都还是去执行JDBC代码 //根据不同情况，来调用selctList或者selectOne
                // 准备参数 1：statmentid :sql语句的唯一标识：namespace.id= 接口全限定名.方法名
                // 方法名：findAll
                String methodName = method.getName();
                String className = method.getDeclaringClass().getName();

                String statementId = className+"."+methodName;
                MappedStatement mappedStatement = configuration.getMappedStatementMap().get(statementId);
                SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();

                if (sqlCommandType.equals(SqlCommandType.SELECT)) {
                    System.out.println("select ... ");

                    // 准备参数2：params:args
                    // 获取被调用方法的返回值类型
                    Type genericReturnType = method.getGenericReturnType();
                    // 判断是否进行了 泛型类型参数化
                    if(genericReturnType instanceof ParameterizedType){
                        List<Object> objects = selectList(mappedStatement, args);
                        return objects;
                    }
                    return selectOne(mappedStatement,args);
                } else if (sqlCommandType.equals(SqlCommandType.DELETE)) {
                    System.out.println("delete ... ");
                    return update(mappedStatement, args);
                } else if (sqlCommandType.equals(SqlCommandType.UPDATE)) {
                    System.out.println("update ... ");
                    return update(mappedStatement, args);
                } else if (sqlCommandType.equals(SqlCommandType.INSERT)) {
                    System.out.println("insert ... ");
                    return update(mappedStatement, args);
                } else {
                    System.out.println("doNothing ... ");
                    return update(mappedStatement, args);
                }

            }
        });

        return (T) proxyInstance;
    }


}
