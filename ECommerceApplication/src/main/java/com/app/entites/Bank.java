package com.app.entites;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "banks")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bank {

    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bankId;
    
    @Column(unique = true, nullable = false)
    private String bankName;
    
    private long accountNumber;
    
}
