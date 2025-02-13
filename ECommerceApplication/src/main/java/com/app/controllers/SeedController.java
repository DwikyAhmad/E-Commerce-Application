package com.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.seed.DataSeeder;
import jakarta.annotation.security.PermitAll;

@RestController
@RequestMapping("/api")
public class SeedController {

    @Autowired
    private DataSeeder dataSeeder;
    
    @PermitAll
    @GetMapping("/seed-all")
    public ResponseEntity<String> seedAll() {
        dataSeeder.seedAll();
        
        return new ResponseEntity<String>("Seeded all data", HttpStatus.OK);
    }
}
