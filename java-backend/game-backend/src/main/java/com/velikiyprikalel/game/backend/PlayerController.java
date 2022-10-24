package com.velikiyprikalel.game.backend;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

import com.velikiyprikalel.game.backend.exceptions.PlayerNotFoundException;

@CrossOrigin
@RestController
public class PlayerController {

    private final PlayerRepository playerRepository;

    PlayerController(final PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @PostMapping("/players/add")
    Player newPlayer(final @RequestBody Player newPlayer) {
        return playerRepository.save(newPlayer);
    }

    @GetMapping("/players/{username}")
    Player getPlayer(final @PathVariable String username) {
        return playerRepository.findById(username)
                .orElseThrow(() -> new PlayerNotFoundException(username));
    }

}
