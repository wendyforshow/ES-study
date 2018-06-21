package com.kh.search.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.kh.search")
@MapperScan(basePackages = "com.kh.search.dao")
public class KhSearchConfigApplication {

	public static void main(String[] args) {
		SpringApplication.run(KhSearchConfigApplication.class, args);
	}
}
