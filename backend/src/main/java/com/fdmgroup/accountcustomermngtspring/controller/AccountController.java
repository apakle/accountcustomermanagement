package com.fdmgroup.accountcustomermngtspring.controller;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fdmgroup.accountcustomermngtspring.exception.AccountNotFoundException;
import com.fdmgroup.accountcustomermngtspring.exception.CustomerNotFoundException;
import com.fdmgroup.accountcustomermngtspring.model.Account;
import com.fdmgroup.accountcustomermngtspring.model.SavingsAccount;
import com.fdmgroup.accountcustomermngtspring.model.CheckingAccount;
import com.fdmgroup.accountcustomermngtspring.model.Customer;
import com.fdmgroup.accountcustomermngtspring.service.AccountService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/v1/accounts")
@CrossOrigin(origins="http://localhost:3000")
public class AccountController {
    private AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @Operation(summary = "Allows us to add a savings account to an existing customer")
    @ApiResponses(value = {
    		@ApiResponse(responseCode = "201", description = "Savings account resource successfully created."),
    		@ApiResponse(responseCode = "404")
    })
    @PostMapping("/savings/{customerId}")
    public ResponseEntity<Account> createSavingsAccount(@PathVariable Long customerId, @RequestBody SavingsAccount savingsAccount) {
        Account createdAccount = accountService.createAccount(savingsAccount, customerId);
        if (createdAccount != null) {
            URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{accountId}")
                .buildAndExpand(createdAccount.getAccountId())
                .toUri();
            return ResponseEntity.created(location).build();
        }
        throw new CustomerNotFoundException("Customer id=" + customerId + " not found"); 
    }

    @Operation(summary = "Allows us to add a checking account to an existing customer")
    @ApiResponses(value = {
    		@ApiResponse(responseCode = "201", description = "Checking account resource successfully created."),
    		@ApiResponse(responseCode = "404")
    })
    @PostMapping("/checking/{customerId}")
    public ResponseEntity<Account> createCheckingAccount(@PathVariable Long customerId, @RequestBody CheckingAccount checkingAccount) {
        Account createdAccount = accountService.createAccount(checkingAccount, customerId);
        if (createdAccount != null) {
            URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{accountId}")
                .buildAndExpand(createdAccount.getAccountId())
                .toUri();
            return ResponseEntity.created(location).build();
        }
        throw new CustomerNotFoundException("Customer id=" + customerId + " not found");  
    }
    
    @Operation(summary = "Grabs all accounts")
    @ApiResponses(value = {
    		@ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    })
    @GetMapping
    public List<Account> getAllAccounts() {
        return accountService.getAllAccounts();
    }
    
    @Operation(summary = "Allows us to fetch all accounts of customers by a given city")
    @ApiResponses(value = {
    		@ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
    		@ApiResponse(responseCode = "404")
    })
    @GetMapping("/by-city/{city}")
    public ResponseEntity<List<Account>> getAccountsByCity(@PathVariable String city) {
        List<Account> accounts = accountService.getAccountsByCustomerCity(city);
        if (accounts != null && !accounts.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(accounts);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    
    @Operation(summary = "Delete account by id")
    @ApiResponses(value = {
    		@ApiResponse(responseCode = "200"),
    		@ApiResponse(responseCode = "404")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
    	if(accountService.deleteAccount(id)) {
    		return ResponseEntity
    				.status(HttpStatus.OK)
    				.build();
    	}        
    	throw new AccountNotFoundException("Account id=" + id + " not found");   
    }
    
    @Operation(summary = "Grabs a account by id")
    @ApiResponses(value = {
    		@ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
    		@ApiResponse(responseCode = "404")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Account> getCustomerById(@PathVariable Long id) {
        Account account = accountService.getAccountById(id);
        
        if (account != null) {
        	return ResponseEntity
    				.status(HttpStatus.OK)
    				.body(account);
        }
        throw new AccountNotFoundException("Account id=" + id + " not found");    
    }
    
    @Operation(summary = "Allows us to update a savings account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Savings account resource successfully updated."),
            @ApiResponse(responseCode = "404")
    })
    @PutMapping("/savings/{id}")
    public ResponseEntity<Account> updateSavingsAccount(@PathVariable Long id, @RequestBody SavingsAccount updatedSavingsAccount) {
        Account existingAccount = accountService.getAccountById(id);
        if (existingAccount == null || !(existingAccount instanceof SavingsAccount)) {
            throw new AccountNotFoundException("Savings account id=" + id + " not found");
        }
        
        SavingsAccount savingsAccount = (SavingsAccount) existingAccount;
        savingsAccount.setBalance(updatedSavingsAccount.getBalance());
        savingsAccount.setInterestRate(updatedSavingsAccount.getInterestRate());
        
        Account updatedAccount = accountService.updateAccount(savingsAccount);
        return ResponseEntity.ok(updatedAccount);
    }

    @Operation(summary = "Allows us to update a checking account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Checking account resource successfully updated."),
            @ApiResponse(responseCode = "404")
    })
    @PutMapping("/checking/{id}")
    public ResponseEntity<Account> updateCheckingAccount(@PathVariable Long id, @RequestBody CheckingAccount updatedCheckingAccount) {
        Account existingAccount = accountService.getAccountById(id);
        if (existingAccount == null || !(existingAccount instanceof CheckingAccount)) {
            throw new AccountNotFoundException("Checking account id=" + id + " not found");
        }
        
        CheckingAccount checkingAccount = (CheckingAccount) existingAccount;
        checkingAccount.setBalance(updatedCheckingAccount.getBalance());
        checkingAccount.setNextCheckNumber(updatedCheckingAccount.getNextCheckNumber());
        
        Account updatedAccount = accountService.updateAccount(checkingAccount);
        return ResponseEntity.ok(updatedAccount);
    }
    
    @Operation(summary = "Allows us to deposit into an account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account resource successfully updated with deposit amount."),
            @ApiResponse(responseCode = "404")
    })
    @PutMapping("/{id}/deposit")
    public ResponseEntity<Account> deposit(@PathVariable Long id, @RequestParam double amount) {
        Account updatedAccount = accountService.deposit(id, amount);
        if (updatedAccount != null) {
            return ResponseEntity.ok(updatedAccount);
        }
        throw new AccountNotFoundException("Account id=" + id + " not found"); 
    }
}
