package com.sparta.restplaceforj.controller;
import com.sparta.restplaceforj.service.PlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class PlanController {

    private final PlanService planService;
}
