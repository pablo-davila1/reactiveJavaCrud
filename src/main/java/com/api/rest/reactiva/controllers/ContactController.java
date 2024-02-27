package com.api.rest.reactiva.controllers;

import com.api.rest.reactiva.entities.Contact;
import com.api.rest.reactiva.repositories.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ContactController {
    @Autowired
    private ContactRepository contactRepository;

    @GetMapping("/contact")
    public List<Contact> ListContacts () {
        Flux<Contact> contactFlux = contactRepository.findAll();
        List<Contact> contactList = new ArrayList<>();

        contactFlux
                .map(contact -> contactList.add(contact));

        return contactList;
    }

    @GetMapping("/id/{id}")
    public Mono< ResponseEntity<Contact> > GetById (@PathVariable(name = "id")String id){
        Mono<Contact> contactIdMono = this.contactRepository.findById(id);
        return contactIdMono
                .map(contact -> new ResponseEntity<>(contact, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    @GetMapping("/email/{email}")
    public Mono< ResponseEntity<Contact> > GetByEmail (@PathVariable(name = "email")String email){
        Mono<Contact> contactMono = this.contactRepository.findFirstByEmail(email);
        return contactMono
                .map(contact -> new ResponseEntity<>(contact, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    @GetMapping("/tel_name/{tel_name}")
    public Mono< ResponseEntity<Contact> > GetByNameOrPhone (@PathVariable(name = "tel_name")String tel_name){
        Mono<Contact> contactMonoTel = this.contactRepository.findAlltByTelefonoOrNombre(tel_name);
        return contactMonoTel
                .map(contact -> new ResponseEntity<>(contact, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/save")
    public Mono< ResponseEntity<Contact> > SaveContatc (@RequestBody Contact contact){
        return this.contactRepository.insert(contact)
                .map(contact1 -> new ResponseEntity<>(contact1, HttpStatus.OK))
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE));
    }

    @PutMapping("/update/{id}")
    public Mono< ResponseEntity<Contact> > UpdateContact(@PathVariable(name = "id")String id, @RequestBody Contact contact){
        Mono<Contact> contactMono = this.contactRepository.findById(id);
        return contactMono.flatMap(cobtactoActualizado -> {
                    contact.setId(cobtactoActualizado.getId());
                     return this.contactRepository.save(contact)
                             .map( c -> new ResponseEntity<>(c, HttpStatus.OK) );
                })
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    @DeleteMapping("/delete/{id}")
    public Mono<Void> DeleteContact (@PathVariable(name = "id")String id){
        return this.contactRepository.deleteById(id);
    }


}
