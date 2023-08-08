package com.mercadolivro.exception

class BadRequestException(override val message : String, val errorCode: String): Exception() { //Está estendendo a classe Exception()
} //tivemos que colocar override em message porque a classe Exception já possui esse atributo