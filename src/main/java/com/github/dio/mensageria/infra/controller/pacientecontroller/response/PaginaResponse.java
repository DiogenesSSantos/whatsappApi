package com.github.dio.mensageria.infra.controller.pacientecontroller.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Resposta paginada")
public record PaginaResponse<T>(
        @Schema(description = "Lista de itens")
        List<T> conteudo,
        @Schema(description = "Pagina atual (0-indexed)")
        int pagina,
        @Schema(description = "Tamanho da pagina")
        int tamanhoPagina,
        @Schema(description = "Total de itens")
        long totalItens,
        @Schema(description = "Total de paginas")
        int totalPaginas
) {}
