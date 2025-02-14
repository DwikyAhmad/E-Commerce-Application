package com.app.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.entites.Bank;
import com.app.payloads.BankDTO;
import com.app.payloads.ResponseDTO;
import com.app.services.BankService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;



@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "E-Commerce Application")
public class BankController {

    @Autowired
    private BankService bankService;
    
    @GetMapping("/public/banks")
    public ResponseEntity<?> getBanks() {
        List<Bank> banks = bankService.getBanks();
        ResponseDTO response = new ResponseDTO("List of banks", banks, 200);

        return new ResponseEntity<ResponseDTO>(response, HttpStatus.OK);
    }

    @GetMapping("/public/banks/{bankId}")
    public ResponseEntity<?> getBank(@PathVariable Long bankId) {
        Bank bank = bankService.getBank(bankId);
        ResponseDTO response = new ResponseDTO("Bank with id: " + bankId, bank, 200);

        return new ResponseEntity<ResponseDTO>(response, HttpStatus.OK);
    }

    @PostMapping("/admin/bank")
    public ResponseEntity<?> createBank(@RequestBody BankDTO bankDTO) {
        Bank bank = bankService.createBank(bankDTO);
        ResponseDTO response = new ResponseDTO("Bank created", bank, 201);

        return new ResponseEntity<ResponseDTO>(response, HttpStatus.CREATED);
    }
    
    @PutMapping("/admin/bank/{id}")
    public ResponseEntity<?> UpdateBank(@PathVariable Long id, @RequestBody BankDTO bankDTO) {
        Bank bank = bankService.updateBank(id, bankDTO);
        ResponseDTO response = new ResponseDTO("Bank updated", bank, 200);

        return new ResponseEntity<ResponseDTO>(response, HttpStatus.OK);
    }

    @DeleteMapping("/admin/bank/{id}")
    public ResponseEntity<?> deleteBank(@PathVariable Long id) {
        String response = bankService.deleteBank(id);
        ResponseDTO responseDTO = new ResponseDTO(response, null, 200);

        return new ResponseEntity<ResponseDTO>(responseDTO, HttpStatus.OK);
    }
    
}
