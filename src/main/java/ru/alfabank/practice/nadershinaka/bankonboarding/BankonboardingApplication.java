package ru.alfabank.practice.nadershinaka.bankonboarding;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableMongoRepositories("ru.alfabank.practice.nadershinaka.bankonboarding.repository")
@EnableScheduling
public class BankonboardingApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankonboardingApplication.class, args);
	}

}
