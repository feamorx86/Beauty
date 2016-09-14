package com.feamor.beauty.models.db;

import com.feamor.beauty.templates.TemplateElement;
import com.feamor.beauty.utils.DBIntegerArrayType;
import com.feamor.beauty.utils.DBStringArrayType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Home on 09.05.2016.
 */
@Entity(name = "blockTemplate")
@Table(name = "\"BlockTemplate\"")
@TypeDefs({ @TypeDef(name = "IntegerArrayObject", typeClass = DBIntegerArrayType.class), @TypeDef(name = "StringArrayObject", typeClass = DBStringArrayType.class) })
public class BlockTemplate {

    public static class TemplateHeader{
        public int id;
        public String alias;
        public String path;
        public int blockType;

        public TemplateHeader(int id, String alias, String path, int blockType) {
            this.id = id;
            this.alias = alias;
            this.path = path;
            this.blockType = blockType;
        }
    }

    public static BlockTemplate createFrom(Integer id, int blockType, String alias, String path, List<TemplateElement> elements) {
        BlockTemplate result = new BlockTemplate();
        if (id!=null) {
            result.setId(id.intValue());
        }
        result.setBlockType(blockType);
        result.setAlias(alias);
        result.setPath(path);

        if (elements != null) {
            String [] lines = new String[elements.size()];
            Integer[] types = new Integer[elements.size()];
            for(int i = 0; i<elements.size(); i++) {
                TemplateElement element = elements.get(i);
                lines[i] = element.getText();
                types[i] = element.getType();
            }
            result.setLines(lines);
            result.setTypes(types);
        }

        return  result;
    }

    @Id
    @Column(name = "\"id\"")
    @SequenceGenerator(name = "blockTemplateIdGenerator", sequenceName = "\"BlockTemplateSequence\"", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "blockTemplateIdGenerator")
    private int id;

    @Column(name = "\"blockType\"", nullable = false)
    private int blockType;

    @Column(name = "\"alias\"")
    private String alias;

    @Column(name = "\"path\"")
    private String path;

    @Column(name = "\"lines\"")
    @Type(type = "StringArrayObject")
    private String [] lines;

    @Column(name = "\"types\"")
    @Type(type = "IntegerArrayObject")
    private Integer [] types;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBlockType() {
        return blockType;
    }

    public void setBlockType(int blockType) {
        this.blockType = blockType;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String[] getLines() {
        return lines;
    }

    public void setLines(String[] lines) {
        this.lines = lines;
    }

    public Integer[] getTypes() {
        return types;
    }

    public void setTypes(Integer[] types) {
        this.types = types;
    }

    public void printTo(StringBuilder builder) {
        if (lines!=null && lines.length > 0) {
            if (lines.length != types.length) {
                throw new IllegalArgumentException("Incorrect template - lines.length != types.length. Template : "+toString());
            }
            for(int i=0; i<lines.length; i++) {
                int type = types[i];
                String text = lines[i];
                TemplateElement.printTo(builder, type, text);
            }
        }
    }

    @Override
    public String toString() {
        return "BlockTemplate{" +
                "id=" + id +
                ", blockType=" + blockType +
                ", alias='" + alias + '\'' +
                ", path='" + path + '\'' +
                ", lines=" + Arrays.toString(lines) +
                ", types=" + Arrays.toString(types) +
                '}';
    }

    public List<TemplateElement> toElements() {
        List<TemplateElement> result = null;
        if (lines != null && types != null) {
            result = new ArrayList<TemplateElement>();
            int count = Math.min(lines.length, types.length);
            for(int i = 0; i<count; i++) {
                TemplateElement element = new TemplateElement(types[i], lines[i]);
                result.add(element);
            }
        }
        return  result;
    }
}
