package com.mercadolivro.model

import com.mercadolivro.enums.CustomerStatus
import com.mercadolivro.enums.Profile
import jakarta.persistence.*


@Entity(name = "customer")
data class CustomerModel (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id : Int? = null,

    @Column //se o nome da coluna fosse diferente do atributo, precisaria especificar via parâmetro aqui
    var name : String,

    @Column(unique = true)
    var email: String,

    @Column
    @Enumerated(EnumType.STRING)
    var status : CustomerStatus,

    @Column
    var password: String,

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    @ElementCollection(targetClass = Profile::class, fetch = FetchType.EAGER) //Toda vez que buscar também traga as informações da tabela customer_role
    @CollectionTable(name = "customer_role", joinColumns = [JoinColumn(name = "customer_id")]) //Olhe na coluna customer_id de customer_roles para saber qual Role é a desse customer
    var roles : Set<Profile> = setOf() //Set não permite repetição de valores

)