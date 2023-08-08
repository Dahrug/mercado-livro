package com.mercadolivro.controller.response

import com.mercadolivro.model.CustomerModel

data class PurchaseResponse ( //aqui eu poderia ter quais atributos quiser

    var id : Int? = null,

    var customer : CustomerModel? = null

)
