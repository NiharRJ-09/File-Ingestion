package com.example.file_ingestion_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableAsync
@EnableTransactionManagement
public class FileIngestionSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(FileIngestionSystemApplication.class, args);
	}

}
