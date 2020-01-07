package com.lagou.sqlSession;

import com.lagou.pojo.MappedStatement;

import java.util.List;

public interface SqlSession {

    //查询所有
    <E> List<E> selectList(MappedStatement mappedStatement,Object... params) throws Exception;

    //根据条件查询单个
    <T> T selectOne(MappedStatement mappedStatement,Object... params) throws Exception;

    int update(MappedStatement mappedStatement, Object... params) throws Exception;

    //为Dao接口生成代理实现类
    <T> T getMapper(Class<?> mapperClass);


}
