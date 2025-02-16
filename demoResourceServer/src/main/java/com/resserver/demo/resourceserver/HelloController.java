package com.resserver.demo.resourceserver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class HelloController {
    
    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        log.info("hello called");
        return ResponseEntity.ok("hello");
    }

    @GetMapping("/hello-auth")
    public ResponseEntity<String> helloAuth() {
        log.info("hello-auth called");
        return ResponseEntity.ok("hello auth");
    }
    
    @GetMapping("/hello-admin")
    public ResponseEntity<String> helloAdmin() {
        log.info("hello-admin called");
        return ResponseEntity.ok("hello admin");
    }
}
