package com.deliverytech.delivery_api.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.deliverytech.delivery_api.dto.requests.ClientDTO;
import com.deliverytech.delivery_api.dto.responses.ClientResponseDTO;
import com.deliverytech.delivery_api.model.User;

public interface IClientService {
    ClientResponseDTO registerClient(ClientDTO dto, User userLogged);
    ClientResponseDTO findClientById(Long id);
    ClientResponseDTO findClientByEmail(String email);
    ClientResponseDTO updateClient(Long id, ClientDTO dto);
    ClientResponseDTO updateClientStatus(Long id, Boolean active);
    Page<ClientResponseDTO> listActiveClients(Pageable pageable);
}