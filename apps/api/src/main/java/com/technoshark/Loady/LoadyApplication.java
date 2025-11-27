package com.technoshark.Loady;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.technoshark.Loady.Utils.ApiResponse;
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

	@GetMapping("/health")
	public ResponseEntity<ApiResponse<String>> healthCheck() {
		return ResponseEntity.status(HttpStatus.OK)
				.body(ApiResponse.<String>builder()
						.success(true)
						.message("API is healthy")
						.data("OK")
						.status(200)
						.build());
	};

}
