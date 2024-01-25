package com.ssafy.moiroomserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class MoiroomserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(MoiroomserverApplication.class, args);
	}

}