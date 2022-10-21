package com.velikiyprikalel.game.backend;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.velikiyprikalel.game.backend.exceptions.PlayerNotFoundException;

@RestController
public class PlayerController {

    private final PlayerRepository playerRepository;

    PlayerController(final PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @PostMapping("/players")
    Player newPlayer(final @RequestBody Player newPlayer) {
        return playerRepository.save(newPlayer);
    }

    @GetMapping("/players")
    Player getPlayer(final @RequestBody PlayerId playerId) {
        return playerRepository.findById(playerId.getUsername())
                .orElseThrow(() -> new PlayerNotFoundException(playerId));
    }

}
