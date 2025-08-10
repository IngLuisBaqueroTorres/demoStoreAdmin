package com.ingeduardo.demostore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@SpringBootApplication
@EnableSpringDataWebSupport
public class DemoStoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoStoreApplication.class, args);
	}

}
