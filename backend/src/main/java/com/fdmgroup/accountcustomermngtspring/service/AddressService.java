package com.fdmgroup.accountcustomermngtspring.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fdmgroup.accountcustomermngtspring.model.Address;
import com.fdmgroup.accountcustomermngtspring.repository.AddressRepository;

@Service
public class AddressService {
	private AddressRepository addressRepo;
	
	@Autowired
	public AddressService(AddressRepository addressRepo) {
		this.addressRepo = addressRepo;
	}
	
	public Address addAddress(Address address) {
		return addressRepo.save(address);
	}
	
	public Address getAddressById(Long id) {
		Optional<Address> optAddress = addressRepo.findById(id);
		
		if(optAddress.isPresent()) {
			return optAddress.get();
		}
		return null;
	}
	
	public List<Address> getAllAddresses(){
		return addressRepo.findAll();
	}

	public Address updateAddress(Address address) {
		if(addressRepo.existsById(address.getAddressId())) {
			addressRepo.save(address);
			return address;
		}
		return null;
	}
	
	public boolean deleteAddress(Long id) {
        if (addressRepo.existsById(id)) {
            addressRepo.deleteById(id);
            return true;
        }
        return false;
    }
	

}
