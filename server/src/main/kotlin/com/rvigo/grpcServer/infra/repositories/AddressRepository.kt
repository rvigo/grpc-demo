package com.rvigo.grpcServer.infra.repositories

import com.rvigo.grpcServer.infra.entities.AddressEntity
import com.rvigo.grpcServer.infra.entities.AddressEntity.AddressTable
import com.rvigo.grpcServer.infra.entities.PersonEntity
import com.rvigo.grpcServer.infra.entities.toDomain
import com.rvigo.grpcServer.models.Address
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Repository

@Repository
class AddressRepository(private val database: Database) {
    fun findAddressesByType(type: Address.Type): Address? = transaction(database) {
        AddressEntity.find { AddressTable.type eq type }.singleOrNull()?.let(AddressEntity::toDomain)
    }

    fun addAddress(person: PersonEntity, address: Address) = transaction(database) {
        addLogger(StdOutSqlLogger)

        AddressEntity.new {
            this.person = person
            street = address.street
            city = address.city
            type = address.type
        }.toDomain()
    }
}

