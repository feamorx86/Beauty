package com.feamor.beauty.models;

import javax.annotation.Generated;
import javax.persistence.*;

/**
 * Created by Home on 27.02.2016.
 */
@Entity(name = "pageType")
@Table(name = "page_types")
public class PageType {
    @Id
    @Column(name = "id")
    @SequenceGenerator(name = "page_types_id_generator", sequenceName = "page_types_sequence")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "page_types_id_generator")
    private int id;

    @Column(name = "class_id", nullable = false)
    private int classId;

    @Column(name = "alias")
    private String alias;

    @Column(name = "description")
    private String description;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
