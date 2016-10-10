package com.feamor.beauty;

import com.feamor.beauty.blocks.BaseBlockControl;
import com.feamor.beauty.blocks.PageBlockWithHeader;
import com.feamor.beauty.blocks.SimpleBlockControlWithText;
import com.feamor.beauty.blocks.commons.*;
import com.feamor.beauty.blocks.home.HomeContentControl;
import com.feamor.beauty.blocks.home.HomeNewsBlock;
import com.feamor.beauty.blocks.templateeditor.*;
import com.feamor.beauty.controllers.*;
import com.feamor.beauty.controllers.mobileapp.AppStartDataManager;
import com.feamor.beauty.controllers.mobileapp.AuthorizationScreenController;
import com.feamor.beauty.controllers.mobileapp.MainMenuManager;
import com.feamor.beauty.controllers.mobileapp.MainScreenController;
import com.feamor.beauty.dao.*;
import com.feamor.beauty.managers.ControlsManager;
import com.feamor.beauty.managers.RequestManager;
import com.feamor.beauty.models.db.*;
import com.feamor.beauty.pages.BasePageControl;
import com.feamor.beauty.pages.PageWithTemplateControl;
import com.feamor.beauty.views.mobileapp.AppStartDataJsonView;
import com.feamor.beauty.views.ViewFactory;
import com.feamor.beauty.views.mobileapp.MainMenuManagerJsonView;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.hibernate.SessionFactory;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBuilder;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.sql.DataSource;
import java.io.File;

/**
 * Created by Home on 13.02.2016.
 */
@Configuration
@EnableWebMvc
@EnableTransactionManagement
public class Config extends WebMvcConfigurerAdapter {
//    @Bean
//    public TestController getTestController() {
//        return new TestController();
//    }

    @Bean
    public PageDao getPageDao(){
        return new PageDao();
    }

    @Bean
    public SiteDao getSiteDao() {
        return  new SiteDao();
    }

    @Bean
    public UserDao getUserDao() {
        return  new UserDao();
    }

    @Bean
    public GroupDataDAO groupDataDAO() {
        return new GroupDataDAO();
    }

//    @Bean
//    public HibernateTemplate hibernateTemplate() {
//        return new HibernateTemplate(sessionFactory());
//    }

    @Bean
    public SessionFactory sessionFactory() {
        return new LocalSessionFactoryBuilder(getDataSource())
                //map pages
                .addAnnotatedClass(PageBlock.class)
                .addAnnotatedClass(PageBlockData.class)
                .addAnnotatedClass(BlockTemplate.class)
                .addAnnotatedClass(PageData.class)
                .addAnnotatedClass(Page.class)
                //map user
                .addAnnotatedClass(UserData.class)
                .addAnnotatedClass(UserGroup.class)
                .addAnnotatedClass(UserGroupData.class)
                .addAnnotatedClass(User.class)

//                .setProperty("hibernate.current_session_context_class", "thread")
                .setProperty("hibernate.show_sql", "true")
                .setProperty("hibernate.connection.CharSet","utf8")
                .setProperty("hibernate.connection.characterEncoding", "utf8")
                .setProperty("hibernate.connection.useUnicode", "true")
                .setProperty("hibernate.format_sql", "true")
                //.setProperty("hibernate.max_fetch_depth", "1")
                .setProperty("hibernate.dialect", "com.feamor.beauty.utils.PostgreSQLDialectWithArrays")
                .buildSessionFactory( );
    }

    @Bean
    public DataSource getDataSource() {
        /*DataSource dataSource = new  BasicDataSource();
        ((BasicDataSource)dataSource).setDriverClassName("org.postgresql.Driver");
//        dataSource.setUrl("jdbc:postgresql://192.168.187.129:5432/beauty");
        ((BasicDataSource)dataSource).setUrl("jdbc:postgresql://127.0.0.1:5432/beauty");
        ((BasicDataSource)dataSource).setUsername("testing");
        ((BasicDataSource)dataSource).setPassword("testing");

        */
        HikariConfig config = new HikariConfig();
        config.setDriverClassName("org.postgresql.Driver");
        config.setJdbcUrl("jdbc:postgresql://127.0.0.1:5432/beauty");
        config.setUsername("testing");
        config.setPassword("testing");
        config.addDataSourceProperty("characterEncoding","utf8");
        config.addDataSourceProperty("useUnicode","true");
        config.setIdleTimeout(10);
        config.setMinimumIdle(2);
        config.setMaximumPoolSize(100);


        return new HikariDataSource(config);
    }

