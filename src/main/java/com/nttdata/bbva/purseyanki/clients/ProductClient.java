package com.nttdata.bbva.purseyanki.clients;

import com.nttdata.bbva.purseyanki.models.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class ProductClient {
    private static final Logger logger = LoggerFactory.getLogger(ProductClient.class);
    private final WebClient webClient;

    public ProductClient(WebClient.Builder webClientBuilder){
        this.webClient = webClientBuilder.baseUrl("http://localhost:7072/api/1.0.0/products").build();
    }

    public Mono<Product> findById(String id){
        logger.info("Inicio ProductClient ::: findById ::: " + id);
        return this.webClient.get()
                .uri("/{id}", id)
                .retrieve()
                .bodyToMono(Product.class)
                .doOnNext(x -> logger.info("Fin ProductClient ::: findById"));
    }

}
