package org.foxminded.rymarovych;

import org.foxminded.rymarovych.service.StudentService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class UniversityCmsApplication {


	public static void main(String[] args) {
		ApplicationContext applicationContext = SpringApplication.run(UniversityCmsApplication.class, args);
	}

}
