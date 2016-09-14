package com.feamor.beauty.templates;

/**
 * Created by Home on 06.05.2016.
 */
public class TemplateElement {
    public static class Types {
        public static final byte TEXT = 0;
        public static final byte BLOCK_START = 10;
        public static final byte BLOCK_END = 21;
        public static final byte TAG = 20;
        public static final byte DOCUMENT_END = (byte)255;
        public static final byte DOCUMENT_ERROR = (byte)254;

        public static String toString(int type) {
            String result;
            switch (type) {
                case BLOCK_START: result = "block start type";break;
                case BLOCK_END: result = "block end type";break;
                case TAG: result = "identification type";break;
                case TEXT: result = "text type";break;
                case DOCUMENT_END: result = "eof type";break;
                case DOCUMENT_ERROR: result = "error type";break;
                default: result = "unknown type";break;
            }
            return  result;
        }
    }

    private int type;
    private String text;

    public TemplateElement(int type) {
        this.type = type;
        text = null;
    }

    public TemplateElement(int type, String text) {
        this.type = type;
        this.text = text;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public static void printTo(StringBuilder builder, int type, String text) {
        switch(type) {
            case Types.TEXT: builder.append(text); break;
            case Types.TAG:
                builder.append("{$").append(text).append('}');
        }
    }

    public void printTo(StringBuilder builder) {
        printTo(builder, type, text);
    }

    public void setText(String text) {
        this.text = text;
    }
}
