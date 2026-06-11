package com.tup.bentoflash;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BentoflashApplication {

	public static void main(String[] args) {
		SpringApplication.run(BentoflashApplication.class, args);
	}

}
