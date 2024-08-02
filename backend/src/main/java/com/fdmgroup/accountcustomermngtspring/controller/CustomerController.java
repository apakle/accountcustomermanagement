package com.fdmgroup.accountcustomermngtspring.controller;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fdmgroup.accountcustomermngtspring.exception.CustomerNotFoundException;
import com.fdmgroup.accountcustomermngtspring.model.Account;
import com.fdmgroup.accountcustomermngtspring.model.Company;
import com.fdmgroup.accountcustomermngtspring.model.Customer;
import com.fdmgroup.accountcustomermngtspring.model.Person;
import com.fdmgroup.accountcustomermngtspring.service.CustomerService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/api/v1/customers")
@CrossOrigin(origins="http://localhost:3000")
public class CustomerController {   
    private CustomerService customerService;
        
    @Autowired
    public CustomerController(CustomerService customerService) {
		this.customerService = customerService;
	}

    @Operation(summary = "Allows us to create a customer of type person")
    @ApiResponses(value = {
    		@ApiResponse(responseCode = "201", description = "Person resource successfully created."),
    		@ApiResponse(responseCode = "409")
    })
    @PostMapping("/person")
    public ResponseEntity<Customer> createCustomer(@RequestBody Person person) {
		Customer createCustomer = customerService.createCustomer(person);
		
		if(createCustomer != null) {
    		URI location = ServletUriComponentsBuilder
    				.fromCurrentRequest()
    				.path("/{id}")
    				.buildAndExpand(createCustomer.getCustomerId())
    				.toUri();
    		return ResponseEntity
    				.created(location)
    				.build();
    	}
    	return ResponseEntity
    			.status(HttpStatus.CONFLICT)
    			.build();
    }
    
    @Operation(summary = "Allows us to create a customer of type company")
    @ApiResponses(value = {
    		@ApiResponse(responseCode = "201", description = "Company resource successfully created."),
    		@ApiResponse(responseCode = "409")
    })
    @PostMapping("/company")
    public ResponseEntity<Customer> createCompany(@RequestBody Company company) {
        Customer createdCustomer = customerService.createCustomer(company);
        if (createdCustomer != null) {
            URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdCustomer.getCustomerId())
                .toUri();
            return ResponseEntity
            		.created(location)
            		.build();
        }
        return ResponseEntity
        		.status(HttpStatus.CONFLICT)
        		.build();
    }

    @Operation(summary = "Grabs a customer by id")
    @ApiResponses(value = {
    		@ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
    		@ApiResponse(responseCode = "404")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable Long id) {
        Customer customer = customerService.getCustomerById(id);
        
        if (customer != null) {
        	return ResponseEntity
    				.status(HttpStatus.OK)
    				.body(customer);
        }
        throw new CustomerNotFoundException("Customer id=" + id + " not found");    
    }

    @Operation(summary = "Grabs all customers")
    @ApiResponses(value = {
    		@ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    })
    @GetMapping
    public List<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }
    
    @Operation(summary = "Update person by id")
    @ApiResponses(value = {
    		@ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
    		@ApiResponse(responseCode = "404")
    })
    @PutMapping("/person/{id}")
    public ResponseEntity<Customer> updatePerson(@PathVariable Long id, @RequestBody Person updatedPersonDetails) {
    	Customer existingCustomer = customerService.getCustomerById(id);
    	
    	if((existingCustomer == null) || (!(existingCustomer instanceof Person))) {
    		throw new CustomerNotFoundException("Person id=" + id + " not found");   
    	}
    	
    	Person existingPerson = (Person) existingCustomer;
    	existingPerson.setName(updatedPersonDetails.getName());
    	existingPerson.getAddress().setPostalCode(updatedPersonDetails.getAddress().getPostalCode());
    	existingPerson.getAddress().setCity(updatedPersonDetails.getAddress().getCity());
    	existingPerson.getAddress().setProvince(updatedPersonDetails.getAddress().getProvince());
    	Customer updatedCustomer = customerService.updateCustomer(existingPerson);
    	
    	if(updatedCustomer != null) {
    		return ResponseEntity.ok(updatedCustomer);    		
    	} else {
    		return ResponseEntity.status(HttpStatus.CONFLICT).build();
    	}
    }
    
    @Operation(summary = "Update company by id")
    @ApiResponses(value = {
    		@ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
    		@ApiResponse(responseCode = "404")
    })
    @PutMapping("/company/{id}")
    public ResponseEntity<Customer> updateCompany(@PathVariable Long id, @RequestBody Company updatedCompanyDetails) {
    	Customer existingCustomer = customerService.getCustomerById(id);
    	
    	if((existingCustomer == null) || (!(existingCustomer instanceof Company))) {
    		throw new CustomerNotFoundException("Company id=" + id + " not found");   
    	}
    	
    	Company existingCompany = (Company) existingCustomer;
    	existingCompany.setName(updatedCompanyDetails.getName());
    	existingCompany.getAddress().setPostalCode(updatedCompanyDetails.getAddress().getPostalCode());
    	existingCompany.getAddress().setCity(updatedCompanyDetails.getAddress().getCity());
    	existingCompany.getAddress().setProvince(updatedCompanyDetails.getAddress().getProvince());
    	Customer updatedCustomer = customerService.updateCustomer(existingCompany);
    	
    	if(updatedCustomer != null) {
    		return ResponseEntity.ok(updatedCustomer);    		
    	} else {
    		return ResponseEntity.status(HttpStatus.CONFLICT).build();
    	}
    }

    @Operation(summary = "Delete customer by id")
    @ApiResponses(value = {
    		@ApiResponse(responseCode = "200"),
    		@ApiResponse(responseCode = "404")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
    	if(customerService.deleteCustomer(id)) {
    		return ResponseEntity
    				.status(HttpStatus.OK)
    				.build();
    	}        
    	throw new CustomerNotFoundException("Customer id=" + id + " not found");   
    }
    
    @Operation(summary = "Allows us to add an account to a customer")
    @ApiResponses(value = {
    		@ApiResponse(responseCode = "201", description = "Person resource successfully created."),
    		@ApiResponse(responseCode = "404")
    })
    @PostMapping("/{id}/accounts")
    public ResponseEntity<Customer> addAccountToCustomer(@PathVariable Long id, @RequestBody Account account) {
        Customer customer = customerService.addAccountToCustomer(id, account);
        if (customer != null) {
            return ResponseEntity.ok(customer);
        }
        throw new CustomerNotFoundException("Customer id=" + id + " not found");   
    }    
}

