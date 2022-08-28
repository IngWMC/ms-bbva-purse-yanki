package com.nttdata.bbva.purseyanki.services.impl;

import com.nttdata.bbva.purseyanki.clients.CustomerClient;
import com.nttdata.bbva.purseyanki.clients.OpenAccountClient;
import com.nttdata.bbva.purseyanki.exceptions.BadRequestException;
import com.nttdata.bbva.purseyanki.models.User;
import com.nttdata.bbva.purseyanki.repositories.IUserRepository;
import com.nttdata.bbva.purseyanki.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    private IUserRepository repo;
    @Autowired
    private OpenAccountClient openAccountClient;
    @Autowired
    private CustomerClient customerClient;

    @Override
    public Mono<User> insert(User obj) {
        if (obj.getId() == null || obj.getId().isEmpty()) {
            String userId = UUID.randomUUID().toString().replaceAll("-", "");
            obj.setId(userId);
        }

        return this.addOrUpdateUser(obj);
    }

    @Override
    public Mono<User> update(User obj) {
        if (obj.getId() == null || obj.getId().isEmpty())
            return Mono.error(() -> new BadRequestException("El campo id es requerido."));
        return this.insert(obj);
    }

    @Override
    public Flux<User> findAll() {
        return repo.findAll();
    }

    @Override
    public Mono<User> findById(String id) {
        return repo.findById(id);
    }

    @Override
    public Mono<Void> delete(String id) {
        return repo.deleteById(id);
    }

    private Mono<User> addOrUpdateUser(User obj) {
        return repo.existsByIdentificationDocument(obj.getIdentificationDocument())
                .flatMap(existsIdentificationDocument -> {
                    if (existsIdentificationDocument)
                        return Mono.error(() -> new BadRequestException("El campo IdentificationDocument no es válido."));

                    return repo.existsByEmailAddress(obj.getEmailAddress())
                            .flatMap(existsEmailAddress -> {
                                if (existsEmailAddress)
                                    return Mono.error(() -> new BadRequestException("El campo EmailAddress no es válido."));

                                return openAccountClient.findById(obj.getBankAccountNumber())
                                        .flatMap(openAccount -> customerClient.findById(openAccount.getCustomerId())
                                                .flatMap(customer -> {
                                                    if (customer.getIdentificationDocument().equals(obj.getIdentificationDocument()))
                                                        return repo.save(obj).map(isSaved -> obj);
                                                    else
                                                        return Mono.error(() -> new BadRequestException("El usuario no es cliente del banco."));
                                                })
                                        );
                            });
                })
                .thenReturn(obj);
    }
}
