package com.nttdata.bbva.purseyanki.clients;

import com.nttdata.bbva.purseyanki.models.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class CustomerClient {
    private static final Logger logger = LoggerFactory.getLogger(CustomerClient.class);
    private final WebClient webClient;

    public CustomerClient(WebClient.Builder webClientBuilder){
        this.webClient = webClientBuilder.baseUrl("http://localhost:7071/api/1.0.0/customers").build();
    }

    public Mono<Customer> findById(String id){
        logger.info("Inicio CustomerClient ::: findById ::: " + id);
        return this.webClient.get()
                .uri("/{id}", id)
                .retrieve()
                .bodyToMono(Customer.class)
                .doOnNext(x -> logger.info("Fin CustomerClient ::: findById"));
    }

}
