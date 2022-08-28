package com.nttdata.bbva.purseyanki.clients;

import com.nttdata.bbva.purseyanki.exceptions.ExceptionResponse;
import com.nttdata.bbva.purseyanki.exceptions.ModelNotFoundException;
import com.nttdata.bbva.purseyanki.models.OpenAccount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class OpenAccountClient {
    private static final Logger logger = LoggerFactory.getLogger(OpenAccountClient.class);
    private final WebClient webClient;

    public OpenAccountClient(WebClient.Builder webClientBuilder){
        this.webClient = webClientBuilder.baseUrl("http://localhost:7073/api/1.0.0/openaccounts").build();
    }

    public Mono<OpenAccount> findById(String id){
        logger.info("Inicio OpenAccountClient ::: findAll");
        return this.webClient.get()
                .uri("/{id}", id)
                .retrieve()
//                .onStatus(HttpStatus.NOT_FOUND::equals,
//                        response -> response
//                                .bodyToMono(ExceptionResponse.class)
//                                .flatMap(error -> Mono.error(new ModelNotFoundException(error.getMensaje()))))
                .bodyToMono(OpenAccount.class)
                .doOnNext(x -> logger.info("Fin OpenAccountClient ::: findAll"));
    }

}
