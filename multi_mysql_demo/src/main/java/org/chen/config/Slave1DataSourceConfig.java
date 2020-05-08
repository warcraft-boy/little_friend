package org.chen.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

/**
 * @Description: slave1副数据源配置
 * @Author chenjianwen
 * @Date 2020-03-24
 **/
@Configuration
@MapperScan(basePackages = "org.chen.slave1.dao", sqlSessionTemplateRef = "slave1SqlSessionTemplate")
public class Slave1DataSourceConfig {

    @Bean(name = "slave1DataSource")
    @ConfigurationProperties(prefix = "spring.datasource.slave1")
    public DataSource createSlave1DataSource(){
        return new DruidDataSource();
    }

    @Bean(name = "slave1SqlSessionFactory")
    public SqlSessionFactory createSlave1SqlSessionFactory(@Qualifier("slave1DataSource") DataSource dataSource) throws Exception {
        /**
         * 很多SqlSessionFactory都是这样配置的，但在使用 mybatisplus 的时候会报出如下错误
         * org.apache.ibatis.binding.BindingException: Invalid bound statement(not found)
         */
        //SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        MybatisSqlSessionFactoryBean bean = new MybatisSqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/slave1/*.xml"));
        return bean.getObject();
    }

    @Bean(name = "slave1SqlSessionTemplate")
    public SqlSessionTemplate createSqlSessionTemplate(@Qualifier("slave1SqlSessionFactory") SqlSessionFactory sqlSessionFactory){
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
