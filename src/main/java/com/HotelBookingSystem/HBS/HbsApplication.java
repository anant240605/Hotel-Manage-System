package com.HotelBookingSystem.HBS;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class HbsApplication {

	public static void main(String[] args) {
		SpringApplication.run(HbsApplication.class, args);
	}

}
