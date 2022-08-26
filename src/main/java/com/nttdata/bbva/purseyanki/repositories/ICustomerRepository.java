package com.nttdata.bbva.purseyanki.repositories;

import com.nttdata.bbva.purseyanki.models.Customer;
import org.springframework.stereotype.Repository;

import java.util.Map;

public interface ICustomerRepository {
    Map<String, Customer> findAll();
    Customer findbyId(String id);
    void save(Customer obj);
    void delete(String id);
}
