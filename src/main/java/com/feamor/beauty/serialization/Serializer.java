package com.feamor.beauty.serialization;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.feamor.beauty.models.ui.PageDom;

import java.io.IOException;

/**
 * Created by Home on 19.10.2016.
 */
public class Serializer {
    public static class DataTypes {
        public static final int Binary = 9;
        public static final int DataBase = 10;
        public static final int JSON = 11;
        public static final int STRING = 12;
        public static final int JSON_STRING = 13;
        public static final int HTML = 14;
        public static final int DOM = 15;
    }

    public Object loadFrom(int dataType, Object data, Object... args) {
        Object result;
        switch (dataType) {
            case DataTypes.DataBase:
                result = loadFromDB(data, args);
                break;
            case DataTypes.JSON:
                result = loadFromJson((JsonNode) data, args);
                break;
            case DataTypes.STRING:
                result = loadFromString((String) data, args);
                break;
            case DataTypes.JSON_STRING:
                result = loadFromJsonString((String) data, args);
                break;
            case DataTypes.Binary:
                result = loadFromBinary(data, args);
                break;
            case DataTypes.HTML:
                result = loadFromHtml(data, args);
                break;
            case DataTypes.DOM:
                result = loadFromDom(data, args);
                break;
            default:
                result = null;
                break;
        }
        return result;
    }

    protected Object loadFromDom(Object domData, Object... args) {
        return null;
    }

    public Object saveTo(int dataType, Object value, Object ... args) {
        Object result;
        switch(dataType) {
            case DataTypes.JSON:
                result = saveToJson(value);
            break;
            case DataTypes.DataBase:
                saveToDb(value, args);
                result = null;
            break;
            case DataTypes.JSON_STRING:
                result = saveToJsonString(value, args);
                break;
            case DataTypes.STRING:
                result = saveToString(value, args);
                break;
            case DataTypes.Binary:
                saveToBinary(value, args);
                result = null;
                break;
            case DataTypes.HTML:
                result = saveToHtml(value, args);
                break;
            case DataTypes.DOM:
                saveToDom(value, args);
                result = null;
                break;
            default:
                result = null;
            break;
        }
        return result;
    }

    protected Object saveToHtml(Object value, Object...args) {
        return null;
    }


    protected void saveToDom(Object value, Object...args) {

    }

    protected void saveToBinary(Object value, Object ... args) {

    }


    protected JsonNode saveToJson(Object value, Object... args) {
        return null;
    }

    protected String saveToString(Object value, Object... args) {
        return null;
    }

    protected void saveToDb(Object value, Object ...args) {

    }

    protected String saveToJsonString(Object value, Object... args) {
        JsonNode node = saveToJson(value);
        String result;
        if (node != null) {
            result = node.toString();
        } else {
            result = null;
        }
        return result;
    }

    protected Object loadFromHtml(Object data, Object... args) {
        return null;
    }

    protected Object loadFromBinary(Object data, Object... args) {
        return null;
    }

    protected Object loadFromDB(Object data, Object... args) {
        return null;
    }

    protected Object loadFromJson(JsonNode json, Object... args) {
        return null;
    }

    protected Object loadFromString(String str, Object... args) {
        return null;
    }

    protected Object loadFromJsonString(String str, Object... args) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = null;
        try {
            json = mapper.readTree(str);
        } catch (IOException e) {
            json = null;
            e.printStackTrace();
        }
        return loadFromJson(json);
    }

    protected int optInt(ObjectNode node, String field, int fallback) {
        JsonNode jn = node.get(field);
        if (jn != null && jn.isInt()) {
            return jn.intValue();
        } else {
            return fallback;
        }
    }

    protected String optStr(ObjectNode node, String field, String fallback) {
        JsonNode jn = node.get(field);
        if (jn != null && jn.isTextual()) {
            return jn.textValue();
        } else {
            return fallback;
        }
    }

    protected ObjectNode optObj(ObjectNode node, String field) {
        JsonNode jn = node.get(field);
        if (jn != null && jn.isObject()) {
            return (ObjectNode) jn;
        } else {
            return null;
        }
    }

}
