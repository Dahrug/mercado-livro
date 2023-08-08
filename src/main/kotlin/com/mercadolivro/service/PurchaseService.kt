package com.mercadolivro.service

import com.mercadolivro.enums.BookStatus
import com.mercadolivro.enums.Errors
import com.mercadolivro.events.PurchaseEvent
import com.mercadolivro.exception.BadRequestException
import com.mercadolivro.exception.NotFoundException
import com.mercadolivro.model.BookModel
import com.mercadolivro.model.CustomerModel
import com.mercadolivro.model.PurchaseModel
import com.mercadolivro.repository.CustomerRepository
import com.mercadolivro.repository.PurchaseRepository
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service

@Service
class PurchaseService(
    private val purchaseRepository: PurchaseRepository,
    private val applicationEventPublisher: ApplicationEventPublisher,
    private val customerRepository : CustomerRepository,
    )   {

    fun create(purchaseModel: PurchaseModel){
        purchaseModel.books.map {
            if (it.status != BookStatus.ATIVO) {
                throw BadRequestException(Errors.ML103.message.format(it.status), Errors.ML103.code)
            }
        }
        purchaseRepository.save(purchaseModel)
        applicationEventPublisher.publishEvent(PurchaseEvent(this, purchaseModel))
    }

    fun update(purchaseModel: PurchaseModel) {
        purchaseRepository.save(purchaseModel)
    }

    fun findByCustomer(customer : CustomerModel): List<PurchaseModel> {
        return purchaseRepository.findByCustomer(customer)
    }

    fun getLivrosComprados(listaDeCompras: List<PurchaseModel>): List<BookModel> {
        val listaDeLivros : MutableList<BookModel> = mutableListOf();
        for (compra in listaDeCompras)
            compra.books.map { listaDeLivros!!.add(it) }
        return listaDeLivros;
    }

}
