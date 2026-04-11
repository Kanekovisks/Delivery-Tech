package com.deliverytech.delivery_api.controller;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deliverytech.delivery_api.dto.requests.OrderDTO;
import com.deliverytech.delivery_api.dto.requests.OrderStatusDTO;
import com.deliverytech.delivery_api.dto.responses.OrderResponseDTO;
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

    @Operation(summary = "Criar pedido.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Pedido criado."),
        @ApiResponse(responseCode = "400", description = "Dados inválidos para criação de pedido.")
    })
    @PostMapping("/orders")
    public ResponseEntity<OrderResponseDTO> createOrder(@Valid @RequestBody OrderDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createOrder(dto));
    }

    @Operation(summary = "Buscar pedido completo por ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pedido encontrado."),
        @ApiResponse(responseCode = "404", description = "Pedido não encontrado.")
    })
    @GetMapping("/orders/{id}")
    public ResponseEntity<com.deliverytech.delivery_api.dto.responses.ApiResponse<OrderResponseDTO>> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(new com.deliverytech.delivery_api.dto.responses.ApiResponse<>(service.findOrderById(id)));
    }

    @Operation(summary = "Buscar histórico de pedidos de um cliente.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Histórico de pedidos retornado."),
        @ApiResponse(responseCode = "404", description = "Cliente não encontrado.")
    })
    @GetMapping("/clients/{clientId}/orders")
    public ResponseEntity<List<OrderResponseDTO>> getOrdersByClient(@PathVariable Long clienteId) {
        return ResponseEntity.ok(service.findOrdersByClient(clienteId));
    }

    @Operation(summary = "Atualizar status do pedido.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Status do pedido atualizado."),
        @ApiResponse(responseCode = "404", description = "Pedido não encontrado.")
    })
    @PatchMapping("/orders/{id}/status")
    public ResponseEntity<com.deliverytech.delivery_api.dto.responses.ApiResponse<OrderResponseDTO>> updateOrderStatus(@PathVariable Long id, @Valid @RequestBody OrderStatusDTO dto) {
        return ResponseEntity.ok(new com.deliverytech.delivery_api.dto.responses.ApiResponse<>(service.updateOrderStatus(id, dto.getStatus())));
    }

    @Operation(summary = "Cancelar pedido.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pedido cancelado."),
        @ApiResponse(responseCode = "404", description = "Pedido não encontrado.")
    })
    @DeleteMapping("/orders/{id}")
    public ResponseEntity<com.deliverytech.delivery_api.dto.responses.ApiResponse<OrderResponseDTO>> cancelOrder(@PathVariable Long id) {
        return ResponseEntity.ok(new com.deliverytech.delivery_api.dto.responses.ApiResponse<>(service.cancelOrder(id)));
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
