package com.example.demo.springbean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class TestBean {


    @Resource(name = "person2")
    private Person2 person2;

    public static void main(String[] args) {

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        Object person1 = context.getBean("person1");
        System.out.println(person1);
        new TestBean().sout();

        ApplicationContext ctx = new AnnotationConfigApplicationContext(PersonConfig.class);
        Person person3 = ctx.getBean(Person.class);
        System.out.println(person3);
    }


    public void sout(){
        System.out.println(person2);
    }
}
