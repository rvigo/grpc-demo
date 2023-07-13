package com.rvigo.grpcClient.infra.clients

import com.rvigo.grpcClient.adapters.grpc.person.Person.PersonRequest
import com.rvigo.grpcClient.adapters.grpc.person.Person.PersonResponse
import com.rvigo.grpcClient.adapters.grpc.person.PersonServiceGrpc
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class PersonClientTest {
    companion object {
        const val CPF = "12345678901"
    }

    private val stub: PersonServiceGrpc.PersonServiceBlockingStub = mockk()
    private val personClient: PersonClient = PersonClient(stub)

    @Test
    fun `should find a person by cpf`() {
        val personRequest = PersonRequest.newBuilder().setCpf(CPF).build()
        val personResponse = PersonResponse.newBuilder().setCpf(CPF).build()

        every { stub.findPerson(personRequest) } returns personResponse

        val actual = personClient.findPersonByCpf(CPF)

        assertEquals(personResponse.cpf, actual.cpf)
    }
}
