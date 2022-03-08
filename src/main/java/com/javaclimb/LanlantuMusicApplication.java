package com.javaclimb;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.javaclimb.dao")
public class LanlantuMusicApplication {
    public static void main(String[] args) {
        SpringApplication.run(LanlantuMusicApplication.class,args);
    }
}
