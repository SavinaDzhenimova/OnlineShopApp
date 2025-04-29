package org.onlineshop.service;

import org.onlineshop.model.entity.Address;
import org.onlineshop.repository.AddressRepository;
import org.onlineshop.service.interfaces.AddressService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;

    public AddressServiceImpl(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Override
    public void saveAndFlush(Address address) {
        this.addressRepository.saveAndFlush(address);
    }

    @Override
    public void deleteById(Long id) {
        this.addressRepository.deleteById(id);
    }

    @Override
    public Optional<Address> getById(Long id) {
        return this.addressRepository.findById(id);
    }
}