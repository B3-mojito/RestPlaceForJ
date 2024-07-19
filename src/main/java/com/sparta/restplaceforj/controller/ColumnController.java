package com.sparta.restplaceforj.controller;

import com.sparta.restplaceforj.service.ColumnService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ColumnController {
    private final ColumnService columnService;
}
