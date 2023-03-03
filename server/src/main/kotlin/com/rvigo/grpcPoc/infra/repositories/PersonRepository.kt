package com.rvigo.grpcPoc.infra.repositories

import com.rvigo.grpcPoc.configurations.query
import com.rvigo.grpcPoc.infra.entities.PersonEntity
import com.rvigo.grpcPoc.infra.entities.PersonEntity.PersonsTable
import com.rvigo.grpcPoc.infra.entities.toDomain
import com.rvigo.grpcPoc.models.Person
import org.jetbrains.exposed.sql.Database
import org.springframework.stereotype.Repository

@Repository
class PersonRepository(
    private val database: Database,
    private val addressRepository: AddressRepository,
) {
    fun findPersonByCpfOrNull(cpf: String): Person? = database.query {
        PersonEntity.find { PersonsTable.cpf eq cpf }.singleOrNull()?.let(PersonEntity::toDomain)
    }

    fun addPerson(person: Person): Person? {
        val newPerson = database.query {
            PersonEntity.new {
                name = person.name
                email = person.email
                cpf = person.cpf
            }
        }

        person.addresses.applyIfNotEmpty {
            map { addressRepository.addAddress(newPerson, it) }
        }

        return database.query {
            PersonEntity.findById(newPerson.id)?.let(PersonEntity::toDomain)
        }
    }

    fun personExists(cpf: String) = database.query {
        !PersonEntity.find { PersonsTable.cpf eq cpf }.empty()
    }
}

fun <T> List<T>.applyIfNotEmpty(block: List<T>.() -> List<T>) {
    if (this.isNotEmpty()) {
        block(this)
    }
}
