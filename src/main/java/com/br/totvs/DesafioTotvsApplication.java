package com.br.totvs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.br.totvs", "com.br.totvs.mapper"})
public class DesafioTotvsApplication {

	public static void main(String[] args) {
		SpringApplication.run(DesafioTotvsApplication.class, args);
	}

}
