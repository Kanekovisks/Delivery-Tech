package com.deliverytech.delivery_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.deliverytech.delivery_api.repository.ClientRepository;
import com.deliverytech.delivery_api.model.Client;

import java.util.List;

@Service

public class ClientService {

    @Autowired
    private ClientRepository repository;

    public ClientService(ClientRepository repository) {
        this.repository = repository;
    }

    public Client registerClient(Client client) {
        if (repository.existsByEmail(client.getEmail())) {
            throw new IllegalArgumentException("E-mail já cadastrado.");
        }

        client.setActive(true);

        return repository.save(client);
    }

    public List<Client> listActives() {
        return repository.findByActiveTrue();
    }

    public Client searchByID(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID não pode ser nulo.");
        }
        return repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado."));
    }

    public void deactivateId(Long id) {
        Client client = searchByID(id);
        client.setActive(false);
        repository.save(client);
    }

    public Client updateInfo(Long id, Client info) {
        Client client = searchByID(id);
        client.setName(info.getName());
        client.setEmail(info.getEmail());
        client.setPhone(info.getPhone());
        client.setAddress(info.getAddress());
        return repository.save(client);
    }
}
