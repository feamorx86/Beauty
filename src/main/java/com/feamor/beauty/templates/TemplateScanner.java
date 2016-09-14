package com.feamor.beauty.templates;

import java.io.*;

/**
 * Created by Home on 06.05.2016.
 */
public class TemplateScanner {

    public static final int MAX_SIZE = 1024 * 4;

    private InputStreamReader reader;
    private char [] buffer = new char[MAX_SIZE];
    private int bufferPosition = MAX_SIZE;
    private int bufferDataSize = 0;

    public TemplateScanner(InputStream stream) {
        this.reader = new InputStreamReader(stream);
    }

    public void close() {
        try {
            if (reader != null) {
                reader.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            reader = null;
        }
    }

    private TemplateElement error(String message) {
        TemplateElement result= new TemplateElement(TemplateElement.Types.DOCUMENT_ERROR, message);
        return result;
    }

    private boolean getNextChar() {
        boolean has = true;
        boolean found = false;
        do {
            if (bufferPosition >= bufferDataSize) {
                try {
                    bufferDataSize = reader.read(buffer);
                    if (bufferDataSize < 0) {
                        has = false;
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    has = false;
                }
                bufferPosition = 0;
            }
            if (has) {
                currentCharacter = buffer[bufferPosition];
                //if (currentCharacter == '\r' || currentCharacter == '\n' || currentCharacter == '\t') {
                //    bufferPosition++;
                //} else {
                    found = true;
                //}
            } else {
                found = true;
            }
        } while(!found);
        return has;
    }

    private StringBuilder builder = new StringBuilder(512);//TODO: calculate middle length of content item
    private int state = States.START;
    private char currentCharacter = '\u0000';

    private static class States {
        public static final int START = 0;
        public static final int READ_TEXT = 100;
        public static final int READ_BLOCK_START = 20;
        public static final int READ_BLOCK_END = 50;
        public static final int READ_IDENTIFICATOR = 30;
    }

    public TemplateElement next() {
        if (reader == null) {
            return  null;
        }
        TemplateElement result = null;
        do {
            boolean eof = !getNextChar();
            switch (state) {
                case 0: //undefined, read next byte
                    if (eof) {
                        result = new TemplateElement(TemplateElement.Types.DOCUMENT_END);
                        builder.setLength(0);
                    } else {
                        builder.setLength(0);
                        builder.append(currentCharacter);
                        bufferPosition++;
                        if (currentCharacter == '{') {
                            eof = !getNextChar();
                            if (eof) {
                                result = new TemplateElement(TemplateElement.Types.TEXT, builder.toString());
                                builder.setLength(0);
                            } else if (currentCharacter == '/') {
                                builder.append(currentCharacter);
                                bufferPosition++;
                                eof = !getNextChar();
                                if (eof) {
                                    result = new TemplateElement(TemplateElement.Types.TEXT, builder.toString());
                                    builder.setLength(0);
                                } else {
                                    if (currentCharacter == '#') {
                                        builder.setLength(0);
                                        bufferPosition++;
                                        state = States.READ_BLOCK_END;
                                    } else {
                                        state = States.READ_TEXT;
                                        builder.append(currentCharacter);
                                        bufferPosition++;
                                    }
                                }
                            } else if (currentCharacter == '#') {
                                builder.setLength(0);
                                bufferPosition++;
                                state = States.READ_BLOCK_START;
                            } else if (currentCharacter == '$') {
                                builder.setLength(0);
                                bufferPosition++;
                                state = States.READ_IDENTIFICATOR;
                            } else {
                                state = States.READ_TEXT;
                                builder.append(currentCharacter);
                                bufferPosition++;
                            }
                        } else {
                            state = States.READ_TEXT;
                        }
                    }
                    break;
                case States.READ_TEXT:
                    if (eof) {
                        result = new TemplateElement(TemplateElement.Types.TEXT, builder.toString());
                        builder.setLength(0);
                        state = States.START;
                    } else if (currentCharacter == '{') {
                        char char1 = currentCharacter;
                        bufferPosition++;
                        eof = !getNextChar();
                        if (eof) {
                            builder.append(char1);
                            bufferPosition++;
                            result = new TemplateElement(TemplateElement.Types.TEXT, builder.toString());
                            state = States.START;
                            builder.setLength(0);
                        } else if (currentCharacter == '#') {
                            result = new TemplateElement(TemplateElement.Types.TEXT, builder.toString());
                            builder.setLength(0);
                            bufferPosition++;
                            state = States.READ_BLOCK_START;
                        } else if (currentCharacter == '$') {
                            result = new TemplateElement(TemplateElement.Types.TEXT, builder.toString());
                            builder.setLength(0);
                            bufferPosition++;
                            state = States.READ_IDENTIFICATOR;
                        } else if (currentCharacter == '/') {
                            char char2 = currentCharacter;
                            bufferPosition++;
                            eof = !getNextChar();
                            if (eof) {
                                builder.append(char1);
                                builder.append(char2);
                                result = new TemplateElement(TemplateElement.Types.TEXT, builder.toString());
                                builder.setLength(0);
                                bufferPosition++;
                                state = States.START;
                            } else if (currentCharacter == '#') {
                                result = new TemplateElement(TemplateElement.Types.TEXT, builder.substring(0, builder.length()));
                                builder.setLength(0);
                                bufferPosition++;
                                state = States.READ_BLOCK_END;
                            } else {
                                builder.append(char1);
                                builder.append(char2);
                                builder.append(currentCharacter);
                                bufferPosition++;
                            }
                        } else {
                            builder.append(char1);
                            builder.append(currentCharacter);
                            bufferPosition++;
                        }
                    } else {
                        builder.append(currentCharacter);
                        bufferPosition++;
                    }
                    break;
                case States.READ_BLOCK_START:
                    if (eof) {
                        result = new TemplateElement(TemplateElement.Types.DOCUMENT_ERROR, "Not closed block start : " + builder.toString());
                        builder.setLength(0);
                        state = States.START;
                    } else {
                        if (currentCharacter == '}') {
                            result = new TemplateElement(TemplateElement.Types.BLOCK_START, builder.toString());
                            builder.setLength(0);
                            bufferPosition++;
                            state = States.START;
                        } else {
                            builder.append(currentCharacter);
                            bufferPosition++;
                        }
                    }
                    break;
                case States.READ_IDENTIFICATOR:
                    if (eof) {
                        result = new TemplateElement(TemplateElement.Types.DOCUMENT_ERROR, "Not closed identificator : " + builder.toString());
                        builder.setLength(0);
                        state = States.START;
                    } else {
                        if (currentCharacter == '}') {
                            result = new TemplateElement(TemplateElement.Types.TAG, builder.toString());
                            builder.setLength(0);
                            bufferPosition++;
                            state = States.START;
                        } else {
                            //continue read
                            builder.append(currentCharacter);
                            bufferPosition++;
                        }
                    }
                    break;
                case States.READ_BLOCK_END:
                    if (eof) {
                        result = new TemplateElement(TemplateElement.Types.DOCUMENT_ERROR, "Not closed block end : " + builder.toString());
                        builder.setLength(0);
                        state = States.START;
                    } else {
                        if (currentCharacter == '}') {
                            result = new TemplateElement(TemplateElement.Types.BLOCK_END, builder.toString());
                            builder.setLength(0);
                            bufferPosition++;
                            state = States.START;
                        } else {
                            //continue read
                            builder.append(currentCharacter);
                            bufferPosition++;
                        }
                    }
                    break;
            }
        } while (result == null) ;
        return result;
    }
}
