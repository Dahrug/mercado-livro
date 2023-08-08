package com.mercadolivro.validation

import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass

@Constraint(validatedBy = [EmailAvailableValidator::class])
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD) //informar qual o tipo de alvo da classe de anotação, no caso serão os fields
annotation class EmailAvailable(
    val message : String = "E-mail já cadastrado",
    val groups : Array<KClass<*>> = [],
    val payload : Array<KClass<out Payload>> = []
)
