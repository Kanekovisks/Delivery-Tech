package com.deliverytech.delivery_api.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;


import com.deliverytech.delivery_api.repository.ClientRepository;

import jakarta.transaction.Transactional;

import com.deliverytech.delivery_api.dto.requests.ClientDTO;
import com.deliverytech.delivery_api.dto.responses.ClientResponseDTO;
import com.deliverytech.delivery_api.exception.BusinessException;
import com.deliverytech.delivery_api.exception.EntityNotFoundException;
import com.deliverytech.delivery_api.model.Client;

@Service
public class ClientService implements IClientService {

    private final ClientRepository repository;

    private final ModelMapper modelMapper;

    public ClientService(ClientRepository repository, ModelMapper modelMapper) {
        this.repository = repository;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public ClientResponseDTO registerClient(ClientDTO dto) {
        if (repository.existsByEmail(dto.getEmail())) {
            throw new BusinessException("E-mail já cadastrado.");
        }

        Client client = modelMapper.map(dto, Client.class);
        client.setActive(true);
        Client save = repository.save(client);

        return modelMapper.map(save, ClientResponseDTO.class);
    }

    @Override
    public ClientResponseDTO findClientById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID não pode ser nulo.");
        }
        Client client = repository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado."));
    
        return modelMapper.map(client, ClientResponseDTO.class);
    }

    @Override
    public ClientResponseDTO findClientByEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("E-mail não pode ser nulo ou vazio.");
        }
        Client client = repository.findByEmail(email)
        .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado."));
    
        return modelMapper.map(client, ClientResponseDTO.class);
    }

    @Override
    @Transactional
    public ClientResponseDTO updateClient(Long id, ClientDTO dto) {
        if (id == null) {
            throw new IllegalArgumentException("ID não pode ser nulo.");
        }
        Client client = repository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado."));
        client.setName(dto.getName());
        client.setEmail(dto.getEmail());
        client.setPhone(dto.getPhone());
        client.setAddress(dto.getAddress());
        Client save = repository.save(client);
        return modelMapper.map(save, ClientResponseDTO.class);
    }

    @Override
    @Transactional
    public ClientResponseDTO updateClientStatus(Long id, Boolean active) {
        if (id == null) {
            throw new IllegalArgumentException("ID não pode ser nulo.");
        }
        if (active == null) {
            throw new IllegalArgumentException("O campo active não pode ser nulo.");
        }
        Client client = repository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado."));
        client.setActive(active);
        Client saved = repository.save(client);
        return modelMapper.map(saved, ClientResponseDTO.class);
    }

    @Override
    public Page<ClientResponseDTO> listActiveClients(Pageable pageable) {
        return repository.findByActiveTrue(pageable)
        .map(client -> modelMapper.map(client, ClientResponseDTO.class));
    }
}
