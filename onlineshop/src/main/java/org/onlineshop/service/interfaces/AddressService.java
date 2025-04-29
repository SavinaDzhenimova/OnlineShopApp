package org.onlineshop.service.interfaces;

import org.onlineshop.model.entity.Address;

import java.util.Optional;

public interface AddressService {

    void saveAndFlush(Address address);

    void deleteById(Long id);

    Optional<Address> getById(Long id);

}