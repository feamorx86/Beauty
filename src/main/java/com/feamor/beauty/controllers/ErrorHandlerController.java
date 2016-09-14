package com.feamor.beauty.controllers;

import com.feamor.beauty.tests.HTMLWriter;
import org.springframework.boot.autoconfigure.web.AbstractErrorController;
import org.springframework.boot.autoconfigure.web.BasicErrorController;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;

import javax.net.ssl.HttpsURLConnection;
import javax.persistence.CollectionTable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * Created by Home on 22.06.2016.
 */

@Controller
public class ErrorHandlerController extends AbstractErrorController {

    private ErrorAttributes errorAttributes;

    public ErrorHandlerController(ErrorAttributes errorAttributes, ErrorProperties errorProperties) {
        super(errorAttributes);
        this.errorAttributes = errorAttributes;
    }

    @RequestMapping("/error")
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response) {
        RequestAttributes requestAttributes = new ServletRequestAttributes(request);
        Throwable error = errorAttributes.getError(requestAttributes);
        HttpStatus status = getStatus(request);
        response.setStatus(status.value());
        HTMLWriter writer = new HTMLWriter();
        writer.startPage()
                .title("Error")
                .refline("to start page", "/");
        if (error != null) {
            writer.writeln("Occour error : " + error.getClass().getCanonicalName())
                    .writeMultiline(error.toString())
                    .writeln("Trace : ");
            Object[] trace = error.getStackTrace();
            if (trace != null) {
                for (Object t : trace) {
                    writer.writeMultiline(t.toString());
                }
            }
        }
        if (status != HttpStatus.OK) {
            writer.writeln("Fail to get page ("+status.value()+") : "+status.getReasonPhrase());
        }
        writer.endPage();
        String page = writer.toString();
        try {
            response.getOutputStream().print(page);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}
