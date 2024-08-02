package com.fdmgroup.accountcustomermngtspring.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fdmgroup.accountcustomermngtspring.model.Address;
import com.fdmgroup.accountcustomermngtspring.repository.AddressRepository;

@ExtendWith(MockitoExtension.class)
class AddressServiceTest {

    AddressService addressService;
    
	@Mock
    AddressRepository mockAddressRepo;

	@BeforeEach
    void setUp() throws Exception {
        addressService = new AddressService(mockAddressRepo);
    }

	@Test
    void test_updateAddress_updatesAddress_inAddressRepo() {
        // Setup test data
		long id = 1;
        Address existingAddress = new Address("2 QUEEN STREET", "R43 6Y6", "CALEDON", "ONTARIO");
        existingAddress.setAddressId(id);

        Address updatedAddress = new Address("updated street", "updated postal code", "updated city", "updated province");
        updatedAddress.setAddressId(id);
        
        // Update fields of existingAddress to match updatedAddressDetails
        existingAddress.setStreetNumber(updatedAddress.getStreetNumber());
        existingAddress.setCity(updatedAddress.getCity());
        existingAddress.setPostalCode(updatedAddress.getPostalCode());
        existingAddress.setProvince(updatedAddress.getProvince());

        // Mock the repository calls
        when(mockAddressRepo.existsById(existingAddress.getAddressId())).thenReturn(true);
        when(mockAddressRepo.save(existingAddress)).thenReturn(updatedAddress);

        // Act
        Address resultAddress = addressService.updateAddress(existingAddress);
        
        // Assert
        assertEquals(updatedAddress.getStreetNumber(), resultAddress.getStreetNumber());
        assertEquals(updatedAddress.getPostalCode(), resultAddress.getPostalCode());
        assertEquals(updatedAddress.getCity(), resultAddress.getCity());
        assertEquals(updatedAddress.getProvince(), resultAddress.getProvince());
    }

}
