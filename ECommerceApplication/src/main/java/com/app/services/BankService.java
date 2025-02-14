package com.app.services;

import java.util.List;

import com.app.entites.Bank;
import com.app.payloads.BankDTO;

public interface BankService {

    Bank createBank(BankDTO bankDTO);

    List<Bank> getBanks();

    Bank getBank(Long bankId);

    Bank updateBank(Long bankId, BankDTO bankDTO);

    String deleteBank(Long bankId);

    Bank getBankByName(String bankName);
    
}