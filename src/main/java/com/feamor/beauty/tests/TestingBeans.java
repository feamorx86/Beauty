package com.feamor.beauty.tests;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Home on 09.05.2016.
 */
@Configuration
public class TestingBeans {

    @Bean (name = "test-template-scanner")
    TestRunnable getTemplateScannerTest() {
        return  new TemplateScannerTest();
    }
}
