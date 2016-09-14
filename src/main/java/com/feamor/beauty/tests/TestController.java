package com.feamor.beauty.tests;

import com.feamor.beauty.dao.PageDao;
import com.feamor.beauty.dao.SiteDao;
import com.feamor.beauty.dao.UserDao;
import com.feamor.beauty.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;

/**
 * Created by Home on 13.02.2016.
 */
@Transactional
//@RestController
public class TestController {
/*
    @Autowired
    private PageDao pageDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private SiteDao siteDao;


//    @RequestMapping(value = "/", produces = "text/html;charset=utf-8")
//    @ResponseBody
    String home() {
        StringBuilder builder = new StringBuilder();

        builder.append("<html><meta charset=\"utf-8\"><body><h1>Ураааа!!!</h1>");
        builder.append("<a href=\"web/index.htm\"><h1>Самый ахуительный сайт!!!!!</h1></a>");

//        List<Page> pages = pageDao.listPages();
//        if (pages == null || pages.size() == 0) {
//            builder.append("no pages");
//        } else {
//
//            for(Page p : pages) {
//                PageDom dom = pageDao.loadPage(p.getPageId());
//                dom.print(builder);
//            }
//        }
        builder.append("</body><html>");
        return builder.toString();
    }

    private String printLoginPage(String login, String password, String error) {
        HTMLWriter writer = new HTMLWriter();
        writer
                .startPage()
                .title("Super mega site!!!")
                .tag("h2", "Login form");
        if (!StringUtils.isEmpty(error)) {
            writer.tag("h3","Error").writeln(error);
        }

        writer.appendHtml("<Form method=\"POST\" action=\"login\"/>")
                .writeln("Enter login")
                .appendHtml("<p><input type=\"text\" name=\"login\"" + (StringUtils.isEmpty(login) ? "" : "value = \""+login+"\"")+" /></p>")
                .writeln("Enter password")
                .appendHtml("<p><input type=\"text\" name=\"password\"" + (StringUtils.isEmpty(password) ? "" : "value = \""+password+"\"")+" /></p>")
                .appendHtml("<p><input type=\"submit\" name=\"Login\"/></p>")
                .appendHtml("</Form>")
                .endPage();
        return writer.toString();
    }

    //@RequestMapping(value = "/login", produces = "text/html;charset=utf-8")
    public void login(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if ("GET".equalsIgnoreCase(request.getMethod())) {
            response.getOutputStream().print(printLoginPage(null, null, null));
        } else if ("POST".equalsIgnoreCase(request.getMethod())) {
            String login = request.getParameter("login");
            String password = request.getParameter("password");
            if (StringUtils.isEmpty(login) || StringUtils.isEmpty(password)) {
                response.getOutputStream().print(printLoginPage(login, password, "Please enter login and password"));
            } else {
                User user = userDao.getUserForLoginAndPassword(login, password);
                if (user == null) {
                    response.getOutputStream().print(printLoginPage(login, password, "There is no user with such login and password. Check login and password and try again."));
                } else {
                    String sessionId = request.getSession(true).getId();
                    userDao.putActiveUserSession(sessionId, user.getId());
                    response.sendRedirect("/home");
                }
            }

        }
    }

    //@RequestMapping(value = "/home", produces = "text/html;charset=utf-8")
    public void home(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String sessionId = request.getRequestedSessionId();
        User user = null;
        if (!StringUtils.isEmpty(sessionId)) {
            user = userDao.getUserForSession(sessionId);
        }

        if (user != null){
            HTMLWriter writer = new HTMLWriter();
            writer.startPage();
            writer.title("Welcome back!");
            writer.tag("h2","Dear "+user.getFirstName()+" "+user.getLastName());
            writer.endPage();
            response.getOutputStream().print(writer.toString());
        } else {
            response.sendRedirect("/login");
        }
    }

//    @RequestMapping(value = "/test-async", produces = "text/html;charset=utf-8")
//    public ResponseBodyEmitter testAsynch() {
//        final ResponseBodyEmitter emitter = new ResponseBodyEmitter();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                emitter.send("");
//            }
//        }).start();
//
//        return emitter;
//    }



//    @RequestMapping(value = "/stream/page{pageId}", produces = "text/html;charset=utf-8")
    public StreamingResponseBody stream(@PathVariable final int pageId, @CookieValue String sessionId) {
        return new StreamingResponseBody() {

            @Override
            public void writeTo(OutputStream outputStream) throws IOException {
                Writer w = new OutputStreamWriter(outputStream);
                Page page = pageDao.getPage(pageId);
                List<PageBlock> pageBlocks = pageDao.getBlocksOfPage(page.getPageId());
                for (PageBlock block: pageBlocks) {

                }
                w.close();
            }
        };
    }

//    @RequestMapping(value = "/menu", produces = "text/html;charset=utf-8")
//    @ResponseBody
//    public String showMenu() {
//
//        List<SiteDao.MenuItemData> menu = siteDao.getGroupMenu(1,1);
//
//        HTMLWriter writer = new HTMLWriter();
//
//        writer.startPage();
//        writer.title("Menu");
//        for (String s : menu) {
//            writer.ref(s, "/menu");
//        }
//        writer.endPage();
//        return writer.toString();
//    }


//    @RequestMapping(value = "/templates/{id}", produces = "text/html;charset=utf-8")
//    @ResponseBody
    public String getTemplates(@PathVariable int id) {

        BlockTemplate template = pageDao.getTemplate(id);

        HTMLWriter writer = new HTMLWriter();

        writer.title("Block template #"+id);
        writer.writeln(template.toString());
        return writer.toString();
    }


*/

}