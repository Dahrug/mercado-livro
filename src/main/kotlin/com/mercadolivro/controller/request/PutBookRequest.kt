package com.mercadolivro.controller.request

import jakarta.validation.constraints.NotEmpty
import java.math.BigDecimal

data class PutBookRequest ( //aqui colocamos apenas o que queremos receber de fato do usuário

    var name: String?,
    var price: BigDecimal?
)

