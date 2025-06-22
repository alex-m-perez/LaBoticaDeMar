package es.laboticademar.webstore.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.annotation.PostConstruct;

@Configuration
@Profile("dev")
public class DotenvConfig {

    @PostConstruct
    public void loadEnv() {
        Dotenv.configure()
              .filename(".env")
              .ignoreIfMissing()
              .load();
    }

}
