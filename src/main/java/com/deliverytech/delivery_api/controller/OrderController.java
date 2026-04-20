package com.deliverytech.delivery_api.controller;

import java.math.BigDecimal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.deliverytech.delivery_api.dto.requests.OrderDTO;
import com.deliverytech.delivery_api.dto.requests.OrderStatusDTO;
import com.deliverytech.delivery_api.dto.responses.OrderResponseDTO;
import com.deliverytech.delivery_api.model.User;
import com.deliverytech.delivery_api.service.IOrderService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
@Tag(name = "Pedidos", description = "Endpoints de controle de pedidos.")
public class OrderController {

    private final IOrderService service;

    public OrderController(IOrderService service) {
        this.service = service;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENTE')")
    @GetMapping("/my")
    public ResponseEntity<?> myOrders(
            @AuthenticationPrincipal User userLogged,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(service.myOrders(userLogged, pageable));
    }

    @Operation(summary = "Criar pedido.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Pedido criado."),
        @ApiResponse(responseCode = "400", description = "Dados inválidos para criação de pedido.")
    })
    @PostMapping("/orders")
    public ResponseEntity<OrderResponseDTO> createOrder(@Valid @RequestBody OrderDTO dto, @AuthenticationPrincipal User userLogged) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createOrder(dto, userLogged));
    }

    @Operation(summary = "Buscar pedido completo por ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pedido encontrado."),
        @ApiResponse(responseCode = "404", description = "Pedido não encontrado.")
    })
    @GetMapping("/orders/{id}")
    public ResponseEntity<com.deliverytech.delivery_api.dto.responses.ApiResponse<OrderResponseDTO>> getOrderById(@PathVariable Long id, @AuthenticationPrincipal User loggedUser) {
        return ResponseEntity.ok(new com.deliverytech.delivery_api.dto.responses.ApiResponse<>(service.findOrderById(id, loggedUser)));
    }

    @Operation(summary = "Buscar histórico de pedidos de um cliente.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Histórico de pedidos retornado."),
        @ApiResponse(responseCode = "404", description = "Cliente não encontrado.")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'RESTAURANT')")
    @GetMapping("/clients/{clientId}/orders")
    public ResponseEntity<Page<OrderResponseDTO>> getOrdersByClient(
        @AuthenticationPrincipal User userLogged, 
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size) 
        {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(service.findOrdersByClient(userLogged, pageable));
    }

    @Operation(summary = "Atualizar status do pedido.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Status do pedido atualizado."),
        @ApiResponse(responseCode = "404", description = "Pedido não encontrado.")
    })
    @PatchMapping("/orders/{id}/status")
    public ResponseEntity<com.deliverytech.delivery_api.dto.responses.ApiResponse<OrderResponseDTO>> updateOrderStatus(@PathVariable Long id, @Valid @RequestBody OrderStatusDTO dto, @AuthenticationPrincipal User loggedUser) {
        return ResponseEntity.ok(new com.deliverytech.delivery_api.dto.responses.ApiResponse<>(service.updateOrderStatus(id, dto.getStatus(), loggedUser)));
    }

    @Operation(summary = "Cancelar pedido.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pedido cancelado."),
        @ApiResponse(responseCode = "404", description = "Pedido não encontrado.")
    })
    @DeleteMapping("/orders/{id}")
    public ResponseEntity<com.deliverytech.delivery_api.dto.responses.ApiResponse<OrderResponseDTO>> cancelOrder(@PathVariable Long id, @AuthenticationPrincipal User loggedUser) {
        return ResponseEntity.ok(new com.deliverytech.delivery_api.dto.responses.ApiResponse<>(service.cancelOrder(id, loggedUser)));
    }

    @Operation(summary = "Calcular total do pedido sem salvar.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Total calculado."),
        @ApiResponse(responseCode = "400", description = "Dados inválidos para cálculo do pedido.")
    })
    @PostMapping("/orders/calculate")
    public ResponseEntity<com.deliverytech.delivery_api.dto.responses.ApiResponse<BigDecimal>> calculateOrderTotal(@Valid @RequestBody OrderDTO dto) {
        return ResponseEntity.ok(new com.deliverytech.delivery_api.dto.responses.ApiResponse<>(service.calculateOrderTotal(dto.getItems())));
    }
}
