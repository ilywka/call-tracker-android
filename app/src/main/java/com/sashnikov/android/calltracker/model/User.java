package com.sashnikov.android.calltracker.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * @author Ilya_Sashnikau
 */
@Entity(tableName = "user")
public class User {

    @PrimaryKey
    private Long id;

    private String name;
    private Integer age;


    public User(Long id, String name, Integer age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
