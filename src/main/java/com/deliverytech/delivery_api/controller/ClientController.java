package com.deliverytech.delivery_api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deliverytech.delivery_api.service.ClientService;
import com.deliverytech.delivery_api.model.Client;

import java.util.List;

@RestController
@RequestMapping("/clientes")

public class ClientController {
    private ClientService service;

    public ClientController(ClientService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Client> registerClient(@RequestBody Client client) {
        return ResponseEntity.status(201).body(service.registerClient(client));
    }

    @GetMapping
    public List<Client> listActives(){
        return service.listActives();
    }
}
