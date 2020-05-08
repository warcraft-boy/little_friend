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
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

/**
 * @Description: 主数据源配置
 * @Author chenjianwen
 * @Date 2020-03-24
 **/
@Configuration
@MapperScan(basePackages = "org.chen.master.dao", sqlSessionTemplateRef = "masterSqlSessionTemplate")
public class MasterDataSourceConfig {

    @Bean(name = "masterDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.master")
    @Primary
    public DataSource createMasterDataSource(){
        return new DruidDataSource();
    }

    @Bean(name = "masterSqlSessionFactory")
    @Primary
    public SqlSessionFactory createMasterSqlSessionFactory(@Qualifier("masterDataSource") DataSource dataSource) throws Exception {
        /**
         * 很多SqlSessionFactory都是这样配置的，但在使用 mybatisplus 的时候会报出如下错误
         * org.apache.ibatis.binding.BindingException: Invalid bound statement(not found)
         */
        //SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        MybatisSqlSessionFactoryBean bean = new MybatisSqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:mapper/master/*.xml"));
        return bean.getObject();
    }

    @Bean(name = "masterSqlSessionTemplate")
    @Primary
    public SqlSessionTemplate createMasterSqlSessionTemplate(@Qualifier("masterSqlSessionFactory") SqlSessionFactory sqlSessionFactory){
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
