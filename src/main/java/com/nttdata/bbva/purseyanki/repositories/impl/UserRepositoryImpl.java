package com.nttdata.bbva.purseyanki.repositories.impl;

import com.nttdata.bbva.purseyanki.models.User;
import com.nttdata.bbva.purseyanki.repositories.IUserRepository;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveHashOperations;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class UserRepositoryImpl implements IUserRepository {
    private static final Logger logger = LoggerFactory.getLogger(UserRepositoryImpl.class);
    private static final String KEY = "USER";
    private final ReactiveRedisOperations<String, User> redisOperations;
    private final ReactiveHashOperations<String, String, User> hashOperations;

    @Autowired
    public UserRepositoryImpl(ReactiveRedisOperations<String, User> redisOperations) {
        this.redisOperations = redisOperations;
        this.hashOperations = redisOperations.opsForHash();
    }

    @Override
    public Flux<User> findAll() {
        return hashOperations.values(KEY);
    }

    @Override
    public Mono<User> findById(String id) {
        return hashOperations.get(KEY, id);
    }

    @Override
    public Mono<User> save(User obj) {
        return hashOperations.put(KEY, obj.getId(), obj).map(isSaved -> obj);
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return hashOperations.remove(KEY, id).then();
    }

    @Override
    public Mono<User> findByIdentificationDocument(String identificationDocument) {
        return hashOperations.values(KEY)
                .filter(u -> u.getIdentificationDocument().equals(identificationDocument))
                .singleOrEmpty();
    }

    @Override
    public Mono<User> findByEmailAddress(String emailAddress) {
        return hashOperations.values(KEY)
                .filter(u -> u.getEmailAddress().equals(emailAddress))
                .singleOrEmpty();
    }

    @Override
    public Mono<User> findByCellPhoneNumber(String cellPhoneNumber) {
        return hashOperations.values(KEY)
                .filter(u -> u.getCellPhoneNumber().equals(cellPhoneNumber))
                .singleOrEmpty();
    }

    @Override
    public Mono<Boolean> existsByIdentificationDocument(String identificationDocument) {
        logger.info("Inicio UserRepositoryImpl ::: existsByIdentificationDocument ::: " + identificationDocument);
        return this.findByIdentificationDocument(identificationDocument)
                .hasElement()
                .doOnNext(exists -> logger.info("Fin UserRepositoryImpl ::: existsByIdentificationDocument ::: " + exists));
    }

    @Override
    public Mono<Boolean> existsByEmailAddress(String emailAddress) {
        logger.info("Inicio UserRepositoryImpl ::: existsByEmailAddress ::: " + emailAddress);
        return this.findByEmailAddress(emailAddress)
                .hasElement()
                .doOnNext(exists -> logger.info("Fin UserRepositoryImpl ::: existsByEmailAddress ::: " + exists));
    }

    @Override
    public Mono<Boolean> existsByCellPhoneNumber(String cellPhoneNumber) {
        logger.info("Inicio UserRepositoryImpl ::: existsByCellPhoneNumber ::: " + cellPhoneNumber);
        return this.findByCellPhoneNumber(cellPhoneNumber)
                .hasElement()
                .doOnNext(exists -> logger.info("Fin UserRepositoryImpl ::: existsByCellPhoneNumber ::: " + exists));
    }

    @Override
    public Flux<User> findAllById(Iterable<String> strings) {
        return null;
    }
    @Override
    public Flux<User> findAllById(Publisher<String> idStream) {
        return null;
    }
    @Override
    public Mono<Long> count() {
        return null;
    }
    @Override
    public Mono<Void> deleteById(Publisher<String> id) {
        return null;
    }
    @Override
    public Mono<Void> delete(User entity) {
        return null;
    }
    @Override
    public Mono<Void> deleteAllById(Iterable<? extends String> strings) {
        return null;
    }
    @Override
    public Mono<Void> deleteAll(Iterable<? extends User> entities) {
        return null;
    }
    @Override
    public Mono<Void> deleteAll(Publisher<? extends User> entityStream) {
        return null;
    }
    @Override
    public Mono<Void> deleteAll() {
        return null;
    }
    @Override
    public <S extends User> Flux<S> saveAll(Iterable<S> entities) {
        return null;
    }
    @Override
    public <S extends User> Flux<S> saveAll(Publisher<S> entityStream) {
        return null;
    }
    @Override
    public Mono<User> findById(Publisher<String> id) {
        return null;
    }
    @Override
    public Mono<Boolean> existsById(String s) {
        return null;
    }
    @Override
    public Mono<Boolean> existsById(Publisher<String> id) {
        return null;
    }
}
