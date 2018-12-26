package cn.com.clm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"cn.com"})
public class ExportEasyDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExportEasyDemoApplication.class, args);
	}

}

