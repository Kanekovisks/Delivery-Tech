package com.deliverytech.delivery_api.repository;

import java.util.Optional;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.deliverytech.delivery_api.model.Client;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    Optional<Client> findByEmail(String email);
    Boolean existsByEmail(String email);
    Page<Client> findByActiveTrue(Pageable pageable);
    List<Client> findByNameContainingIgnoreCase(String name);

    boolean existsByUser_Id(Long id);
} 
