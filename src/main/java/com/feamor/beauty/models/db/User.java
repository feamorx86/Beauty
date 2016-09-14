package com.feamor.beauty.models.db;

import javax.persistence.*;
import java.util.List;

/**
 * Created by Home on 02.05.2016.
 */

@Entity(name = "user")
@Table(name = "\"User\"")
public class User {
    @Id
    @Column(name = "\"id\"", nullable = false, unique = true)
    @SequenceGenerator(name = "userIdGenerator", sequenceName = "\"UserSequence\"")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userIdGenerator")
    private int id;

    @Column(name = "\"alias\"")
    private String alias;

    @Column(name = "\"firstName\"")
    private String firstName;

    @Column(name = "\"lastName\"")
    private String lastName;

    @Column(name = "\"phone\"")
    private String phone;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}
