package com.feamor.beauty.tests;

import com.feamor.beauty.templates.TemplateElement;
import com.feamor.beauty.templates.TemplateScanner;
import org.springframework.web.util.HtmlUtils;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Home on 09.05.2016.
 */
public class TemplateScannerTest implements TestRunnable {

    private HTMLWriter textWriter = new HTMLWriter();

    @Override
    public String test() {
        textWriter
                .tag("h2","Template Scanner test")
                .tag("h3","Simple correct text")
                .writeln("result :");
        try {
            String result = testSimpleCorrect("/tests/TestTemplateScanner/simple-correct.htm");
            textWriter.writeln("Complete");
            textWriter.appendHtml(result);
        } catch (Exception e) {
            e.printStackTrace();
            textWriter.writeln("Fail, "+e.toString());
        }

        textWriter.tag("h3","Template Build Page")
        .writeln("result :");
        try {
            String result = testSimpleCorrectBuildPage("/tests/TestTemplateScanner/simple-correct.htm");
            textWriter.writeln("Complete");
            textWriter.appendHtml(result);
        } catch (Exception e) {
            e.printStackTrace();
            textWriter.writeln("Fail, "+e.toString());
        }

        String result = textWriter.toString();
        return  result;
    }

    public String testSimpleCorrectBuildPage(String filePath) {
        HTMLWriter writer = new HTMLWriter();
        writer.writeln("Test file : "+filePath);
        ArrayList<TemplateElement> elements = new ArrayList<TemplateElement>();
        InputStream stream = TemplateScanner.class.getResourceAsStream(filePath);
        TemplateScanner scanner = new TemplateScanner(stream);
        TemplateElement element = null;
        writer.writeln("parse page");
        boolean hasError = false;
        do {
            try {
                element = scanner.next();
                elements.add(element);
            }catch (Exception ex) {
                hasError = true;
                ex.printStackTrace();
                writer.writeln("error : "+ex.toString());
                break;
            }
        } while (element!= null && element.getType() != TemplateElement.Types.DOCUMENT_END && element.getType() != TemplateElement.Types.DOCUMENT_ERROR);
        scanner.close();

        if (!hasError) {
            writer.writeln("Parsed. Found "+elements.size()+" elements");
            writer.writeln("build page");
            writer.getBuilder().append("<p>");
            for (TemplateElement e : elements) {
                if (e.getType() == TemplateElement.Types.TEXT) {
                    String result = e.getText().replace("html","th-is-htm").replace("body", "it-is-bdy");
                    writer.getBuilder().append(HtmlUtils.htmlEscape(result));
                } else if (e.getType() == TemplateElement.Types.TAG) {
                    writer.getBuilder().append("var-"+e.getText());
                }
            }
            writer.getBuilder().append("</p>");
        }
        return writer.toString();
    }

    public String testSimpleCorrect(String filePath) throws Exception {
        HTMLWriter writer = new HTMLWriter();
        writer.writeln("Test file : "+filePath);

        InputStream stream = TemplateScanner.class.getResourceAsStream(filePath);
        TemplateScanner scanner = new TemplateScanner(stream);

        TemplateElement element = null;
        writer.writeln("Log tags");

        boolean hasError = false;
        do
        {
            try {
                element = scanner.next();
                writer.writeln(TemplateElement.Types.toString(element.getType()) + ": " + element.getText());
            }catch (Exception ex) {
                ex.printStackTrace();
                writer.writeln("error : "+ex.toString());
                break;
            }
        } while (element!= null && element.getType() != TemplateElement.Types.DOCUMENT_END && element.getType() != TemplateElement.Types.DOCUMENT_ERROR);
        scanner.close();

        return writer.toString();
    }
}