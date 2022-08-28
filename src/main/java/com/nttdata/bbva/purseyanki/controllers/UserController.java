package com.nttdata.bbva.purseyanki.controllers;

import com.nttdata.bbva.purseyanki.models.User;
import com.nttdata.bbva.purseyanki.services.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping("api/1.0.0/users")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private IUserService service;

    @GetMapping
    public Mono<ResponseEntity<Flux<User>>> findAll(){
        logger.info("Inicio CustomerController ::: findAll");
        Flux<User> customers = service.findAll().doOnNext(x -> logger.info("Fin CustomerController ::: findAll"));
        return Mono.just(ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(customers));
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Mono<User>>> findById(@PathVariable("id") String id){
        logger.info("Inicio CustomerController ::: findById ::: " + id);
        Mono<User> customer = service.findById(id).doOnNext(x -> logger.info("Fin CustomerController ::: findById"));
        return Mono.just(ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(customer));
    }

    @PostMapping
    public Mono<ResponseEntity<Mono<User>>> insert(@Valid @RequestBody User obj){
        logger.info("Inicio CustomerController ::: insert ::: " + obj);
        Mono<User> customer = service.insert(obj).doOnNext(x -> logger.info("Fin CustomerController ::: insert"));
        return Mono.just(new ResponseEntity<Mono<User>>(customer, HttpStatus.CREATED));
    }

    @PutMapping
    public Mono<ResponseEntity<Mono<User>>> update(@Valid @RequestBody User obj){
        logger.info("Inicio CustomerController ::: update ::: " + obj);
        Mono<User> customer = service.update(obj).doOnNext(x -> logger.info("Fin CustomerController ::: update"));
        return Mono.just(new ResponseEntity<Mono<User>>(customer, HttpStatus.CREATED));
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable("id") String id) {
        logger.info("Inicio CustomerController ::: delete ::: " + id);
        service.delete(id).doOnNext(x -> logger.info("Fin CustomerController ::: delete"));
        return Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT));
    }

}
