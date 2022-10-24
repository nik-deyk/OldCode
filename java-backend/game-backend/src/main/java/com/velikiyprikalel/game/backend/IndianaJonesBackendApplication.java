package com.velikiyprikalel.game.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan
@SpringBootApplication
public class IndianaJonesBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(IndianaJonesBackendApplication.class, args);
	}

}
