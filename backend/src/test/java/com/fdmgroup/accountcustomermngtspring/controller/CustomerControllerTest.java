package com.fdmgroup.accountcustomermngtspring.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

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
import com.fdmgroup.accountcustomermngtspring.model.Address;
import com.fdmgroup.accountcustomermngtspring.model.CheckingAccount;
import com.fdmgroup.accountcustomermngtspring.model.Company;
import com.fdmgroup.accountcustomermngtspring.model.Customer;
import com.fdmgroup.accountcustomermngtspring.model.Person;
import com.fdmgroup.accountcustomermngtspring.model.SavingsAccount;
import com.fdmgroup.accountcustomermngtspring.service.CustomerService;

@WebMvcTest(CustomerController.class)
@ExtendWith(MockitoExtension.class)
class CustomerControllerTest {
	
	@Autowired
    MockMvc mockMvc;
	
	@Autowired
	ObjectMapper objectMapper;
	
	@MockBean
	CustomerService mockCustomerService;
	
	@Test
	void test_getCustomerById_returns_statusOk_andCustomer_whenCustomerExists() throws Exception {
		// Setup test data
		Address address = new Address("2 QUEEN STREET", "R43 6Y6", "CALEDON", "ONTARIO");
		Person person = new Person("JOHN", address);
		long id = 1001L;
		person.setCustomerId(id);
		
		// Prepare the URL and expected JSON response
		String url = "/api/v1/customers/" + person.getCustomerId();
		String customerJsonString = objectMapper.writeValueAsString(person);
		
		// Mock the service call
		when(mockCustomerService.getCustomerById(id)).thenReturn(person);
		
		// Perform the request and verify the response
		mockMvc.perform(MockMvcRequestBuilders.get(url).accept(MediaType.APPLICATION_JSON_VALUE))
			.andExpectAll(MockMvcResultMatchers.status().isOk(),
				MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE), 
				MockMvcResultMatchers.content().json(customerJsonString));
	}
	
	@Test
	void test_createPerson_returns_statusCreated_andLocationHeader() throws Exception {
		// Setup test data
		Address address = new Address("2 QUEEN STREET", "R43 6Y6", "CALEDON", "ONTARIO");
		Person createdPerson = new Person("JOHN", address);		
		long id = 1002L;
		createdPerson.setCustomerId(id);
		
		// Prepare the URL and expected location header
		String url = "/api/v1/customers/person";
		String expectedCreatedUrl = "http://localhost/api/v1/customers/person/" + createdPerson.getCustomerId();		
		String customerJsonString = objectMapper.writeValueAsString(createdPerson); 	
		
		// Mock the service call
		when(mockCustomerService.createCustomer(any(Person.class))).thenReturn(createdPerson);
		
		// Perform the request and verify the response
		mockMvc.perform(MockMvcRequestBuilders.post(url).contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(customerJsonString))
				.andExpectAll(MockMvcResultMatchers.status().isCreated(),
						MockMvcResultMatchers.header().string("location", expectedCreatedUrl))
				.andDo(MockMvcResultHandlers.print()); 		
	}
	
	@Test
	void test_createCompany_returns_statusCreated_andLocationHeader() throws Exception {
		// Setup test data
        Address address = new Address("13 KENWORTH STREET", "T3R 3E3", "TORONTO", "ONTARIO");
        Company createdCompany = new Company("FDM", address);
		long id = 1001L;
		createdCompany.setCustomerId(id);
		
		// Prepare the URL and expected location header
		String url = "/api/v1/customers/company";
		String expectedCreatedUrl = "http://localhost/api/v1/customers/company/" + createdCompany.getCustomerId();		
		String customerJsonString = objectMapper.writeValueAsString(createdCompany); 
		
		// Mock the service call
		when(mockCustomerService.createCustomer(any(Company.class))).thenReturn(createdCompany);
		
		// Perform the request and verify the response
		mockMvc.perform(MockMvcRequestBuilders.post(url).contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(customerJsonString))
				.andExpectAll(MockMvcResultMatchers.status().isCreated(),
						MockMvcResultMatchers.header().string("location", expectedCreatedUrl))
				.andDo(MockMvcResultHandlers.print()); 		
	}

	
	@Test
	void test_updatePerson_returns_statusOk_andUpdatedCustomer() throws Exception {
		// Setup test data
		long id = 1001L;
		Address address = new Address("2 QUEEN STREET", "R43 6Y6", "CALEDON", "ONTARIO");
		Person existingPerson = new Person("JOHN", address);	
		existingPerson.setCustomerId(id);		
		
		// Updated details
		Address updatedAddress = new Address("updated street", "updated postal code", "updated city", "updated province");
		Person updatedPersonDetails = new Person("updated name", updatedAddress);
		updatedPersonDetails.setCustomerId(id);		
		Person updatedPerson = new Person("updated name", updatedAddress);
		updatedPerson.setCustomerId(id);
		
		// Prepare the URL and expected JSON response
		String url = "/api/v1/customers/person/" + id;
		String updatedCustomerJsonString = objectMapper.writeValueAsString(updatedPersonDetails);
		String responseCustomerJsonString = objectMapper.writeValueAsString(updatedPerson);
		
		// Mock the service calls
		when(mockCustomerService.getCustomerById(id)).thenReturn(existingPerson);
		when(mockCustomerService.updateCustomer(existingPerson)).thenReturn(updatedPerson);
		
		// Perform the request and verify the response
		mockMvc.perform(MockMvcRequestBuilders.put(url)
				.contentType(MediaType.APPLICATION_JSON_VALUE)
				.content(updatedCustomerJsonString))
			.andExpectAll(MockMvcResultMatchers.status().isOk(),
				MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE),
				MockMvcResultMatchers.content().json(responseCustomerJsonString))
			.andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	void test_deleteCustomerById_returns_statusOk_whenCustomerExists() throws Exception {
		long id = 1001L;
		
		String url = "/api/v1/customers/" + id;
		
		// Mock the service call
		when(mockCustomerService.deleteCustomer(id)).thenReturn(true);

		// Perform the request and verify the response
		mockMvc.perform(MockMvcRequestBuilders.delete(url))
			.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	void test_deleteCustomerById_returns_statusNotFound_whenCustomerDoesNotExist() throws Exception {
		long id = 1001L;
		
		String url = "/api/v1/customers/" + id;
		
		// Mock the service call
		when(mockCustomerService.deleteCustomer(id)).thenReturn(false);

		// Perform the request and verify the response
		mockMvc.perform(MockMvcRequestBuilders.delete(url))
			.andExpect(MockMvcResultMatchers.status().isNotFound());
	}
	
	@Test
	void test_getAllCustomers_returns_statusOk_andJsonArray() throws Exception {
		// Setup test data
		Address address1 = new Address("13 KENWORTH STREET", "T3R 3E3", "TORONTO", "ONTARIO");
        Address address2 = new Address("2 QUEEN STREET", "R43 6Y6", "CALEDON", "ONTARIO");

        Company company = new Company("FDM", address1);
        Person person = new Person("JOHN", address2);
		
		List<Customer> customers = List.of(company, person);
		
		// Prepare the URL and expected JSON response
		String url = "/api/v1/customers";
		String customersJsonString = objectMapper.writeValueAsString(customers);
		
        // Mock the service call
		when(mockCustomerService.getAllCustomers()).thenReturn(customers);		
		
		// Perform the request and verify the response
		mockMvc.perform(MockMvcRequestBuilders.get(url).accept(MediaType.APPLICATION_JSON_VALUE))
			.andExpectAll(MockMvcResultMatchers.status().isOk(),
				MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE),
				MockMvcResultMatchers.content().json(customersJsonString))
			.andDo(MockMvcResultHandlers.print());
	}
	
	@Test
    void test_getCustomerById_returnsCustomerWithAccounts() throws Exception {
        // Setup test data
        Address address = new Address("13 KENWORTH STREET", "T3R 3E3", "TORONTO", "ONTARIO");
        Company company = new Company("FDM", address);
        long customerId = 1001L;
        company.setCustomerId(customerId);

        SavingsAccount savingsAccount = new SavingsAccount(2300.0, 4.5);
        savingsAccount.setAccountId(1L);
        CheckingAccount checkingAccount = new CheckingAccount(120.0, 1);
        checkingAccount.setAccountId(3L);

        company.addAccount(savingsAccount);
        company.addAccount(checkingAccount);

        // Prepare the URL and expected JSON response
        String url = "/api/v1/customers/" + customerId;

        // Mock the service call
        when(mockCustomerService.getCustomerById(customerId)).thenReturn(company);

        // Perform the request and verify the response
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/customers/{id}", customerId)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(MockMvcResultMatchers.jsonPath("$.customerId").value(company.getCustomerId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(company.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.accounts[0].accountId").value(savingsAccount.getAccountId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.accounts[0].balance").value(savingsAccount.getBalance()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.accounts[0].interestRate").value(savingsAccount.getInterestRate()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.accounts[1].accountId").value(checkingAccount.getAccountId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.accounts[1].balance").value(checkingAccount.getBalance()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.accounts[1].nextCheckNumber").value(checkingAccount.getNextCheckNumber()))
                .andDo(MockMvcResultHandlers.print());

        // Verify the service method was called
        verify(mockCustomerService).getCustomerById(customerId);
    }

}