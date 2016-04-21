package com.feamor.beauty;

import com.feamor.beauty.dao.PageDao;
import com.feamor.beauty.models.*;
import org.apache.commons.dbcp.BasicDataSource;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBuilder;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.sql.DataSource;

/**
 * Created by Home on 13.02.2016.
 */
@Configuration
@EnableWebMvc
@EnableTransactionManagement
public class Config {
//    @Bean
//    public TestController getTestController() {
//        return new TestController();
//    }

    @Bean
    public PageDao getPageDao(){
        return new PageDao();
    }

//    @Bean
//    public HibernateTemplate hibernateTemplate() {
//        return new HibernateTemplate(sessionFactory());
//    }

    @Bean
    public SessionFactory sessionFactory() {
        return new LocalSessionFactoryBuilder(getDataSource())
                .addAnnotatedClass(BlockParameter.class)
                .addAnnotatedClass(BlockType.class)
                .addAnnotatedClass(Page.class)
                .addAnnotatedClass(PageBlock.class)
                .addAnnotatedClass(PageType.class)
                .addAnnotatedClass(ParameterType.class)
//                .setProperty("hibernate.current_session_context_class", "thread")
                .setProperty("hibernate.show_sql", "true")
                .setProperty("hibernate.format_sql", "true")
                .setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQL9Dialect")
                .buildSessionFactory();
    }

    @Bean
    public DataSource getDataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName("org.postgresql.Driver");
        dataSource.setUrl("jdbc:postgresql://127.0.0.1:5432/testdb");
        dataSource.setUsername("testing");
        dataSource.setPassword("testing");

        return dataSource;
    }

    @Bean(name = "transactionManager")
    public HibernateTransactionManager transactionManager(){
        return new HibernateTransactionManager(sessionFactory());
    }
}
