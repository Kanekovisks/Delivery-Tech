package com.deliverytech.delivery_api.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deliverytech.delivery_api.dto.requests.RestaurantDTO;
import com.deliverytech.delivery_api.dto.responses.RestaurantResponseDTO;
import com.deliverytech.delivery_api.service.IRestaurantService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/restaurants")
@Tag(name = "Restaurantes", description = "Endpoints de controle de restaurantes.")
public class RestaurantController {

    private final IRestaurantService service;

    public RestaurantController(IRestaurantService service) {
        this.service = service;
    }

    @Operation(summary = "Cadastrar restaurante.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Restaurante cadastrado."),
        @ApiResponse(responseCode = "400", description = "Dados inválidos para cadastro de restaurante.")
    })
    @PostMapping("/register")
    public ResponseEntity<RestaurantResponseDTO> registerRestaurant(@Valid @RequestBody RestaurantDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.registerRestaurant(dto));
    }

    @Operation(summary = "Buscar restaurante por ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Restaurante encontrado."),
        @ApiResponse(responseCode = "404", description = "Restaurante não encontrado.")
    })
    @GetMapping("/{id}")
    public ResponseEntity<com.deliverytech.delivery_api.dto.responses.ApiResponse<RestaurantResponseDTO>> getRestaurantById(@PathVariable Long id) {
        return ResponseEntity.ok(new com.deliverytech.delivery_api.dto.responses.ApiResponse<>(service.findRestaurantById(id)));
    }

    @Operation(summary = "Listar restaurantes disponíveis.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de restaurantes retornada.")
    })
    @GetMapping
    public ResponseEntity<List<RestaurantResponseDTO>> listAvailableRestaurants() {
        return ResponseEntity.ok(service.findAvailableRestaurants());
    }

    @Operation(summary = "Filtrar restaurantes por categoria.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Restaurantes filtrados retornados.")
    })
    @GetMapping("/category/{category}")
    public ResponseEntity<List<RestaurantResponseDTO>> findRestaurantsByCategory(@PathVariable String categoria) {
        return ResponseEntity.ok(service.findRestaurantsByCategory(categoria));
    }

    @Operation(summary = "Atualizar restaurante.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Restaurante atualizado."),
        @ApiResponse(responseCode = "404", description = "Restaurante não encontrado.")
    })
    @PutMapping("/{id}/update")
    public ResponseEntity<com.deliverytech.delivery_api.dto.responses.ApiResponse<RestaurantResponseDTO>> updateRestaurant(@PathVariable Long id, @Valid @RequestBody RestaurantDTO dto) {
        return ResponseEntity.ok(new com.deliverytech.delivery_api.dto.responses.ApiResponse<>(service.updateRestaurant(id, dto)));
    }

    @Operation(summary = "Calcular taxa de entrega para restaurante e CEP.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Taxa de entrega calculada."),
        @ApiResponse(responseCode = "404", description = "Restaurante não encontrado.")
    })
    @GetMapping("/{id}/delivery-fee/{cep}")
    public ResponseEntity<com.deliverytech.delivery_api.dto.responses.ApiResponse<BigDecimal>> calculateDeliveryFee(@PathVariable Long id, @PathVariable String cep) {
        return ResponseEntity.ok(new com.deliverytech.delivery_api.dto.responses.ApiResponse<>(service.calculateDeliveryFee(id, cep)));
    }
}
