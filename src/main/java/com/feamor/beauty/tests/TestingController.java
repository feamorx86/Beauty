package com.feamor.beauty.tests;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Scope;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Home on 09.05.2016.
 */
//@Transactional
//@RestController
public class TestingController {
/*
    private Map<String, TestRunnable> tests;

    @Autowired
    private ApplicationContext context;

    private void initializeTests() {

    }

//    @RequestMapping(value = "/tests/", produces = "text/html;charset=utf-8")
//    @ResponseBody
    public String listTests() {
        if (tests == null) {
            tests = context.getBeansOfType(TestRunnable.class);
        }

        StringBuilder builder = new StringBuilder();
        builder.append("<html><body><H1>Tests</h1>");
        for(Map.Entry<String, TestRunnable> test : tests.entrySet()) {
            String name = test.getKey();
            builder.append("<a href=\"/tests/"+name+"\"><p>"+name+"</p></a>");
        }
        builder.append("</body></html>");
        return builder.toString();
    }

//    @RequestMapping(value = "/tests/{testId}", produces = "text/html;charset=utf-8")
//    @ResponseBody
    public String runTest(@PathVariable String testId) {
        if (tests == null) {
            tests = context.getBeansOfType(TestRunnable.class);
        }

        HTMLWriter writer = new HTMLWriter();
        writer.startPage();
        writer.title("Execute test for : "+testId);
        TestRunnable r = tests.get(testId);
        if (r == null) {
            writer.writeln("Can`t find test, check testId");
        } else {
            try {
                writer.writeln("Start test for class : "+r.getClass().getSimpleName()+", name : "+testId);
                String result = r.test();
                writer.appendHtml(result);
            } catch (Exception ex) {
                writer.writeln("Fail to  run test : "+r.getClass().getSimpleName()).writeln("error : "+ex.toString());
            }
        }

        writer.endPage();
        return writer.toString();
    }*/
}
