package com.fdmgroup.accountcustomermngtspring.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fdmgroup.accountcustomermngtspring.model.Account;
import com.fdmgroup.accountcustomermngtspring.model.CheckingAccount;
import com.fdmgroup.accountcustomermngtspring.model.SavingsAccount;
import com.fdmgroup.accountcustomermngtspring.service.AccountService;

@WebMvcTest(AccountController.class)
@ExtendWith(MockitoExtension.class)
class AccountControllerTest {

	@Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    AccountService mockAccountService;
    
    @Test
    void test_createSavingsAccount_returns_statusCreated_andLocationHeader() throws Exception {
        // Setup test data
        long customerId = 1001L;
        SavingsAccount savingsAccount = new SavingsAccount(2300.0, 4.5);
        savingsAccount.setAccountId(1L);

        // Prepare the URL and expected location header
        String url = "/api/v1/accounts/savings/" + customerId;
        String expectedCreatedUrl = "http://localhost/api/v1/accounts/savings/" + customerId + "/" +  savingsAccount.getAccountId();
        String accountJsonString = objectMapper.writeValueAsString(savingsAccount);

        // Mock the service call
        when(mockAccountService.createAccount(any(SavingsAccount.class), any(Long.class))).thenReturn(savingsAccount);

        // Perform the request and verify the response
        mockMvc.perform(MockMvcRequestBuilders.post(url).contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(accountJsonString))
                .andExpectAll(MockMvcResultMatchers.status().isCreated(),
                        MockMvcResultMatchers.header().string("location", expectedCreatedUrl))
                .andDo(MockMvcResultHandlers.print());
    }
    
    @Test
    void test_createCheckingAccount_returns_statusCreated_andLocationHeader() throws Exception {
        // Setup test data
        long customerId = 1002L;
        CheckingAccount checkingAccount = new CheckingAccount(120.0, 1);
        checkingAccount.setAccountId(1L);

        // Prepare the URL and expected location header
        String url = "/api/v1/accounts/checking/" + customerId;
        String expectedCreatedUrl = "http://localhost/api/v1/accounts/checking/"  + customerId + "/" + checkingAccount.getAccountId();
        String accountJsonString = objectMapper.writeValueAsString(checkingAccount);

        // Mock the service call
        when(mockAccountService.createAccount(any(CheckingAccount.class), any(Long.class))).thenReturn(checkingAccount);

        // Perform the request and verify the response
        mockMvc.perform(MockMvcRequestBuilders.post(url).contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(accountJsonString))
                .andExpectAll(MockMvcResultMatchers.status().isCreated(),
                        MockMvcResultMatchers.header().string("location", expectedCreatedUrl))
                .andDo(MockMvcResultHandlers.print());
    }
    
    @Test
    void test_getAllAccountsByCity_returns_statusOk_andJsonArray() throws Exception {
        // Setup test data
        SavingsAccount savingsAccount = new SavingsAccount(2300.0, 4.5);
        savingsAccount.setAccountId(1L);
        CheckingAccount checkingAccount = new CheckingAccount(120.0, 1);
        checkingAccount.setAccountId(2L);

        List<Account> accounts = List.of(savingsAccount, checkingAccount);

        // Prepare the URL and expected JSON response
        String url = "/api/v1/accounts/by-city/TORONTO";
        String accountsJsonString = objectMapper.writeValueAsString(accounts);

        // Mock the service call
        when(mockAccountService.getAccountsByCustomerCity("TORONTO")).thenReturn(accounts);

        // Perform the request and verify the response
        mockMvc.perform(MockMvcRequestBuilders.get(url).accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpectAll(MockMvcResultMatchers.status().isOk(),
                        MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE),
                        MockMvcResultMatchers.content().json(accountsJsonString))
                .andDo(MockMvcResultHandlers.print());

        // Verify the service method was called
        verify(mockAccountService).getAccountsByCustomerCity("TORONTO");
    }
    
