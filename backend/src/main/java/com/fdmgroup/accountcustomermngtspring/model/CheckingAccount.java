package com.fdmgroup.accountcustomermngtspring.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;

@Entity
public class CheckingAccount extends Account {
	@Column(nullable = false)
	@NotNull (message = "Next Checknumber must not be null")
    private int nextCheckNumber;

    CheckingAccount() {}

    public CheckingAccount(double balance, int nextCheckNumber) {
        super(balance);
        this.nextCheckNumber = nextCheckNumber;
    }

    public int getNextCheckNumber() {
        return nextCheckNumber;
    }

    public void setNextCheckNumber(int nextCheckNumber) {
        this.nextCheckNumber = nextCheckNumber;
    }
}
