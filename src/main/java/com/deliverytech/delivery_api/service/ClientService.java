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
            throw new IllegalArgumentException("E-mail já cadastrado.");
        }

        client.setAtivo(true);

        return repository.save(client);
    }

    public List<Client> listActives() {
        return repository.findByAtivoTrue();
    }

    public Client searchByID(Long id) {
        return repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado."));
    }

    public void deactivateId(Long id) {
        Client client = searchByID(id);
        client.setAtivo(false);
        repository.save(client);
    }

    public Client updateInfo(Long id, Client info) {
        Client client = searchByID(id);
        client.setNome(info.getNome());
        client.setEmail(info.getEmail());
        client.setTelefone(info.getTelefone());
        client.setEndereco(info.getEndereco());
        return repository.save(client);
    }
}
