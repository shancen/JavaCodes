package com.example.demo.autoconfiguration;


import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "student")
public class StudentProperties {


    private int id = 0;
    private String name ="duancen";

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

    @Override
    public String toString() {
        return "StudentProperties{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
