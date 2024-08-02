package com.fdmgroup.accountcustomermngtspring.controller;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fdmgroup.accountcustomermngtspring.exception.AddressNotFoundException;
import com.fdmgroup.accountcustomermngtspring.model.Address;
import com.fdmgroup.accountcustomermngtspring.service.AddressService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/v1/addresses")
public class AddressController {
	private AddressService addressService;
	
	@Autowired
	public AddressController(AddressService addressService) {
		this.addressService = addressService;
	}

	@Operation(summary = "Allows us to create a address")
    @ApiResponses(value = {
    		@ApiResponse(responseCode = "201", description = "Address resource successfully created."),
    		@ApiResponse(responseCode = "409")
    })
	@PostMapping
	public ResponseEntity<Address> addNewAddress(@RequestBody Address address) {
		Address createAddress = addressService.addAddress(address);		
		
		if(createAddress != null) {
    		URI location = ServletUriComponentsBuilder
    				.fromCurrentRequest()
    				.path("/{id}")
    				.buildAndExpand(createAddress.getAddressId())
    				.toUri();
    		return ResponseEntity
    				.created(location)
    				.build();
    	}
    	return ResponseEntity
    			.status(HttpStatus.CONFLICT)
    			.build();
    }
	
	
	@Operation(summary = "Grabs all addresses")
    @ApiResponses(value = {
    		@ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE))
    })
	@GetMapping
	public List<Address> getAllAddresses() {
		return addressService.getAllAddresses();
	}
	
	@Operation(summary = "Update address by id")
    @ApiResponses(value = {
    		@ApiResponse(responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)),
    		@ApiResponse(responseCode = "404")
    })
	@PutMapping("/{id}")
	public ResponseEntity<Address> updateAddress(@PathVariable Long id, @RequestBody Address updatedAddressDetails){
		Address existingAddress = addressService.getAddressById(id);
		
		if(existingAddress == null) {
			throw new AddressNotFoundException("Address id=" + id + " not found");
		}
		
		if (existingAddress != null) {
			existingAddress.setPostalCode(updatedAddressDetails.getPostalCode());
			existingAddress.setCity(updatedAddressDetails.getCity());
			existingAddress.setProvince(updatedAddressDetails.getProvince());			
		}
		
		Address updatedAddress = addressService.updateAddress(existingAddress);
		
		if(updatedAddress != null) {
			return ResponseEntity.ok(updatedAddress);
		} else {
    		return ResponseEntity.status(HttpStatus.CONFLICT).build(); 
		}		
	}
	
	@Operation(summary = "Delete address by id")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200"),
        @ApiResponse(responseCode = "404"), 
        @ApiResponse(responseCode = "409", description = "Foreign key constraint violation")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAddress(@PathVariable Long id) {
        try {
            if (addressService.deleteAddress(id)) {
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .build();
            }
            throw new AddressNotFoundException("Address id=" + id + " not found");
        } catch (DataIntegrityViolationException ex) {
        	return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Foreign key constraint violation");
        }
    }
	

}
