package com.mercadolivro.service

import com.mercadolivro.enums.CustomerStatus
import com.mercadolivro.enums.Errors
import com.mercadolivro.enums.Profile
import com.mercadolivro.exception.NotFoundException
import com.mercadolivro.model.BookModel
import com.mercadolivro.model.CustomerModel
import com.mercadolivro.repository.CustomerRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class CustomerService(
    private val customerRepository : CustomerRepository,
    private var bookService : BookService,
    //private val bCrypt: BCryptPasswordEncoder
) {

    fun getAll(name : String?): List<CustomerModel> {

        name?.let { //let é um bloco de código que roda somente se a condição anterior for atendida
            return customerRepository.findByNameContaining(it)
        }
        return customerRepository.findAll().toList()

    }

    fun findAll(name : String?, pageable : Pageable) : Page<CustomerModel> {

        name?.let { //let é um bloco de código que roda somente se a condição anterior for atendida
            return customerRepository.findByNameContaining(it, pageable)
        }

        return customerRepository.findAll(pageable)

    }

    fun create(customer: CustomerModel) {
        val customerCopy = customer.copy(
            roles = setOf(Profile.CUSTOMER),
            //password = bCrypt.encode(customer.password)
        )
        customerRepository.save(customerCopy)
    }

    fun findById(id: Int): CustomerModel {
        return customerRepository.findById(id).orElseThrow{ NotFoundException(Errors.ML201.message.format(id), Errors.ML201.code) }
    }

    fun update(customer: CustomerModel) {
        if (!customerRepository.existsById(customer.id!!)){
            throw Exception()
        }
        customerRepository.save(customer)
    }

    fun delete(id: Int) {
        var customer = findById(id)
        bookService.deleteByCustomer(customer)
        customer.status = CustomerStatus.INATIVO
        customerRepository.save(customer)
    }

    fun emailAvailable(email: String): Boolean {
        return !customerRepository.existsByEmail(email) //invertemos porque queremos saber se está disponível
    }

    fun getLivrosVendidos(customer: CustomerModel): List<BookModel> {
        return bookService.findByCustomer(customer);
    }

}