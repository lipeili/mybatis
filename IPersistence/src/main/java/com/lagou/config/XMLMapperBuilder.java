package com.lagou.config;

import com.lagou.mapping.SqlCommandType;
import com.lagou.pojo.Configuration;
import com.lagou.pojo.MappedStatement;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.List;

public class XMLMapperBuilder {

    private Configuration configuration;

    private String namespace;

    public XMLMapperBuilder(Configuration configuration) {
        this.configuration =configuration;
    }

    public void parse(InputStream inputStream) throws DocumentException {

        Document document = new SAXReader().read(inputStream);
        Element rootElement = document.getRootElement();

        namespace = rootElement.attributeValue("namespace");

        for (Element element : (List<Element>)rootElement.selectNodes("//select")) {
            buildMappedStatement(element,SqlCommandType.SELECT);
        }


        for (Element element : (List<Element>)rootElement.selectNodes("//update")) {
            buildMappedStatement(element,SqlCommandType.UPDATE);
        }

        for (Element element : (List<Element>)rootElement.selectNodes("//delete")) {
            buildMappedStatement(element,SqlCommandType.DELETE);
        }

        for (Element element : (List<Element>)rootElement.selectNodes("//insert")) {
            buildMappedStatement(element,SqlCommandType.INSERT);
        }

    }

    private void buildMappedStatement (Element element,SqlCommandType sqlCommandType) {
        String id = element.attributeValue("id");
        String resultType = element.attributeValue("resultType");
        String paramterType = element.attributeValue("paramterType");
        String sqlText = element.getTextTrim();
        MappedStatement mappedStatement = new MappedStatement();
        mappedStatement.setId(id);
        mappedStatement.setResultType(resultType);
        mappedStatement.setParamterType(paramterType);
        mappedStatement.setSql(sqlText);
        mappedStatement.setSqlCommandType(sqlCommandType);
        String key = namespace+"."+id;
        configuration.getMappedStatementMap().put(key,mappedStatement);
    }


}
