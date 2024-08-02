package com.fdmgroup.accountcustomermngtspring.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fdmgroup.accountcustomermngtspring.model.Account;
import com.fdmgroup.accountcustomermngtspring.model.Address;
import com.fdmgroup.accountcustomermngtspring.model.CheckingAccount;
import com.fdmgroup.accountcustomermngtspring.model.Company;
import com.fdmgroup.accountcustomermngtspring.model.Customer;
import com.fdmgroup.accountcustomermngtspring.model.Person;
import com.fdmgroup.accountcustomermngtspring.model.SavingsAccount;
import com.fdmgroup.accountcustomermngtspring.repository.AccountRepository;
import com.fdmgroup.accountcustomermngtspring.repository.CustomerRepository;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

	AccountService accountService;
	
	@Mock 
    AccountRepository mockAccountRepo;
	
	@Mock
    CustomerRepository mockCustomerRepo;
	
	@BeforeEach
    void setUp() throws Exception {
        accountService = new AccountService(mockAccountRepo, mockCustomerRepo);        
    }
	
	@Test
    void test_createSavingsAccount_savesAccount_toAccountRepo() {
        // Setup test data
        SavingsAccount savingsAccount = new SavingsAccount(2300.0, 4.5);
        savingsAccount.setAccountId(1L);
        Address address = new Address("2 QUEEN STREET", "R43 6Y6", "CALEDON", "ONTARIO");
        Person customer = new Person("JOHN", address);
        long customerId = 1001L;
        customer.setCustomerId(customerId);

        // Mock the repository call
        when(mockCustomerRepo.findById(customerId)).thenReturn(Optional.of(customer));
        when(mockAccountRepo.save(savingsAccount)).thenReturn(savingsAccount);
        
        // Act
        Account createdAccount = accountService.createAccount(savingsAccount, customerId);
        
        // Assert
        assertEquals(savingsAccount, createdAccount);
    }
	
	@Test
    void test_createCheckingAccount_savesAccount_toAccountRepo() {
        // Setup test data
		CheckingAccount checkingAccount = new CheckingAccount(120.0, 1);
		checkingAccount.setAccountId(2L);
        Address address = new Address("2 QUEEN STREET", "R43 6Y6", "CALEDON", "ONTARIO");
        Person customer = new Person("JOHN", address);
        long customerId = 1002L;
        customer.setCustomerId(customerId);

        // Mock the repository call
        when(mockCustomerRepo.findById(customerId)).thenReturn(Optional.of(customer));
        when(mockAccountRepo.save(checkingAccount)).thenReturn(checkingAccount);
        
        // Act
        Account createdAccount = accountService.createAccount(checkingAccount, customerId);
        
        // Assert
        assertEquals(checkingAccount, createdAccount);
    }
	
	@Test
    void test_getAllAccountsByCity_returnsAccounts_fromAccountRepo() {
        // Setup test data		
		Address address1 = new Address("13 KENWORTH STREET", "T3R 3E3", "TORONTO", "ONTARIO");
        Address address2 = new Address("2 QUEEN STREET", "R43 6Y6", "CALEDON", "ONTARIO");
        Company company = new Company("FDM", address1);
        Person person = new Person("JOHN", address2);
                
        SavingsAccount savingsAccount = new SavingsAccount(2300.0, 4.5);
        savingsAccount.setAccountId(1L);
        CheckingAccount checkingAccount = new CheckingAccount(120.0, 1);
		checkingAccount.setAccountId(2L);
		
		List<Account> expectedAccounts = List.of(savingsAccount, checkingAccount);
		
		company.addAccount(savingsAccount);
		person.addAccount(checkingAccount);

		List<Customer> customers = List.of(company, person);
		
        // Mock the repository call
        when(mockCustomerRepo.findByAddressCity("TORONTO")).thenReturn(customers);

        // Act
        List<Account> retrievedAccounts = accountService.getAccountsByCustomerCity("TORONTO");
        
        // Assert
        assertEquals(expectedAccounts, retrievedAccounts);
    }
	
	@Test
    void test_updateSavingsAccount_updatesAccount_inAccountRepo() {
        // Setup test data
        SavingsAccount existingSavingsAccount = new SavingsAccount(2300.0, 4.5);
        existingSavingsAccount.setAccountId(1L);

        SavingsAccount updatedSavingsAccount = new SavingsAccount(2500.0, 5.0);
        updatedSavingsAccount.setAccountId(1L);

        // Mock the repository call
        when(mockAccountRepo.existsById(existingSavingsAccount.getAccountId())).thenReturn(true);
        when(mockAccountRepo.save(existingSavingsAccount)).thenReturn(updatedSavingsAccount);

        // Act
        Account updatedAccount = accountService.updateAccount(existingSavingsAccount);

        // Assert
        assertEquals(updatedSavingsAccount, updatedAccount);
    }
	
	@Test
    void test_updateCheckingAccount_updatesAccount_inAccountRepo() {
        // Setup test data
        CheckingAccount existingCheckingAccount = new CheckingAccount(120.0, 1);
        existingCheckingAccount.setAccountId(2L);

        CheckingAccount updatedCheckingAccount = new CheckingAccount(130.0, 2);
        updatedCheckingAccount.setAccountId(2L);

        // Mock the repository call
        when(mockAccountRepo.existsById(existingCheckingAccount.getAccountId())).thenReturn(true);
        when(mockAccountRepo.save(existingCheckingAccount)).thenReturn(updatedCheckingAccount);

        // Act
        Account updatedAccount = accountService.updateAccount(existingCheckingAccount);

        // Assert
        assertEquals(updatedCheckingAccount, updatedAccount);
    }
	
	@Test
    void test_deleteAccount_deletesAccount_fromAccountRepo() {
        // Setup test data
        long accountId = 1L;

        // Mock the repository call
        when(mockAccountRepo.existsById(accountId)).thenReturn(true);
        doNothing().when(mockAccountRepo).deleteById(accountId);

        // Act
        boolean isDeleted = accountService.deleteAccount(accountId);

        // Assert
        assertTrue(isDeleted);
        verify(mockAccountRepo, times(1)).deleteById(accountId);
    }

    @Test
    void test_deleteAccount_returnsFalse_whenAccountDoesNotExist() {
        // Setup test data
        long accountId = 1L;

        // Mock the repository call
        when(mockAccountRepo.existsById(accountId)).thenReturn(false);

        // Act
        boolean isDeleted = accountService.deleteAccount(accountId);

        // Assert
        assertFalse(isDeleted);
        verify(mockAccountRepo, never()).deleteById(accountId);
    }
	
}
