package com.feamor.beauty.views;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Home on 01.10.2016.
 */
public class SimpleJsonView extends BaseViewRender {
    @Override
    public int getFormat() {
        return ViewFactory.RenderFormats.FORMAT_JSON;
    }

    protected void sendProblem(HttpServletResponse response, int errorCode, String data, String message) throws IOException {
        ObjectNode json = withStatus("error")
                .put("error_code", errorCode);
        if (!StringUtils.isEmpty(data)) {
            json.put("error_data", data.toString());
        }
        if (!StringUtils.isEmpty(message)) {
            json.put("error_message", message);
        }
        sendJson(response, json);
    }

    protected ObjectNode withStatus(String status) {
        ObjectNode json = JsonNodeFactory.instance.objectNode();
        json.put("status", status);
        return json;
    }

    protected void sendJson(HttpServletResponse response, ObjectNode json) throws IOException {
        response.setContentType(ViewFactory.RenderFormats.ContentType_JSON);
        response.getOutputStream().print(json.toString());
    }
}
