package skkunion.union2024;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAsync
@EnableJpaAuditing
@EnableScheduling
public class Skkunion2024Application {

    public static void main(String[] args) {
        SpringApplication.run(Skkunion2024Application.class, args);
    }

}
