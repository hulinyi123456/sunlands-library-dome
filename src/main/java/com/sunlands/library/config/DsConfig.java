package com.sunlands.library.config;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * @author : hulin
 * @date : 2018/6/8 13:56
 * @description : 配置新的数据源Druid数据源
 */
@Configuration
@MapperScan(basePackages = "com.sunlands.library.mapper",sqlSessionTemplateRef = "mySqlSessionTemplate")
public class DsConfig {
    /** mapper文件路径 */
    @Value("${mybatis.mapper-locations}")
    private String myMapperLocations;

    /**
     *
     * 功能描述: 配置数据源
     *
     * @param: []
     * @return: javax.sql.DataSource
     * @auther: hulin
     * @date: 2018/6/8 14:33
     */
    @Bean("myDataSource")
    @ConfigurationProperties(prefix = "spring.datasource")
    @Primary
    public DataSource myDataSource(){
        return new DruidDataSource();
    }

    /**
     *
     * 功能描述: 配置会话工厂
     *
     * @param: [dataSource]
     * @return: org.apache.ibatis.session.SqlSessionFactory
     * @auther: hulin
     * @date: 2018/6/8 14:33
     */
    @Bean("mySqlSessionFactory")
    public SqlSessionFactory mySqlSessionFactory(@Qualifier("myDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);

        //指定mapper文件路径
        sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(myMapperLocations));

        //指定别名
        sqlSessionFactoryBean.setTypeAliasesPackage("com.sunlands.library.domain");

        return sqlSessionFactoryBean.getObject();
    }

    /**
     *
     * 功能描述: 配置事物管理器
     *
     * @param: [dataSource]
     * @return: org.springframework.jdbc.datasource.DataSourceTransactionManager
     * @auther: hulin
     * @date: 2018/6/8 14:42
     */
    @Bean("myTransactionManager")
    public DataSourceTransactionManager myTransactionManager(@Qualifier("myDataSource") DataSource dataSource){
        DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
        dataSourceTransactionManager.setDataSource(dataSource);
        return dataSourceTransactionManager;
    }

    /**
     *
     * 功能描述: 配置会话模板
     *
     * @param: [sqlSessionFactory]
     * @return: org.mybatis.spring.SqlSessionTemplate
     * @auther: hulin
     * @date: 2018/6/8 14:44
     */
    @Bean("mySqlSessionTemplate")
    public SqlSessionTemplate mySqlSessionTemplate(@Qualifier("mySqlSessionFactory") SqlSessionFactory sqlSessionFactory){
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