    @Test
    void test_updateSavingsAccount_returns_statusOk_andUpdatedAccount() throws Exception {
    	// Setup test data
        long accountId = 1L;
        SavingsAccount existingSavingsAccount = new SavingsAccount(1000.0, 0.03);
        existingSavingsAccount.setAccountId(accountId);

        SavingsAccount updatedSavingsAccount = new SavingsAccount(2000.0, 0.05);
        updatedSavingsAccount.setAccountId(accountId);

        // Prepare the URL and expected JSON response
        String url = "/api/v1/accounts/savings/" + accountId;
        String updatedAccountJsonString = objectMapper.writeValueAsString(updatedSavingsAccount);

        // Mock the service call
        when(mockAccountService.getAccountById(accountId)).thenReturn(existingSavingsAccount);
        when(mockAccountService.updateAccount(existingSavingsAccount)).thenReturn(updatedSavingsAccount);

        // Perform the request and verify the response
        mockMvc.perform(MockMvcRequestBuilders.put(url)
        		.contentType(MediaType.APPLICATION_JSON_VALUE)
        		.content(updatedAccountJsonString))
        	.andExpectAll(MockMvcResultMatchers.status().isOk(),
    			MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE),
				MockMvcResultMatchers.content().json(updatedAccountJsonString))
        	.andDo(MockMvcResultHandlers.print());

        // Verify the service method was called
        verify(mockAccountService).getAccountById(accountId);
        verify(mockAccountService).updateAccount(existingSavingsAccount);
    }
    
    @Test
    void test_updateCheckingAccount_returns_statusOk_andUpdatedAccount() throws Exception {
    	// Setup test data
        long accountId = 1L;
        CheckingAccount existingCheckingAccount = new CheckingAccount(500.0, 1001);
        existingCheckingAccount.setAccountId(accountId);

        CheckingAccount updatedCheckingAccount = new CheckingAccount(1000.0, 2002);
        updatedCheckingAccount.setAccountId(accountId);

        // Prepare the URL and expected JSON response
        String url = "/api/v1/accounts/checking/" + accountId;
        String updatedAccountJsonString = objectMapper.writeValueAsString(updatedCheckingAccount);
     
        // Mock the service call
        when(mockAccountService.getAccountById(accountId)).thenReturn(existingCheckingAccount);
        when(mockAccountService.updateAccount(existingCheckingAccount)).thenReturn(updatedCheckingAccount);

        // Perform the request and verify the response
        mockMvc.perform(MockMvcRequestBuilders.put(url)
        		.contentType(MediaType.APPLICATION_JSON_VALUE)
        		.content(updatedAccountJsonString))
        	.andExpectAll(MockMvcResultMatchers.status().isOk(),
    			MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE),
				MockMvcResultMatchers.content().json(updatedAccountJsonString))
        	.andDo(MockMvcResultHandlers.print());

        // Verify the service method was called
        verify(mockAccountService).getAccountById(accountId);
        verify(mockAccountService).updateAccount(existingCheckingAccount);
    }
    
    @Test
    void test_deleteAccount_returns_statusOk_whenAccountExists() throws Exception {
        long accountId = 1L;

        String url = "/api/v1/accounts/" + accountId;
     
        // Mock the service call
        when(mockAccountService.deleteAccount(accountId)).thenReturn(true);

    	// Perform the request and verify the response
 		mockMvc.perform(MockMvcRequestBuilders.delete(url))
 			.andExpect(MockMvcResultMatchers.status().isOk());

 		// Verify the service method was called
        verify(mockAccountService).deleteAccount(accountId);
    }

    @Test
    void test_deleteAccount_returns_statusNotFound_whenAccountDoesNotExist() throws Exception {
        long accountId = 1L;

        String url = "/api/v1/accounts/" + accountId;

        // Mock the service call
        when(mockAccountService.deleteAccount(accountId)).thenReturn(false);

        // Perform the request and verify the response
 		mockMvc.perform(MockMvcRequestBuilders.delete(url))
 			.andExpect(MockMvcResultMatchers.status().isNotFound());

 		// Verify the service method was called
        verify(mockAccountService).deleteAccount(accountId);
    }
    
    
    

}