    @Bean(name = "transactionManager")
    public HibernateTransactionManager transactionManager(){
        return new HibernateTransactionManager(sessionFactory());
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String path1 = new File(".").getAbsolutePath();
        String path2 = new File("src/web").getAbsolutePath();
        System.out.println("app path : "+path1);
        System.out.println("web path : "+path2);
        registry
            .addResourceHandler("/web/**")
            .addResourceLocations("file:./src/web/");
        registry
            .addResourceHandler("/favicon.ico")
            .addResourceLocations("file:./src/favicon.ico");

    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Bean
    public Texts texts() {
        return  new Texts();
    }

    //------------------------------------------------
    //  Managers
    @Bean
    public RequestManager requestManager() {
        return new RequestManager();
    }

    @Bean
    public ControlsManager controlsManager() {
        return new ControlsManager();
    }

    @Bean
    public ViewFactory viewFactory() {
        return new ViewFactory();
    }

//    @Bean
//    public SimpleMappingExceptionResolver simpleMappingExceptionResolver() {
//        MapBasedExceptionResolver resolver = new MapBasedExceptionResolver();
//        return resolver;
//    }

    @Bean
    public ErrorHandlerController errorHandlerController(ErrorAttributes errorAttributes) {
        return new ErrorHandlerController(errorAttributes, new ErrorProperties() {
            @Override
            public String getPath() {
                return "/error";
            }
            @Override
            public IncludeStacktrace getIncludeStacktrace() {
                return IncludeStacktrace.ALWAYS;
            }
        });
    }

    //------------------------------------------------
    //  Controllers
    @Bean
    public BaseController baseController() {
        return  new BaseController();
    }

    @Bean
    public BaseControllerWithPage controllerWithPage() {
        return  new BaseControllerWithPage();
    }

    @Bean
    public HomePageController homePageController() {
        return new HomePageController();
    }

    @Bean
    public TestPageController testPageController(){
        return new TestPageController();
    }

    @Bean
    public SimpleTemplateEditor simpleTemplateEditor() {
        return  new SimpleTemplateEditor();
    }

    @Bean
    public PageMappingEditor pageMappingEditor() {return  new PageMappingEditor(); }

    @Bean
    public ShareViewController shareViewController() {return new ShareViewController(); }

    //------------------------------------------------
    //  Mobile app controllers

    @Bean
    public AppStartDataManager appStartDataManager() {
        return new AppStartDataManager();
    }

    @Bean
    public AuthorizationScreenController authorizationScreenController() {
        return new AuthorizationScreenController();
    }

    @Bean
    public MainScreenController mainScreenController() {
        return new MainScreenController();
    }

    @Bean
    public MainMenuManager mainMenuManager() {
        return new MainMenuManager();
    }

    //------------------------------------------------
    //  Page controls
    @Bean
    public BasePageControl basePageControl() {
        return  new BasePageControl();
    }

    @Bean
    public PageWithTemplateControl pageWithTemplateControl() {
        return  new PageWithTemplateControl();
    }
    //------------------------------------------------
    //  Block controls

    @Bean
    public BaseBlockControl baseBlockControl() {
        return  new BaseBlockControl();
    }

    @Bean
    public PageBlockWithHeader pageBlockWithHeader() {
        return  new PageBlockWithHeader();
    }

    @Bean
    public NavigationControl navigationControl() {return new NavigationControl();}

    @Bean
    public NavigationItemControl navigationItemControl() {return new NavigationItemControl();}

    @Bean
    public SimpleBlockControlWithText simpleBlockControlWithText() {
        return  new SimpleBlockControlWithText();
    }

    @Bean
    public TemplateEditorPageControl templateEditorPageControl() {
        return new TemplateEditorPageControl();
    }

    @Bean
    public TemplateEditorError templateEditorError() {
        return new TemplateEditorError();
    }

    @Bean
    public TemplateEditorItem templateEditorItem() {
        return new TemplateEditorItem();
    }

    @Bean
    public TemplateEditorList templateEditorList() {
        return new TemplateEditorList();
    }

    @Bean
    public TemplateEditorMenuItem templateEditorMenuItem() {
        return new TemplateEditorMenuItem();
    }

    @Bean
    public TemplateEditorViewer templateEditorViewer() {
        return new TemplateEditorViewer();
    }

    @Bean
    public HomeContentControl homeContentControl() {return new HomeContentControl();}

    @Bean
    public HomeNewsBlock homeNewsBlock() {return new HomeNewsBlock();}

    @Bean
    public FooterControl footerControl() { return  new FooterControl(); }

    @Bean
    public FooterAboutControl footerAboutControl() { return  new FooterAboutControl(); }

    @Bean
    public PageControl pageControl() {return new PageControl();}

    //------------------------------------------------
    //  View renders
    @Bean
    public AppStartDataJsonView appStartDataJsonView() {
        return new AppStartDataJsonView();
    }

    @Bean
    public MainMenuManagerJsonView mainMenuManagerJsonView() {
        return new MainMenuManagerJsonView();
    }

//    @Bean
//    public () {return new ();}

}
