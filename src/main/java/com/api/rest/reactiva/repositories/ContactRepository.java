package com.api.rest.reactiva.repositories;

import com.api.rest.reactiva.entities.Contact;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

public interface ContactRepository extends ReactiveMongoRepository<Contact,String> {
    Mono<Contact> findFirstByEmail(String email);
    Mono<Contact> findAlltByTelefonoOrNombre(String email);
}
