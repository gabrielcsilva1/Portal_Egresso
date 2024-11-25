package com.gabrielcsilva1.Portal_Egresso.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/egress")
public class EgressController {

  @GetMapping
  public String get() {
    return "Hello";
  }
}
