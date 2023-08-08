package com.mercadolivro.controller.request

import com.mercadolivro.validation.EmailAvailable
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotEmpty

data class PostCustomerRequest ( //aqui colocamos apenas o que queremos receber de fato do usuário

    @field:NotEmpty(message = "Nome deve ser informado")
    var name : String,

    @field:Email(message = "E-mail deve ser válido")
    @EmailAvailable(message = "E-mail em uso")
    var email: String,

    @field:NotEmpty(message = "Senha deve ser informado")
    var password : String
)