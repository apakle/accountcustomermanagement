package com.fdmgroup.accountcustomermngtspring.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;

@Entity
public class SavingsAccount extends Account {
	@Column(nullable = false)
	@NotNull (message = "Interest rate must not be null")
    private double interestRate;

    SavingsAccount() {}

    public SavingsAccount(double balance, double interestRate) {
        super(balance);
        this.interestRate = interestRate;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(double interestRate) {
        this.interestRate = interestRate;
    }
    
    @Override
    public void withdraw(double amount) {
        if (amount > 0) {
            if (this.balance >= amount) {
                this.balance -= amount;
            } else {
                throw new IllegalArgumentException("Cannot overdraw a savings account");
            }
        } else {
            throw new IllegalArgumentException("Withdrawal amount must be positive");
        }
    }
}
