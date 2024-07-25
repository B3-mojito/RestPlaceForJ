package com.sparta.restplaceforj.controller;

import com.sparta.restplaceforj.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class CommentController {

  private final CommentService commentService;
}
