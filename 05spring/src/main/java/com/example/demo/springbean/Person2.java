package com.example.demo.springbean;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("person2")
public class Person2 {
    @Value("liuzhongshan2")
    private String name ;
    @Value("26")
    private Integer age ;

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public Integer getAge() {
        return age;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
