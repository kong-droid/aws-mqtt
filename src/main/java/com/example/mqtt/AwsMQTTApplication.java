package com.example.mqtt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AwsMQTTApplication {

	public static void main(String[] args) {
		SpringApplication.run(AwsMQTTApplication.class, args);
		System.setProperty("com.amazonaws.sdk.disabledEc2Metadata", "true");
	}

}
