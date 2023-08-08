package com.mercadolivro.controller.request

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotEmpty

data class PutCustomerRequest ( //aqui colocamos apenas o que queremos receber de fato do usuário

    @field:NotEmpty(message = "Nome deve ser informado")
    var name : String,

    @field:Email(message = "E-mail deve ser válido")
    var email: String
)