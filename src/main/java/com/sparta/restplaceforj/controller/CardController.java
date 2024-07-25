package com.sparta.restplaceforj.controller;

import com.sparta.restplaceforj.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class CardController {

  private final CardService cardService;
}

