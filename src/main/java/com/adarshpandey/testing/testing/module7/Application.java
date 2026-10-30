package com.adarshpandey.testing.testing.module7;

import com.adarshpandey.testing.testing.module7.services.DataService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@RequiredArgsConstructor
@SpringBootApplication
public class Application implements  CommandLineRunner{
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

//	private  final DataService dataService;

	@Value("${my.variable}")
	private String myVariable;

	@Override
	public void run(String... args) throws Exception {

		System.out.println("my variable: "+myVariable);
//		System.out.println("The data is: "+dataService.getData());

	}
}
