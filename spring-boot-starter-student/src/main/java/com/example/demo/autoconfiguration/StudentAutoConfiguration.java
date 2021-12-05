package com.example.demo.autoconfiguration;



import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

@Configuration
@EnableConfigurationProperties(StudentProperties.class)
public class StudentAutoConfiguration {

    /**
     * 注入属性配置类
     */
    @Resource
    private StudentProperties studentProperties;

    @Bean(name = "student")
    public StudentService studentService() {
        StudentService student = new StudentService(studentProperties.getId(),studentProperties.getName());
        // 如果提供了其他set方法，在此也可以调用对应方法对其进行相应的设置或初始化。
        return student;
    }
}
