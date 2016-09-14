package com.feamor.beauty.controllers;

import com.feamor.beauty.managers.Constants;
import com.feamor.beauty.models.db.Page;
import com.feamor.beauty.models.db.User;
import com.feamor.beauty.tests.HTMLWriter;
import org.springframework.http.HttpMethod;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Home on 22.06.2016.
 */
public class ShareViewController extends BaseController {

    @Override
    public int controllerId() {
        return Constants.Controllers.SHARE_VIEW;
    }

    private String printError(String message) {
        HTMLWriter writer = new HTMLWriter();
        writer.startPage()
                .title("Simple share viewer")
                .tag("h2","Error")
                .ref("go top", "/share")
                .writeln(message)
                .endPage();
        return writer.toString();
    }

    private String printFiles(String dir, ArrayList<String> names, ArrayList<String> urls) {
        HTMLWriter writer = new HTMLWriter();
        writer.startPage()
                .title("Simple share viewer")
                .tag("h2", "list files for directory")
                .writeln(dir)
                //.startForm("upload", "POST")
                .appendHtml("<form action=\""+dir+"\" method=\"post\" enctype=\"multipart/form-data\">")
                .writeln("Select file to upload (max 100 mb)")
                .input("file", "file", null, null, null)
                .submit("Upload")
                .endForm()
                .appendHtml("<hr>");

        for(int i=0; i<names.size(); i++) {
            String name = names.get(i);
            String url = urls.get(i);
            writer.refline(name, url);
        }

        writer.appendHtml("<hr>").endPage();
        return writer.toString();
    }

    private String oneFolderTop(String path) {
        String result = "";
        if (!StringUtils.isEmpty(path)) {
            int pos = path.lastIndexOf("/");
            if (pos > 0) {
                result = path.substring(0, pos);
                if (result.equalsIgnoreCase("/")) {
                    result = "";
                }
            }
        }
        return  result;
    }

    @Override
    public ModelAndView render(HttpServletRequest request, HttpServletResponse response, User user) throws IOException, NoSuchMethodException {
        if (HttpMethod.GET.matches(request.getMethod())) {
            String requested = URLDecoder.decode(request.getRequestURI());
            if (requested.charAt(requested.length() - 1) == '/') {
                requested = requested.substring(0, requested.length() - 1);
            }

            File file = new File("./src/web/" + requested);
            if (file.exists()) {
                if (file.isDirectory()) {
                    ArrayList<String> names = new ArrayList<>();
                    ArrayList<String> urls = new ArrayList<>();

                    String top = oneFolderTop(requested);
                    if (!StringUtils.isEmpty(top)) {
                        names.add("..");
                        urls.add(top);
                    }

                    for (String fileName : file.list()) {
                        names.add(fileName);
                        urls.add(requested + "/" + fileName);
                    }

                    String result = printFiles(requested, names, urls);
                    response.getOutputStream().print(result);
                } else {
                    try {
                        request.getRequestDispatcher("/web/" + requested).forward(request, response);
                    } catch (ServletException e) {
                        e.printStackTrace();
                        String result = printError("Fail to open file : " + requested + ", error : " + e.toString());
                        response.getOutputStream().print(result);
                    }
                }
            } else {
                String result = printError("There is no such file : " + requested);
                response.getOutputStream().print(result);
            }
        } else if (HttpMethod.POST.matches(request.getMethod())) {
            try {
                MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
                Iterator<String> fileNames = multipartRequest.getFileNames();
                while (fileNames.hasNext()) {
                    String name = fileNames.next();
                    MultipartFile file = multipartRequest.getFile(name);
                    if (file != null && !file.isEmpty()) {
                        File saveTo = new File("./src/web/share/uploads/" + file.getOriginalFilename());
                        if (saveTo.exists()) {
                            saveTo.delete();
                        }
                        BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(saveTo, false));
                        FileCopyUtils.copy(file.getInputStream(), stream);
                        stream.close();
                    }
                }
                HTMLWriter writer = new HTMLWriter();
                writer.startPage()
                        .title("Simple share viewer")
                        .tag("h2", "Files saved to")
                        .refline("uploads", "/share/uploads")
                        .endPage();
                String result = writer.toString();
                response.getOutputStream().print(result);
            } catch (Exception ex) {
                String result = printError("Fail to upload file, error : "+ex.toString());
                response.getOutputStream().print(result);
            }

        }
        return  null;
    }
}
