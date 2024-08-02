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

import com.fdmgroup.accountcustomermngtspring.model.Address;
import com.fdmgroup.accountcustomermngtspring.model.CheckingAccount;
import com.fdmgroup.accountcustomermngtspring.model.Company;
import com.fdmgroup.accountcustomermngtspring.model.Customer;
import com.fdmgroup.accountcustomermngtspring.model.Person;
import com.fdmgroup.accountcustomermngtspring.model.SavingsAccount;
import com.fdmgroup.accountcustomermngtspring.repository.CustomerRepository;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {
	
	CustomerService customerService;
	
	@Mock 
	CustomerRepository mockCustomerRepo;
	
	@BeforeEach
	void setUp() throws Exception {
		customerService = new CustomerService(mockCustomerRepo);		
	}
	
	@Test
	void test_customerService_returnsCustomer_obtainedFromCustomerRepo() {
		// Setup test data
		Address address = new Address("2 QUEEN STREET", "R43 6Y6", "CALEDON", "ONTARIO");
		Person expectedPerson = new Person("JOHN", address);
		long id = 1001L;
		expectedPerson.setCustomerId(id);
		
		// Mock the repository call
		when(mockCustomerRepo.findById(id)).thenReturn(Optional.of(expectedPerson));
		
		// Act
		Customer retrievedPerson = customerService.getCustomerById(id);
		
		// Assert if expectedPerson is retrievedPerson
		assertEquals(expectedPerson, retrievedPerson);
	}
	
	@Test
    void test_createCustomer_savesCustomer_toCustomerRepo() {
        // Setup test data
        Address address = new Address("2 QUEEN STREET", "R43 6Y6", "CALEDON", "ONTARIO");
        Person personToSave = new Person("JOHN", address);
        Person savedPerson = new Person("JOHN", address);
        savedPerson.setCustomerId(1002L);
        
        // Mock the repository call
        when(mockCustomerRepo.save(personToSave)).thenReturn(savedPerson);
        
        // Act
        Customer createdPerson = customerService.createCustomer(personToSave);
        
        // Assert if savedPerson is createdPerson
        assertEquals(savedPerson, createdPerson);
    }
	
	@Test
    void test_updateCustomer_updatesCustomer_inCustomerRepo() {
        // Setup test data
        Address address = new Address("2 QUEEN STREET", "R43 6Y6", "CALEDON", "ONTARIO");
        Person existingPerson = new Person("JOHN", address);
        existingPerson.setCustomerId(1001L);
        
        Address updatedAddress = new Address("updated street", "updated postal code", "updated city", "updated province");
        Person updatedPerson = new Person("updated name", updatedAddress);
        updatedPerson.setCustomerId(1001L);
        
        // Update fields of existingPerson to match updatedPersonDetails
        existingPerson.setName(updatedPerson.getName());
        existingPerson.setAddress(updatedPerson.getAddress());
        
        // Mock the repository call
        when(mockCustomerRepo.existsById(existingPerson.getCustomerId())).thenReturn(true);
        when(mockCustomerRepo.save(existingPerson)).thenReturn(updatedPerson);
        
        // Act
        Customer updatedCustomer = customerService.updateCustomer(existingPerson);
        
        // Assert if savedPerson is createdPerson
        assertEquals(updatedPerson.getName(), updatedCustomer.getName());
        assertEquals(updatedPerson.getAddress().getCity(), updatedCustomer.getAddress().getCity());
        assertEquals(updatedPerson.getAddress().getPostalCode(), updatedCustomer.getAddress().getPostalCode());
        assertEquals(updatedPerson.getAddress().getStreetNumber(), updatedCustomer.getAddress().getStreetNumber());
        assertEquals(updatedPerson.getAddress().getProvince(), updatedCustomer.getAddress().getProvince());
    }
	
	@Test
    void test_deleteCustomer_deletesCustomer_inCustomerRepo() {
        // Setup test data
        long id = 1001L;
        
        // Mock the repository call
        when(mockCustomerRepo.existsById(id)).thenReturn(true);
        doNothing().when(mockCustomerRepo).deleteById(id);
        
        // Act
        boolean isDeleted = customerService.deleteCustomer(id);
        
        // Assert
        assertTrue(isDeleted);
        verify(mockCustomerRepo, times(1)).deleteById(id);
    }
	
	@Test
    void test_deleteCustomer_returnsFalse_whenCustomerDoesNotExist() {
        // Setup test data
        long id = 1001L;
        
        // Mock the repository call
        when(mockCustomerRepo.existsById(id)).thenReturn(false);
        
        // Act
        boolean isDeleted = customerService.deleteCustomer(id);
        
        // Assert
        assertFalse(isDeleted);
        verify(mockCustomerRepo, never()).deleteById(id);
    }
	
	@Test
    void test_getAllCustomers_returnsAllCustomers_fromCustomerRepo() {
        // Setup test data
        Address address1 = new Address("13 KENWORTH STREET", "T3R 3E3", "TORONTO", "ONTARIO");
        Address address2 = new Address("2 QUEEN STREET", "R43 6Y6", "CALEDON", "ONTARIO");
        Company company = new Company("FDM", address1);
        Person person = new Person("JOHN", address2);
        
        List<Customer> expectedCustomers = List.of(company, person);
        
        // Mock the repository call
        when(mockCustomerRepo.findAll()).thenReturn(expectedCustomers);
        
        // Act
        List<Customer> retrievedCustomers = customerService.getAllCustomers();
        
        // Assert
        assertEquals(expectedCustomers, retrievedCustomers);
    }	
	
	@Test
    void test_getCustomerById_returnsCustomerWithAccounts() {
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

        // Mock the repository call
        when(mockCustomerRepo.findById(customerId)).thenReturn(Optional.of(company));

        // Act
        Customer retrievedCustomer = customerService.getCustomerById(customerId);

        // Assert
        assertNotNull(retrievedCustomer);
        assertEquals(company, retrievedCustomer);
        assertNotNull(retrievedCustomer.getAccounts());
        assertEquals(2, retrievedCustomer.getAccounts().size());
        assertTrue(retrievedCustomer.getAccounts().contains(savingsAccount));
        assertTrue(retrievedCustomer.getAccounts().contains(checkingAccount));
    }
}
