package com.feamor.beauty.tests;

import org.springframework.util.StringUtils;
import org.springframework.web.util.HtmlUtils;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by Home on 09.05.2016.
 */
public class HTMLWriter {
    private StringBuilder builder = new StringBuilder();

    public HTMLWriter fromFile(String path) {
        File file = new File(path);
        if (file.exists()) {
            try {
                FileInputStream stream = new FileInputStream(file);
                InputStreamReader reader = new InputStreamReader(stream);
                char buffer[] = new char[2048];
                int read;
                try {
                    while ((read = reader.read(buffer)) > 0) {
                        builder.append(buffer, 0, read);
                    }
                } catch (IOException ioex) {
                    ioex.printStackTrace();
                } finally {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        stream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
        }
        return this;
    }

    public HTMLWriter start() {
        builder.setLength(0);
        tagStack.clear();
        return this;
    }

    public HTMLWriter startPage() {
        builder.append("<html><head><meta charset=\"utf-8\"/></head><body>");
        return this;
    }

    public HTMLWriter endPage() {
        builder.append("</body></html>");
        return this;
    }

    public HTMLWriter title(String title) {
        String escapedTitle = HtmlUtils.htmlEscape(title);
        builder.append("<h1>").append(escapedTitle).append("</h1>");
        return this;
    }

    public HTMLWriter ln() {
        builder.append("<br>");
        return this;
    }

    public HTMLWriter writeln(String text) {
        String escapedText = HtmlUtils.htmlEscape(text);
        builder.append("<p>").append(escapedText).append("</p>");
        return this;
    }

    public HTMLWriter writeMultiline(String text) {
        String[] lines =text.split("\\r?\\n");
        for (String line : lines) {
            builder.append("<p>").append(HtmlUtils.htmlEscape(line)).append("</p>");
        }
        return this;
    }

    public HTMLWriter appendHtml(String htmlCode) {
        builder.append(htmlCode);
        return this;
    }

    public StringBuilder getBuilder() {
        return  builder;
    }

    public HTMLWriter ref(String text, String ref) {
        String escapedText = HtmlUtils.htmlEscape(text);
        builder.append("<a href=\""+ref+"\">").append(escapedText).append("</a>");
        return this;
    }

    public HTMLWriter refline(String text, String ref) {
        String escapedText = HtmlUtils.htmlEscape(text);
        builder.append("<a href=\""+ref+"\"><p>").append(escapedText).append("</p></a>");
        return this;
    }

    public HTMLWriter startTable(String ... headers) {
        builder.append("<table border=\"1\">");
        if (headers!=null) {
            builder.append("<tr>");
            for(String th : headers) {
               builder.append("<th>").append(HtmlUtils.htmlEscape(th)).append("</th>");
            }
            builder.append("</tr>");
        }
        return this;
    }

    public HTMLWriter tableLine(String ... columns) {
        builder.append("<tr>");
        if (columns!=null) {
            for(String th : columns) {
                builder.append("<td>").append(th).append("</td>");
            }
        }
        builder.append("</tr>");
        return this;
    }

    public HTMLWriter endTable() {
        builder.append("</table>");
        return this;
    }

    public HTMLWriter tag(String tag, String text){
        builder.append("<").append(tag).append(">").append(HtmlUtils.htmlEscape(text)).append("</").append(tag).append(">");
        return this;
    }

    public HTMLWriter tagNoEscape(String tag, String noEscapeText){
        builder.append("<").append(tag).append(">").append(noEscapeText).append("</").append(tag).append(">");
        return this;
    }

    public HTMLWriter startForm(String action, String method) {
        builder.append("<form action=\"").append(action).append("\" ");
        if (StringUtils.isEmpty(method)) {
            method = "GET";
        }
        builder.append(" method=\"").append(method).append("\">");
        return this;
    }

    public HTMLWriter endForm() {
        builder.append("</form>");
        return this;
    }

    public HTMLWriter button(String name, String value) {
        return input("button", name, value, null, null);
    }

    public HTMLWriter edit(String name, String value) {
        return input("text", name, value, null, null);
    }

    public HTMLWriter input(String type, String name, String value, String attributes, String rawText) {
        builder.append("<input ");
        if (!StringUtils.isEmpty(type)) {
            builder.append("type=\"").append(type).append("\" ");
        }
        if (!StringUtils.isEmpty(name)) {
            builder.append("name=\"").append(name).append("\" ");
        }
        if (!StringUtils.isEmpty(value)) {
            builder.append("value=\"").append(value).append("\" ");
        }
        if (!StringUtils.isEmpty(attributes)) {
            builder.append(attributes).append(" ");
        }
        if (!StringUtils.isEmpty(rawText)) {
            builder.append(">").append(rawText).append("</input>");
        } else{
            builder.append("/>");
        }
        return this;
    }

    public HTMLWriter submit(String title) {
        builder.append("<input type=\"submit\" value=\"").append(title).append("\" />");
        return this;
    }

    private ArrayList<String> tagStack = new ArrayList<>();

    public HTMLWriter push(String tag) {
        builder.append("<").append(tag).append(">");
        tagStack.add(tag);
        return this;
    }

    public HTMLWriter push(String tag, String rawText) {
        push(tag);
        builder.append(rawText);
        return this;
    }

    public HTMLWriter pop() {
        if (tagStack.size() > 0) {
            builder.append("</").append(tagStack.remove(tagStack.size()-1)).append(">");
        }
        return this;
    }

    public HTMLWriter pop(String rawText) {
        builder.append(rawText);
        return pop();
    }

    public HTMLWriter select(Integer size, String name, String[] optionValues, String[] optionNames, int selectedId) {
        builder.append("<select ");
        if (size != null) {
            builder.append("size=\"").append(size).append("\" ");
        }
        if (!StringUtils.isEmpty(name)) {
            builder.append("name=\"").append(name).append("\" ");
        }
        builder.append(">");
        if (optionValues != null && optionNames != null && optionValues.length == optionNames.length) {
            for(int i = 0; i<optionValues.length; i++) {
                if (selectedId == i) {
                    builder.append("<option selected value=\"").append(optionValues[i]).append("\">").append(optionNames[i]).append("</option>");
                } else {
                    builder.append("<option value=\"").append(optionValues[i]).append("\">").append(optionNames[i]).append("</option>");
                }
            }
        } else {
            builder.append("<p>Fail to add items. Values or Names is null or different length.</p>");
        }
        builder.append("</select>");
        return  this;
    }

    @Override
    public String toString() {
        return builder.toString();
    }
}