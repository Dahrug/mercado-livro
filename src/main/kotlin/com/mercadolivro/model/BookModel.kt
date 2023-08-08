package com.mercadolivro.model

import com.mercadolivro.enums.BookStatus
import com.mercadolivro.enums.Errors
import com.mercadolivro.exception.BadRequestException
import jakarta.persistence.*
import java.math.BigDecimal


@Entity(name = "book")
data class BookModel (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id : Int? = null,

    @Column //se o nome da coluna fosse diferente do atributo, precisaria especificar via parâmetro aqui
    var name : String,

    @Column
    var price: BigDecimal,


    @ManyToOne //da perspectiva de livros, muitos livros podem pertencer a um usuário
    @JoinColumn(name = "customer_id")
    var customer : CustomerModel? = null
) {
    @Column
    @Enumerated(EnumType.STRING)
    var status : BookStatus? = null
        set(value){ //novo valor do atributo
            //field é o valor atual do tributo
            if(field == BookStatus.CANCELADO || field == BookStatus.DELETADO)
                throw BadRequestException(Errors.ML102.message.format(field), Errors.ML102.code)
            field = value
        }

    constructor(id: Int? = null,
                name: String,
                price: BigDecimal,
                customer: CustomerModel? = null,
                status: BookStatus?): this(id, name, price, customer){ //aqui estamos chamando o constutor padrão que está la em cima
        this.status = status
    }

    /*Poderia fazer outros construtores, como por exemplo um recebendo apenas um id:
    constructor(id: Int): this(id: 1, name "Exemplo", BigDecimal.ONE, CustomerModel()...)
    */




}