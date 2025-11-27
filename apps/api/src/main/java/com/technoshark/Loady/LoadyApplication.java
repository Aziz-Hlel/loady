package com.technoshark.Loady;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.technoshark.Loady.Utils.AppProperties;

@SpringBootApplication
@RestController
@RequestMapping("/")
@EnableConfigurationProperties(AppProperties.class)
public class LoadyApplication {

	public static void main(String[] args) {
		SpringApplication.run(LoadyApplication.class, args);
	}

	@GetMapping("/")
	public String getMethodName() {
		return "scwx";
	};

}
