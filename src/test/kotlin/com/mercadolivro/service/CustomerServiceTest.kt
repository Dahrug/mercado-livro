package com.mercadolivro.service

import com.mercadolivro.enums.CustomerStatus
import com.mercadolivro.enums.Errors
import com.mercadolivro.enums.Profile
import com.mercadolivro.exception.NotFoundException
import com.mercadolivro.model.CustomerModel
import com.mercadolivro.repository.CustomerRepository
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.SpyK
import io.mockk.junit5.MockKExtension
import io.mockk.just
import io.mockk.runs
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito.verify
import java.util.*

@ExtendWith(MockKExtension::class)
class CustomerServiceTest {

    //Mocks são classes que não fazem nada, precisamos ensinar a elas como irão se comportar, como no trecho de código que usa o every
    @MockK
    private lateinit var customerRepository: CustomerRepository

    @MockK
    private lateinit var bookService: BookService

    @InjectMockKs //Aqui ele já pega os Mocks acima e inveja no CustomerService
    @SpyK
    private lateinit var customerService: CustomerService

    @Test
    fun `should return all customer`() {

        val fakeCustomers = listOf(buildCustomer(), buildCustomer())

        every { customerRepository.findAll() } returns fakeCustomers //Se apagar essa linha o código não funciona, porque
        //essa é uma classe Mock, sem funcionalidades, então precisamos ensinar a ela o que fazer, apagando essa linha a classe
        //não conheceria o funcionamento de customerRepository.findAll(), pois aqui estamos testando o customerService, e não o repository

        val customers = customerService.getAll(null)

        assertEquals(fakeCustomers, customers)
        verify(exactly = 1) { customerRepository.findAll() } //Garantindo que foi chamado apenas 1 vez
        verify(exactly = 0) { customerRepository.findByNameContaining(any()) }

    }

    @Test
    fun `should return customers when name is informed`() {
        val name = Math.random().toString()
        val fakeCustomers = listOf(buildCustomer(), buildCustomer())

        every { customerRepository.findByNameContaining(name) } returns fakeCustomers //Se apagar essa linha o código não funciona, porque
        //essa é uma classe Mock, sem funcionalidades, então precisamos ensinar a ela o que fazer, apagando essa linha a classe
        //não conheceria o funcionamento de customerRepository.findAll(), pois aqui estamos testando o customerService, e não o repository

        val customers = customerService.getAll(name)

        assertEquals(fakeCustomers, customers)
        verify(exactly = 0) { customerRepository.findAll() } //Garantindo que foi chamado apenas 1 vez
        verify(exactly = 1) { customerRepository.findByNameContaining(name) }

    }

    @Test
    fun `should return customer by id`(){
        val id = Random().nextInt()
        val fakeCustomer = buildCustomer(id)

        every { customerRepository.findById(id) } returns Optional.of(fakeCustomer)

        val customer = customerService.findById(id)

        assertEquals(fakeCustomer, customer)
        verify(exactly = 1) { customerRepository.findById(id) }
    }

    @Test
    fun `should throw error when customer not found`(){
        val id = Random().nextInt()

        every { customerRepository.findById(id) } returns Optional.empty()

        val error = assertThrows<NotFoundException> {
            customerService.findById(id)
        }

        assertEquals("Customer [${id}] not exists", error.message)
        assertEquals("ML-201", error.errorCode)

        verify(exactly = 1) { customerRepository.findById(id) }
    }

    @Test
    fun `should delete customer`() {
        val id = Random().nextInt()
        val fakeCustomer = buildCustomer(id = id)
        val expectedCustomer = fakeCustomer.copy(status = CustomerStatus.INATIVO)

        every {customerService.findById(id) } returns fakeCustomer //Para Mockkar um método de classe que está no InjectMockks, precisamos adicionar a Annotation SpyK
        //Com esse Mocck acima, garantimos que esse é um teste unitário, pois não estamos testando o findById que está
        //no método delete, estamos fazendo o Mocck dele aqui

        every {customerRepository.save(expectedCustomer) } returns expectedCustomer
        every {bookService.deleteByCustomer(fakeCustomer) } just runs //Mockando uma classe que não tem retorno

        customerService.delete(id)

        verify(exactly = 1) { bookService.deleteByCustomer(fakeCustomer) }
        verify(exactly = 1) { customerRepository.save(expectedCustomer) }

    }

    @Test
    fun `should throw not found exception when delete customer`() {
        val id = Random().nextInt()
        val fakeCustomer = buildCustomer(id = id)

        every {customerService.findById(id) } throws NotFoundException(Errors.ML201.message.format(id), Errors.ML201.code)  //Para Mockkar um método de classe que está no InjectMockks, precisamos adicionar a Annotation SpyK
        //Com esse Mocck acima, garantimos que esse é um teste unitário, pois não estamos testando o findById que está
        //no método delete, estamos fazendo o Mocck dele aqui

        val error = assertThrows<NotFoundException> {
            customerService.delete(id)
        }

        assertEquals("Customer [${id}] not exists", error.message)
        assertEquals("ML-201", error.errorCode)

        verify(exactly = 1) { customerService.findById(any()) }
        verify(exactly = 0) { bookService.deleteByCustomer(any()) } //Quando coloca 0 usamos o any pois não queremos que chame nenhum delete, nem com o fakeCustomer ou com nenhum outro
        verify(exactly = 0) { customerRepository.save(any()) }

    }

    @Test
    fun `should return true when email available`(){

        val email = "${Random().nextInt().toString()}@email.com"

        every {customerRepository.existsByEmail(email) } returns false

        val emailAvailable = customerService.emailAvailable(email)

        assertTrue(emailAvailable)
        verify(exactly = 1) { customerRepository.existsByEmail(email) }
    }

    @Test
    fun `should return false when email unavailable`(){

        val email = "${Random().nextInt().toString()}@email.com"

        every {customerRepository.existsByEmail(email) } returns true

        val emailAvailable = customerService.emailAvailable(email)

        assertFalse(emailAvailable)
        verify(exactly = 1) { customerRepository.existsByEmail(email) }
    }

    fun buildCustomer(
        id: Int? = null,
        name: String = "customer name",
        email: String = "${UUID.randomUUID()}@email.com",
        password: String = "password"
    ) = CustomerModel (
        id = id,
        name = name,
        email = email,
        status = CustomerStatus.ATIVO,
        password = password,
        roles = setOf(Profile.CUSTOMER)

    )

}