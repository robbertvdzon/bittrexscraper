package com.vdzon.bittrexscraper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BittrexscraperApplication {

	public static void main(String[] args) {
		SpringApplication.run(BittrexscraperApplication.class, args);
	}
}
