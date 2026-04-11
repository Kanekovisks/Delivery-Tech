package com.deliverytech.delivery_api.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.deliverytech.delivery_api.dto.requests.ClientDTO;
import com.deliverytech.delivery_api.dto.responses.ClientResponseDTO;

public interface IClientService {
    ClientResponseDTO registerClient(ClientDTO dto);
    ClientResponseDTO findClientById(Long id);
    ClientResponseDTO findClientByEmail(String email);
    ClientResponseDTO updateClient(Long id, ClientDTO dto);
    ClientResponseDTO toggleClientStatus(Long id);
    Page<ClientResponseDTO> listActiveClients(Pageable pageable);
}