package com.mercadolivro.controller

import com.mercadolivro.controller.request.PostCustomerRequest
import com.mercadolivro.controller.request.PutCustomerRequest
import com.mercadolivro.controller.response.CustomerResponse
import com.mercadolivro.extension.toCustomerModel
import com.mercadolivro.extension.toResponse
import com.mercadolivro.model.BookModel
import com.mercadolivro.model.PurchaseModel
import com.mercadolivro.service.CustomerService
import com.mercadolivro.service.PurchaseService
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("customers")

class CustomerController (
    private val customerService: CustomerService, //Injeção de dependência
    private val purchaseService: PurchaseService //Injeção de dependência
) {

    @GetMapping
    fun findAll(@RequestParam name : String?, @PageableDefault(page = 0, size = 10) pageable : Pageable) : Page<CustomerResponse> {
        return customerService.findAll(name, pageable).map { it.toResponse() } //map faz uma iteração em todos os objetos da lista
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody @Valid customer: PostCustomerRequest) {
        customerService.create(customer.toCustomerModel())
    }

    @GetMapping("/{id}")
    fun getCustomer(@PathVariable id: Int): CustomerResponse {
        return customerService.findById(id).toResponse()
    }

    @GetMapping("/comprados/{id}")
    fun getCustomerPurchases(@PathVariable id: Int): List<BookModel> {
        return purchaseService.getLivrosComprados( purchaseService.findByCustomer(customerService.findById(id)) );
    }

    @GetMapping("/vendidos/{id}")
    fun getCustomerBooksSold(@PathVariable id: Int): List<BookModel> {
        return customerService.getLivrosVendidos( customerService.findById(id) );
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun update(@PathVariable id: Int, @RequestBody @Valid customer: PutCustomerRequest) {
        val customerSaved = customerService.findById(id)
        customerService.update(customer.toCustomerModel(customerSaved))
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable id: Int) {
        customerService.delete(id)
        }
    }

