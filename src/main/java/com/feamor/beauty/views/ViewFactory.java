package com.feamor.beauty.views;

import com.feamor.beauty.controllers.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Home on 06.09.2016.
 */
@Component
public class ViewFactory {

    public static class RenderFormats {
        public static final int UNKNOWN = 0;
        public static final int FORMAT_HTML = 10;
        public static final int FORMAT_JSON = 20;
        public static final int DEFAULT = FORMAT_HTML;

        public static final String ContentType_JSON = "application/json; charset=utf-8";

        public static int forString(String formatString) {
            String format;
            int result = UNKNOWN;
            if (!StringUtils.isEmpty(formatString)) {
                format = formatString.toLowerCase();
                switch(format) {
                    case "html":
                    case "htm":
                        result = FORMAT_HTML;
                    break;
                    case "json":
                        result = FORMAT_JSON;
                        break;
                    default:
                        result = UNKNOWN;
                }
            }
            return result;
        }

        public static int forStingOrDefault(String format) {
            int result = forString(format);
            if (result == UNKNOWN) {
                result = DEFAULT;
            }
            return result;
        }

        public static String toString(int format) {
            String result;
            switch (format) {
                case FORMAT_HTML:
                    result = "html";
                    break;
                case FORMAT_JSON:
                    result = "json";
                    break;
                default:
                    result = "unknown("+format+")";
                    break;
            }
            return result;
        }
    }

    @Autowired
    private WebApplicationContext context;
    private HashMap<BaseController, HashMap<Integer, BaseViewRender>> renders = new HashMap<>();

    public void register(BaseController controller, int format, BaseViewRender render) {
        HashMap<Integer, BaseViewRender> renderHashMap = renders.get(controller);
        if (renderHashMap == null) {
            renderHashMap = new HashMap<>();
            renders.put(controller, renderHashMap);
        }
        if (renderHashMap.containsKey(format))
            throw new IllegalArgumentException("ViewFactory can't register two Renders for one Format, fromat : "+RenderFormats.toString(format)+", Renders : "+renderHashMap);
        renderHashMap.put(format, render);
    }

    public BaseViewRender getRender(BaseController controller, int format) {
        BaseViewRender result = null;
        HashMap<Integer, BaseViewRender> renderHashMap = renders.get(controller);
        if (renderHashMap != null) {
            result = renderHashMap.get(format);
        }
        return result;
    }

    @PostConstruct
    public void initialize() throws NoSuchMethodException {
        Map<String, BaseViewRender> renders = context.getBeansOfType(BaseViewRender.class);
        for (Map.Entry<String, BaseViewRender> entry: renders.entrySet()) {
            int format = entry.getValue().getFormat();
            String [] beans = entry.getValue().getSupportedBeans();
            if (beans != null && beans.length > 0) {
                for (String bean : beans) {
                    Object controller = context.getBean(bean);
                    if (controller != null && controller instanceof BaseController) {
                        register((BaseController) controller, format, entry.getValue());
                    } else {
                        //TODO: add logs
                    }
                }
            }
            Class cls = entry.getValue().getSupportedClass();
            if (cls != null) {
                Object controller = context.getBean(cls);
                if (controller != null && controller instanceof BaseController) {
                    register((BaseController) controller, format, entry.getValue());
                } else {
                    //TODO: add logs
                }
            }
        }
    }
}
