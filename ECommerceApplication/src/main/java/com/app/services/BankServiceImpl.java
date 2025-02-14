package com.app.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.entites.Bank;
import com.app.payloads.BankDTO;
import com.app.repositories.BankRepo;

import jakarta.transaction.Transactional;

@Transactional
@Service
public class BankServiceImpl implements BankService {

    @Autowired
    private BankRepo bankRepo;

    @Override
    public Bank createBank(BankDTO bankDTO) {
        Bank bank = new Bank();
        bank.setBankName(bankDTO.getBankName().toLowerCase());
        bank.setAccountNumber(bankDTO.getAccountNumber());

        Bank savedBank = bankRepo.save(bank);

        return savedBank;
    }

    @Override
    public List<Bank> getBanks() {
        List<Bank> banks = bankRepo.findAll();

        return banks;
    }

    @Override
    public Bank getBank(Long bankId) {
        Bank bank = bankRepo.findById(bankId).get();

        return bank;
    }

    @Override
    public Bank updateBank(Long bankId, BankDTO bankDTO) {
        Bank oldbank = bankRepo.findById(bankId).get();
        oldbank.setBankName(bankDTO.getBankName().toLowerCase());
        oldbank.setAccountNumber(bankDTO.getAccountNumber());

        Bank newBank = bankRepo.save(oldbank);

        return newBank;
    }

    @Override
    public String deleteBank(Long bankId) {
        bankRepo.deleteById(bankId);

        return "Bank with id: " + bankId + " has been deleted";
    }

    @Override
    public Bank getBankByName(String bankName) {
        Bank bank = bankRepo.findByBankName(bankName);

        return bank;
    }
    
}
