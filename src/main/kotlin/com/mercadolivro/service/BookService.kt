package com.mercadolivro.service

import com.mercadolivro.enums.BookStatus
import com.mercadolivro.enums.Errors
import com.mercadolivro.exception.BadRequestException
import com.mercadolivro.exception.NotFoundException
import com.mercadolivro.model.BookModel
import com.mercadolivro.model.CustomerModel
import com.mercadolivro.model.PurchaseModel
import com.mercadolivro.repository.BookRepository
import com.mercadolivro.repository.PurchaseRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class BookService (
    private var bookRepository: BookRepository, //Injeção de dependência
    private var purchaseRepository: PurchaseRepository//Injeção de dependência
){
    fun create(book: BookModel) {
        bookRepository.save(book)
    }

    fun findAll(pageable: Pageable): Page<BookModel> {
        return bookRepository.findAll(pageable)
    }

    fun findActives(pageable : Pageable): Page<BookModel> {
        return bookRepository.findByStatus(BookStatus.ATIVO, pageable)
    }

    fun findById(id: Int): BookModel {
        return bookRepository.findById(id).orElseThrow{ NotFoundException(Errors.ML101.message.format(id), Errors.ML101.code) }
        //format entende que em tod o lugar que tiver %s vai receber o valor informado por parâmetro
    }

    fun delete(id: Int) {
        val book = findById(id)

        book.status = BookStatus.CANCELADO

        update(book)
    }

    fun update(book: BookModel) {
        bookRepository.save(book)
    }

    fun deleteByCustomer(customer: CustomerModel) {
        var books = bookRepository.findByCustomer(customer)
        for (book in books){
            book.status = BookStatus.DELETADO
        }
        bookRepository.saveAll(books)
    }

    fun findAllByIds(bookIds: Set<Int>): List<BookModel> {
        return bookRepository.findAllById(bookIds).toList()
    }

    fun purchase(purchaseModel: PurchaseModel) {
        purchaseModel.books.map {
            if (it.status == BookStatus.ATIVO)
            {
                it.status = BookStatus.VENDIDO
            }
            else {
                throw BadRequestException(Errors.ML103.message.format(it.status), Errors.ML103.code)
            }
        }
        bookRepository.saveAll(purchaseModel.books)
    }

    fun findByCustomer(customer : CustomerModel): List<BookModel> {
        val listaDeLivrosVendidos : MutableList<BookModel> = mutableListOf();
        var books = bookRepository.findByCustomer(customer);
        for (book in books)
            if (book.status == BookStatus.VENDIDO) {
                listaDeLivrosVendidos.add(book);
            }
        return listaDeLivrosVendidos;

    }


}
