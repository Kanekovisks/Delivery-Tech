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
public class ClientService {

    private final ClientRepository repository;

    private final ModelMapper modelMapper;

    public ClientService(ClientRepository repository, ModelMapper modelMapper) {
        this.repository = repository;
        this.modelMapper = modelMapper;
    }

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

    public Page<ClientResponseDTO> listActives(Pageable pageable) {
        return repository.findByActiveTrue(pageable)
        .map(client -> modelMapper.map(client, ClientResponseDTO.class));
    }

    public ClientResponseDTO searchByID(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID não pode ser nulo.");
        }
        Client client = repository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado."));
    
        return modelMapper.map(client, ClientResponseDTO.class);
    }

    @Transactional
    public ClientResponseDTO deactivateId(Long id){
        if (id == null) {
            throw new IllegalArgumentException("ID não pode ser nulo.");
        }
        Client client =  repository.findById(id)
        .orElseThrow(()-> new EntityNotFoundException("Cliente não encontrado."));
        client.setActive(!client.isActive());
        Client salvo = repository.save(client);
        return modelMapper.map(salvo, ClientResponseDTO.class);
    }
    
    @Transactional
    public ClientResponseDTO updateInfo(Long id, ClientDTO info) {
        if (id == null) {
            throw new IllegalArgumentException("ID não pode ser nulo.");
        }
        Client client = repository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado."));
        client.setName(info.getName());
        client.setEmail(info.getEmail());
        client.setPhone(info.getPhone());
        client.setAddress(info.getAddress());
        Client save = repository.save(client);
        return modelMapper.map(save, ClientResponseDTO.class);
        
    }
}
