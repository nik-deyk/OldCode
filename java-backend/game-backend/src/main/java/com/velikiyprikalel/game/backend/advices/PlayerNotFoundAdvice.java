package com.velikiyprikalel.game.backend.advices;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.velikiyprikalel.game.backend.exceptions.PlayerNotFoundException;

@ControllerAdvice
class PlayerNotFoundAdvice {

  @ResponseBody
  @ExceptionHandler(PlayerNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  String playerNotFoundHandler(PlayerNotFoundException ex) {
    return ex.getMessage();
  }
}