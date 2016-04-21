package com.feamor.beauty.models;

import javax.persistence.*;

/**
 * Created by Home on 27.02.2016.
 */
@Entity(name = "blockType")
@Table(name = "block_type")
public class BlockType {
    @Id
    @Column(name = "id")
    @SequenceGenerator(name = "block_type_id_generator", sequenceName = "block_type_sequence")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "block_type_id_generator")
    private int id;

    @Column(name = "class_id", nullable = false)
    private int classId;

    @Column (name = "alias")
    private String alias;

    @Column (name = "description")
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
