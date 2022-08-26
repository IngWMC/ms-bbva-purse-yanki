package com.nttdata.bbva.purseyanki.repositories.impl;

import com.nttdata.bbva.purseyanki.models.Customer;
import com.nttdata.bbva.purseyanki.repositories.ICustomerRepository;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.UUID;

@Repository
public class CustomerRepositoryImpl implements ICustomerRepository {
    private static final String KEY = "customer";
    private RedisTemplate<String, Customer> redisTemplate;
    private HashOperations hashOperations;

    public CustomerRepositoryImpl(RedisTemplate<String, Customer> redisTemplate){
        this.redisTemplate = redisTemplate;
    }

    @PostConstruct
    private void init(){
        hashOperations = redisTemplate.opsForHash();
    }

    @Override
    public Map<String, Customer> findAll() {
        return hashOperations.entries(KEY);
    }

    @Override
    public Customer findbyId(String id) {
        return (Customer)hashOperations.get(KEY, id);
    }

    @Override
    public void save(Customer obj) {
        hashOperations.put(KEY, UUID.randomUUID().toString(), obj);
    }

    @Override
    public void delete(String id) {
        hashOperations.delete(KEY, UUID.randomUUID().toString(), id);
    }
}
