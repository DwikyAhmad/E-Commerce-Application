package com.app.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.app.entites.Bank;

public interface BankRepo extends JpaRepository<Bank, Long> {

    <Optional>Bank findByBankId(String bankName);
    <Optional>Bank findByBankName(String bankName);

}
