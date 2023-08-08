package com.mercadolivro.controller.response

class PageResponse<T>(
    var items: List<T>, //T é um tipo genérico passado por parâmetro na classe
    var currentPage: Int,
    var totalItems: Long,
    var totalPages: Int
)