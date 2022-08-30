package com.nttdata.bbva.purseyanki.repositories;

import com.nttdata.bbva.purseyanki.models.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface IUserRepository extends ReactiveCrudRepository<User, String> {
    Mono<User> findByIdentificationDocument(String identificationDocument);
    Mono<User> findByEmailAddress(String name);
    Mono<User> findByCellPhoneNumber(String cellPhoneNumber);
    Mono<Boolean> existsByIdentificationDocument(String identificationDocument);
    Mono<Boolean> existsByEmailAddress(String email);
    Mono<Boolean> existsByCellPhoneNumber(String cellPhoneNumber);
}
