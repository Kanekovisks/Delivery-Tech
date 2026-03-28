package com.deliverytech.delivery_api.repository;

import java.util.Optional;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.deliverytech.delivery_api.model.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByEmail(String email);
    Boolean existsByEmail(String email);
    List<Client> findByAtivoTrue();
    List<Client> findByNomeContainingIgnoreCase(String nome);
} 
