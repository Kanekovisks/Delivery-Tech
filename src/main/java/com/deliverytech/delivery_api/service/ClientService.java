package com.deliverytech.delivery_api.service;

import org.springframework.stereotype.Service;

import com.deliverytech.delivery_api.repository.ClientRepository;
import com.deliverytech.delivery_api.model.Client;

import java.util.List;

@Service

public class ClientService {
    private ClientRepository repository;

    public ClientService(ClientRepository repository) {
        this.repository = repository;
    }

    public Client registerClient(Client client) {
        if (repository.existsByEmail(client.getEmail())) {
            throw new IllegalArgumentException("E-mail já cadastrado");
        }

        client.setAtivo(true);

        return repository.save(client);
    }

    public List<Client> listActives() {
        return repository.findByAtivoTrue();
    }
}
