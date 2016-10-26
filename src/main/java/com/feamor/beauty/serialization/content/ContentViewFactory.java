package com.feamor.beauty.serialization.content;

import com.feamor.beauty.managers.Constants;
import com.feamor.beauty.serialization.Serializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;

/**
 * Created by Home on 25.10.2016.
 */
@Component
public class ContentViewFactory {
    @Autowired
    private ApplicationContext context;

    private HashMap<Integer, Serializer> contentSerializers = new HashMap<>();

    @PostConstruct
    public void initialize() {
        register(Constants.PageData.ContentView.TEXT_VIEW, context.getBean(TextViewSerializer.class));
        register(Constants.PageData.ContentView.IMAGE_VIEW, context.getBean(ImageViewSerializer.class));
        register(Constants.PageData.ContentView.TITLE_VIEW, context.getBean(TitleViewSerializer.class));
    }

    private void register(int id, Serializer serializer) {
        contentSerializers.put(id, serializer);
    }

    public Serializer getSerializer(int dbType) {
        Serializer serializer = contentSerializers.get(dbType);
        return serializer;
    }

}
