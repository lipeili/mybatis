package com.lagou.sqlSession;

import com.lagou.pojo.Configuration;
import com.lagou.pojo.MappedStatement;

public interface Executor {

    Object execute (Configuration configuration, MappedStatement mappedStatement, Object... params) throws Exception;

}
