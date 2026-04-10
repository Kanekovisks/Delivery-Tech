package com.deliverytech.delivery_api.dto.responses;

import java.util.List;

import org.springframework.data.domain.Page;

import io.swagger.v3.oas.annotations.media.Schema;


public record PagedResponse<T>(
    @Schema(description = "Entidade registrada como conteúdo na página.", example = "Cliente 1")
    List<T> content,

    @Schema(description = "Página atual.", example = "0")
    int page,

    @Schema(description = "Quantidade de entidades máxima da página.", example = "10")
    int size,

    @Schema(description = "Número total de entidades presentes na lista.", example = "1")
    long totalElements,

    @Schema(description = "Número total de páginas.", example = "1")
    int totalPages,

    @Schema(description = "Verificador de última página.", example = "true")
    boolean last
    ) {
    public PagedResponse(Page<T> page){
        this(
            page.getContent(),
            page.getNumber(),
            page.getSize(),
            page.getTotalElements(),
            page.getTotalPages(),
            page.isLast()
        );
    }
}
