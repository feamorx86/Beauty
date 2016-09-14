package com.feamor.beauty.models.db;

import javax.persistence.*;

/**
 * Created by Home on 18.05.2016.
 */
@Entity(name = "userGroup")
@Table(name = "\"UserGroup\"")
public class UserGroup {
    @Id
    @Column(name = "\"id\"", nullable = false, unique = true)
    @SequenceGenerator(name = "userGroupIdGenerator", sequenceName = "\"UserGroupSequence\"")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userGroupIdGenerator")
    private int id;

    @Column(name = "\"name\"")
    private String name;

    @Column(name = "\"type\"")
    private Integer type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
