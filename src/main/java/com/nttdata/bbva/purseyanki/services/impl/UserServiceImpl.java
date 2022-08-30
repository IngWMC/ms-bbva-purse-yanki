package com.nttdata.bbva.purseyanki.services.impl;

import com.nttdata.bbva.purseyanki.clients.CustomerClient;
import com.nttdata.bbva.purseyanki.clients.OpenAccountClient;
import com.nttdata.bbva.purseyanki.clients.ProductClient;
import com.nttdata.bbva.purseyanki.exceptions.BadRequestException;
import com.nttdata.bbva.purseyanki.models.Event;
import com.nttdata.bbva.purseyanki.models.EventOperation;
import com.nttdata.bbva.purseyanki.models.User;
import com.nttdata.bbva.purseyanki.repositories.IUserRepository;
import com.nttdata.bbva.purseyanki.services.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Service
public class UserServiceImpl implements IUserService {
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    private IUserRepository repo;
    @Autowired
    private OpenAccountClient openAccountClient;
    @Autowired
    private CustomerClient customerClient;
    @Autowired
    private ProductClient productClient;
    @Autowired
    private KafkaTemplate<String, Event> produder;
    private final String topicOperationPurseYanki = "operationPurseYanki";
    @Override
    public Mono<User> insert(User obj) {
        if (obj.getId() == null || obj.getId().isEmpty()) {
            String userId = UUID.randomUUID().toString().replaceAll("-", "");
            obj.setId(userId);
        }

        return this.addOrUpdateUser(obj);
    }

    @Override
    public Mono<Event> insertOperation(EventOperation operation) {
        return repo.findByCellPhoneNumber(operation.getCellPhoneNumber())
                .switchIfEmpty(Mono.error(() -> new BadRequestException("El número de celular no está registrado.")))
                .flatMap(userOrigin -> repo.findByCellPhoneNumber(operation.getCellPhoneNumberTransfer())
                        .switchIfEmpty(Mono.error(() -> new BadRequestException("El número de celular a transferir no está registrado.")))
                        .flatMap(userTransfer -> {
                            if (userOrigin.getCellPhoneNumber().equals(userTransfer.getCellPhoneNumber()))
                                return Mono.error(() -> new BadRequestException("El usuario no puede realizarse una transferencia."));

                            return openAccountClient.findById(userOrigin.getBankAccountNumber())
                                    .flatMap(openAccount -> {
                                        BigDecimal totalAmountAvailable = openAccount.getAmountAvailable().subtract(operation.getAmount());
                                        if (totalAmountAvailable.compareTo(new BigDecimal(0)) < 0) // Si totalAmountAvailable es menor que CERO.
                                            return Mono.error(() -> new BadRequestException("El usuario no cuenta con saldo suficiente para realizar la operación."));

                                        operation.setOpenAccountId(userOrigin.getBankAccountNumber());
                                        operation.setOpenAccountIdTransfer(userTransfer.getBankAccountNumber());
                                        operation.setOperationType("T");
                                        Event event = Event.builder()
                                                .id(UUID.randomUUID().toString())
                                                .date(new Date())
                                                .operation(operation)
                                                .build();

                                        this.produder.send(topicOperationPurseYanki, event);
                                        return Mono.just(event);
                                    });
                        })
                );
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

                    return repo.existsByCellPhoneNumber(obj.getCellPhoneNumber())
                            .flatMap(existsCellPhoneNumber -> {
                                if (existsCellPhoneNumber)
                                    return Mono.error(() -> new BadRequestException("El campo CellPhoneNumber no es válido."));

                                return repo.existsByEmailAddress(obj.getEmailAddress())
                                        .flatMap(existsEmailAddress -> {
                                            if (existsEmailAddress)
                                                return Mono.error(() -> new BadRequestException("El campo EmailAddress no es válido."));

                                            return openAccountClient.findById(obj.getBankAccountNumber())
                                                    .flatMap(openAccount -> customerClient.findById(openAccount.getCustomerId())
                                                            .flatMap(customer -> {
                                                                if (customer.getIdentificationDocument().equals(obj.getIdentificationDocument()))
                                                                    return this.existsProductCUEAById(openAccount.getProductId())
                                                                            .flatMap(existsProductCUEA -> {
                                                                                if (existsProductCUEA)
                                                                                    return repo.save(obj).map(isSaved -> obj);
                                                                                else
                                                                                    return Mono.error(() -> new BadRequestException("El número de cuenta no es una CUENTA DE AHORRO."));
                                                                            });
                                                                else
                                                                    return Mono.error(() -> new BadRequestException("El usuario no es cliente del banco."));
                                                            })
                                                    );
                                        });
                            });
                })
                .thenReturn(obj);
    }
    private Mono<Boolean> existsProductCUEAById(String id) {
        return productClient.findById(id)
                .filter(product -> product.getShortName().equals("CUEA"))
                .hasElement();
    }
}
