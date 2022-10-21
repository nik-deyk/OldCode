package com.velikiyprikalel.game.backend.config;

import java.util.logging.Logger;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.velikiyprikalel.game.backend.Player;
import com.velikiyprikalel.game.backend.PlayerRepository;

@Configuration
public class LoadDatabase {
    private static final Logger log = Logger.getLogger(LoadDatabase.class.toString());

    @Bean
    public CommandLineRunner initDatabase(PlayerRepository repository) {
  
      return args -> {
        log.info("Preloading " + repository.save(new Player("Alex", 2)));
      };
    }
}
