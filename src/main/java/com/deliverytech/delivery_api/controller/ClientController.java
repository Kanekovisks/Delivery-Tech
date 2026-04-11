package com.deliverytech.delivery_api.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.deliverytech.delivery_api.dto.requests.ClientDTO;
import com.deliverytech.delivery_api.dto.requests.StatusUpdateDTO;
import com.deliverytech.delivery_api.dto.responses.ClientResponseDTO;
import com.deliverytech.delivery_api.dto.responses.PagedResponse;
import com.deliverytech.delivery_api.service.IClientService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/clients")
@Tag(name = "Clientes", description = "Endpoints de controle de clientes.")
public class ClientController {
    private final IClientService service;

    public ClientController(IClientService service) {
        this.service = service;
    }

    @Operation(summary = "Cadastrar cliente.")
    @ApiResponses(
        value={
            @ApiResponse(responseCode = "201", description = "Cliente cadastrado."),
            @ApiResponse(responseCode = "400", description = "Não foi possível criar cliente.")
        }
    )
    @PostMapping("/register")
    public ResponseEntity<ClientResponseDTO> registerClient(@Valid @RequestBody ClientDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.registerClient(dto));
    }

    @Operation(summary = "Listar clientes ativos.")
    @ApiResponses(
        value={
            @ApiResponse(responseCode = "200", description = "Lista de clientes ativos retornada."),
            @ApiResponse(responseCode = "404", description = "Incapaz de encontrar lista de clientes ativos.")
        }
    )
    @GetMapping
    public ResponseEntity<PagedResponse<ClientResponseDTO>> listActiveClients(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ){
        Pageable pageable = PageRequest.of(page, size);
        Page<ClientResponseDTO> pageResult = service.listActiveClients(pageable);
        PagedResponse<ClientResponseDTO> pageResponse = new PagedResponse<>(pageResult);

        return ResponseEntity.ok()
            .header("Content-Type", "application/json")
            .cacheControl(CacheControl.maxAge(60, TimeUnit.SECONDS))
            .body(pageResponse);
    }

    @Operation(summary = "Buscar cliente por ID.")
    @ApiResponses(
        value={
            @ApiResponse(responseCode = "200", description = "Cliente encontrado com sucesso."),
            @ApiResponse(responseCode = "404", description = "Incapaz de encontrar cliente com ID mencionado.")
        }
    )
    @GetMapping("/{id}")
    public ResponseEntity<com.deliverytech.delivery_api.dto.responses.ApiResponse<ClientResponseDTO>> getClientById(@PathVariable Long id) {
        return ResponseEntity.ok()
            .header("Content-Type", "application/json")
            .body(new com.deliverytech.delivery_api.dto.responses.ApiResponse<>(service.findClientById(id)));
    }

    @Operation(summary = "Buscar cliente por e-mail.")
    @ApiResponses(
        value={
            @ApiResponse(responseCode = "200", description = "Cliente encontrado por e-mail."),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado pelo e-mail informado.")
        }
    )
    @GetMapping("/email/{email}")
    public ResponseEntity<com.deliverytech.delivery_api.dto.responses.ApiResponse<ClientResponseDTO>> findClientByEmail(@PathVariable String email) {
        return ResponseEntity.ok(new com.deliverytech.delivery_api.dto.responses.ApiResponse<>(service.findClientByEmail(email)));
    }

    @Operation(summary = "Ativar ou desativar cliente.")
    @ApiResponses(
        value={
            @ApiResponse(responseCode = "200", description = "Status do cliente atualizado."),
            @ApiResponse(responseCode = "400", description = "Não foi possível atualizar o status do cliente."),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado.")
        }
    )
    @PatchMapping("/{id}/status-toggle")
    public ResponseEntity<ClientResponseDTO> updateClientStatus(@PathVariable Long id, @Valid @RequestBody StatusUpdateDTO dto) {
        return ResponseEntity.ok(service.updateClientStatus(id, dto.getActive()));
    }

    @Operation(summary = "Atualizar informações do cliente.")
    @ApiResponses(
        value={
            @ApiResponse(responseCode = "200", description = "Cliente atualizado com sucesso."),
            @ApiResponse(responseCode = "400", description = "Incapaz de atualizar cliente."),
            @ApiResponse(responseCode = "404", description = "Incapaz de encontrar cliente com ID mencionado.")
        }
    )
    @PutMapping("/{id}/update")
    public ResponseEntity<com.deliverytech.delivery_api.dto.responses.ApiResponse<ClientResponseDTO>> updateClient(@PathVariable Long id, @Valid @RequestBody ClientDTO info) {
        return ResponseEntity.ok()
            .header("Content-Type", "application/json")
            .body(new com.deliverytech.delivery_api.dto.responses.ApiResponse<>(service.updateClient(id, info)));
    }
}
