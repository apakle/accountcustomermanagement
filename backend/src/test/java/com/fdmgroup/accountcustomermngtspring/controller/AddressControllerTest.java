package com.fdmgroup.accountcustomermngtspring.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import com.fdmgroup.accountcustomermngtspring.service.AddressService;

@WebMvcTest(AddressController.class)
@ExtendWith(MockitoExtension.class)
class AddressControllerTest {

	@Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    AddressService mockAddressService;
    
    @Test
    void test_updateAddress_returns_statusOk_andUpdatedAddress() throws Exception {
        // Setup test data
        long id = 1;
        Address existingAddress = new Address("2 QUEEN STREET", "R43 6Y6", "CALEDON", "ONTARIO");
        existingAddress.setAddressId(id);

        Address updatedAddressDetails = new Address("updated street", "updated postal code", "updated city", "updated province");
        updatedAddressDetails.setAddressId(id);

        Address updatedAddress = new Address("updated street", "updated postal code", "updated city", "updated province");
        updatedAddress.setAddressId(id);

        // Prepare the URL and expected JSON response
        String url = "/api/v1/addresses/" + id;
        String updatedAddressJsonString = objectMapper.writeValueAsString(updatedAddressDetails);
        String responseAddressJsonString = objectMapper.writeValueAsString(updatedAddress);

        // Mock the service calls
        when(mockAddressService.getAddressById(id)).thenReturn(existingAddress);
        when(mockAddressService.updateAddress(existingAddress)).thenReturn(updatedAddress);

        // Perform the request and verify the response
        mockMvc.perform(MockMvcRequestBuilders.put(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(updatedAddressJsonString))
            .andExpectAll(MockMvcResultMatchers.status().isOk(),
                MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON_VALUE),
                MockMvcResultMatchers.content().json(responseAddressJsonString))
            .andDo(MockMvcResultHandlers.print());

        // Verify the service methods were called
        verify(mockAddressService).getAddressById(id);
        verify(mockAddressService).updateAddress(existingAddress);
    }
}
