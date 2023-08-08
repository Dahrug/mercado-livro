package com.mercadolivro.controller.response

import com.mercadolivro.enums.BookStatus
import com.mercadolivro.enums.CustomerStatus
import com.mercadolivro.model.CustomerModel
import java.math.BigDecimal

data class CustomerResponse ( //aqui eu poderia ter quais atributos quiser, password e roles não vêm aqui na responde, por exemplo

    var id : Int? = null,

    var name : String,

    var email : String,

    var status: CustomerStatus? = null

)
