package com.feamor.beauty.dao;

import com.feamor.beauty.managers.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Home on 26.05.2016.
 */

@Component
public class Texts {
    private HashMap<String, TextsSet> sets = new HashMap<String, TextsSet>();
    private TextsSet defaultSet;

    @Autowired
    SiteDao siteDao;

    @PostConstruct
    public void loadDefaultLang() {
        List<String> locales = siteDao.getTextsOfType(Constants.GroupDataType.LOCALIZATION);
        if (locales != null) {
            for (String locale : locales) {
                addLanguage(locale);
            }
        }
    }

    public String getDefault(String object, String name) {
        String result = defaultSet.get(object, name);
        return result;
    }

    public TextsSet getDefault() {
        return defaultSet;
    }

    public String get(String lang, String object, String name) {
        String result;
        TextsSet langSet = sets.get(lang);
        if (langSet != null) {
            result = langSet.get(object, name);
            if (result == null) {
                result = defaultSet.get(object, name);
            }
        } else {
            result = defaultSet.get(object, name);
        }
        return result;
    }

    public TextsSet getLang(String name) {
        TextsSet result = sets.get(name);
        if (result == null) {
            result = defaultSet;
        }
        return result;
    }

    public TextObject getObject(String lang, String name) {
        TextObject result;
        TextsSet langObject = getLang(lang);
        if (langObject != null) {
            result = langObject.getObject(name);
            if (result == null && langObject != defaultSet) {
                result = defaultSet.getObject(name);
            }
        } else {
            result = null;
        }
        return result;
    }

    public String get(TextsSet langSet, String object, String name) {
        String result = langSet.get(object, name);
        if (result == null && langSet != defaultSet) {
            result = defaultSet.get(object, name);
        }
        return result;
    }

    public String getObject(TextsSet lang, TextObject object, String name) {
        String result = null;
        if (object.getLang() == lang) {
            result = object.get(name);
            if (result == null) {
                result = defaultSet.get(object.getName(), name);
            }
        } else {
            result = lang.get(object.getName(), name);
        }
        return result;
    }

    public void addLanguage(TextsSet lang) {
        sets.put(lang.getLanguage(), lang);
    }

    public void addLanguage(String json) {
        JsonParser parser = JsonParserFactory.getJsonParser();
        Map<String, Object> data = parser.parseMap(json);
        String lang = (String) data.get("lang");
        Map<String, Object> objects = (Map<String, Object>) data.get("objects");

        HashMap<String, TextObject> resultObjects = new HashMap<String, TextObject>();
        TextsSet textsSet = new TextsSet(lang, resultObjects);

        for (Map.Entry<String, Object> object : objects.entrySet()) {
            String objectName = object.getKey();
            HashMap<String, String> resultLabels = null;
            if (object.getValue() instanceof Map) {
                resultLabels = new HashMap<String, String>();
                for (Map.Entry<String, Object> label : ((Map<String, Object>) object.getValue()).entrySet()) {
                    resultLabels.put(label.getKey(), label.getValue().toString());
                }
            }
            TextObject textObject = new TextObject(objectName, textsSet, resultLabels);
            resultObjects.put(objectName, textObject);
        }

        if ("default".equalsIgnoreCase(lang)) {
            if (defaultSet != null)
                throw new IllegalArgumentException("Can`t change default text set!");
            defaultSet = textsSet;
        } else {
            sets.put(lang, textsSet);
        }

    }

    public static class TextObject {
        private String name;
        private TextsSet lang;
        private Map<String, String> labels;

        public TextObject(String name, TextsSet lang, Map<String, String> labels) {
            this.name = name;
            this.lang = lang;
            this.labels = labels;
        }

        public void put(String name, String value) {
            if (labels == null) {
                labels = new HashMap<String, String>();
            }
            labels.put(name, value);
        }

        @Override
        public String toString() {
            return "TextObject{" +
                    "name='" + name + '\'' +
                    ", lang=" + lang +
                    ", labels=" + labels +
                    '}';
        }

        public String get(String label) {
            String result;
            if (labels != null) {
                result = labels.get(label);
            } else {
                result = null;
            }
            return result;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setLang(TextsSet lang) {
            this.lang = lang;
        }

        public boolean isValue() {
            return labels == null;
        }

        public String getName() {
            return name;
        }

        public TextsSet getLang() {
            return lang;
        }
    }

    public static class TextsSet {
        private String language;
        private Map<String, TextObject> objects;

        public TextsSet(String language, Map<String, TextObject> objects) {
            this.language = language;
            this.objects = objects;
        }

        public void put(TextObject object) {
            if (objects == null) {
                objects = new HashMap<String, TextObject>();
            }
            objects.put(object.getName(), object);
        }

        public void setLanguage(String language) {
            this.language = language;
        }

        public String getLanguage() {
            return language;
        }

        public TextObject getObject(String name) {
            TextObject object = objects.get(name);
            return object;
        }

        public String getTopLabel(String name) {
            String result;
            TextObject object = objects.get(name);
            if (object != null && object.isValue()) {
                result = object.getName();
            } else {
                result = null;
            }
            return result;
        }

        public String get(String object, String name) {
            TextObject objectsMap = objects.get(object);
            if (objectsMap != null) {
                String result = objectsMap.get(name);
                return (String) result;
            }
            return null;
        }
    }
}
