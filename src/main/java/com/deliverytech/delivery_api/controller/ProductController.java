package com.deliverytech.delivery_api.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deliverytech.delivery_api.dto.requests.ProductAvailabilityDTO;
import com.deliverytech.delivery_api.dto.requests.ProductDTO;
import com.deliverytech.delivery_api.dto.responses.ProductResponseDTO;
import com.deliverytech.delivery_api.service.IProductService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/api/products", produces = "application/json")
@CrossOrigin(origins = "*")
@Tag(name = "Produtos", description = "Endpoints de controle de produtos.")
public class ProductController {

    private final IProductService service;

    public ProductController(IProductService service) {
        this.service = service;
    }

    @Operation(summary = "Cadastrar produto.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Produto cadastrado."),
        @ApiResponse(responseCode = "400", description = "Dados inválidos para cadastro de produto.")
    })
    @PreAuthorize("hasAnyRole('ADMIN','RESTAURANT')")
    @PostMapping("/register")
    public ResponseEntity<ProductResponseDTO> createProduct(@Valid @RequestBody ProductDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.registerProduct(dto));
    }

    @Operation(summary = "Buscar produto por ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Produto encontrado."),
        @ApiResponse(responseCode = "404", description = "Produto não encontrado.")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findProductById(id));
    }

    @Operation(summary = "Listar produtos de um restaurante.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Produtos retornados."),
        @ApiResponse(responseCode = "404", description = "Restaurante não encontrado.")
    })
    @GetMapping("/restaurants/{restaurantId}")
    public ResponseEntity<List<ProductResponseDTO>> getProductsByRestaurant(@PathVariable Long restauranteId) {
        return ResponseEntity.ok(service.findProductsByRestaurant(restauranteId));
    }

    @Operation(summary = "Atualizar produto.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Produto atualizado."),
        @ApiResponse(responseCode = "404", description = "Produto não encontrado.")
    })
    @PreAuthorize("hasAnyRole('ADMIN','RESTAURANT')")
    @PutMapping("/{id}/update")
    public ResponseEntity<ProductResponseDTO> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductDTO dto) {
        return ResponseEntity.ok(service.updateProduct(id, dto));
    }

    @Operation(summary = "Alterar disponibilidade do produto.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Disponibilidade atualizada."),
        @ApiResponse(responseCode = "404", description = "Produto não encontrado.")
    })
    @PatchMapping("/{id}/availability")
    public ResponseEntity<ProductResponseDTO> changeProductAvailability(@PathVariable Long id, @Valid @RequestBody ProductAvailabilityDTO dto) {
        return ResponseEntity.ok(service.toggleProductAvailability(id, dto.getAvailable()));
    }

    @Operation(summary = "Buscar produtos por categoria.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Produtos filtrados retornados.")
    })
    @GetMapping("/category/{category}")
    public ResponseEntity<List<ProductResponseDTO>> getProductsByCategory(@PathVariable String categoria) {
        return ResponseEntity.ok(service.findProductsByCategory(categoria));
    }
}
