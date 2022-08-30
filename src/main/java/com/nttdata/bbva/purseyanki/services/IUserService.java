package com.nttdata.bbva.purseyanki.services;

import com.nttdata.bbva.purseyanki.models.Event;
import com.nttdata.bbva.purseyanki.models.EventOperation;
import com.nttdata.bbva.purseyanki.models.User;
import reactor.core.publisher.Mono;

public interface IUserService extends ICRUD<User, String> {
    Mono<Event> insertOperation(EventOperation operation);
}
