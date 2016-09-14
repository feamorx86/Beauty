package com.feamor.beauty.models.db;

import javax.persistence.*;

/**
 * Created by Home on 02.05.2016.
 */
@Entity(name = "userData")
@Table(name = "\"UserData\"")
public class UserData {
    @Id
    @Column(name = "id", nullable = false, unique = true)
    @SequenceGenerator(name = "userDataIdGenerator", sequenceName = "\"UserDataSequence\"")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userDataIdGenerator")
    private int id;

    @Column(name = "\"type\"", nullable = false)
    private int type;

    @Column(name = "\"userId\"", nullable = false)
    private int userId;

    @Column(name = "\"intValue\"")
    private int intValue;

    @Column(name = "\"strValue\"")
    private String stringValue;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getIntValue() {
        return intValue;
    }

    public void setIntValue(int intValue) {
        this.intValue = intValue;
    }

    public String getStringValue() {
        return stringValue;
    }

    public void setStringValue(String stringValue) {
        this.stringValue = stringValue;
    }
}
