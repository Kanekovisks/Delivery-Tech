package com.deliverytech.delivery_api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

    @PostMapping("/register")
    public ResponseEntity<Client> registerClient(@RequestBody Client client) {
        return ResponseEntity.status(201).body(service.registerClient(client));
    }

    @GetMapping
    public List<Client> listActives(){
        return service.listActives();
    }

    @GetMapping("/{id}")
    public Client searchByID(@PathVariable Long id) {
        return service.searchByID(id);
    }

    @PutMapping("/{id}/deactivate")
    public void deactivate(@PathVariable Long id) {
        service.deactivateId(id);
    }

    @PutMapping("/{id}/update")
    public Client updateInfo(@PathVariable Long id, @RequestBody Client info) {
        return service.updateInfo(id, info);
    }
}
