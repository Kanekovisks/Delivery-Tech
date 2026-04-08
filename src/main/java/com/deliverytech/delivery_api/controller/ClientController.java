package com.deliverytech.delivery_api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deliverytech.delivery_api.service.ClientService;

import jakarta.validation.Valid;

import com.deliverytech.delivery_api.dto.requests.ClientDTO;
import com.deliverytech.delivery_api.dto.responses.ClientResponseDTO;

import java.util.List;

@RestController
@RequestMapping("/clients")

public class ClientController {
    private ClientService service;

    public ClientController(ClientService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public ResponseEntity<ClientResponseDTO> registerClient(@Valid @RequestBody ClientDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.registerClient(dto));
    }

    @GetMapping
    public List<ClientResponseDTO> listActives(){
        return service.listActives();
    }

    @GetMapping("/{id}")
    public ClientResponseDTO searchByID(@PathVariable Long id) {
        return service.searchByID(id);
    }

    @PutMapping("/{id}/deactivate")
    public ClientResponseDTO deactivate(@PathVariable Long id) {
        return service.deactivateId(id);
    }
        
/*
    @PutMapping("/{id}/update")
    public ClientResponseDTO updateInfo(@PathVariable Long id, @RequestBody Client info) {
        return service.updateInfo(id, info);
    }
    */
}
