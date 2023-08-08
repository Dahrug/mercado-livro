package com.mercadolivro.controller.request

import com.fasterxml.jackson.annotation.JsonAlias
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull

import java.math.BigDecimal

data class PostBookRequest ( //aqui colocamos apenas o que queremos receber de fato do usuário

    @field:NotEmpty(message = "Nome deve ser informado")
    var name: String,

    @field:NotNull(message = "Price deve ser informado")
    var price: BigDecimal,

    @JsonAlias("customer_id") //quando receber um customer_id digitado dessa forma, ele saberá que se refere ao customerId
    var customerId: Int
)

