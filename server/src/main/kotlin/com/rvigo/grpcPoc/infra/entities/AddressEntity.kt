package com.rvigo.grpcPoc.infra.entities

import com.rvigo.grpcPoc.models.Address
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption

class AddressEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<AddressEntity>(AddressTable)
    object AddressTable : IntIdTable() {
        val street = varchar("street", 90)
        val city = varchar("city", 50)
        val type = enumeration("type", Address.Type::class).index()
        val person = reference(
            name = "person_id",
            foreign = PersonEntity.PersonsTable,
            onDelete = ReferenceOption.CASCADE,
            onUpdate = ReferenceOption.CASCADE
        )
    }

    var street by AddressTable.street
    var city by AddressTable.city
    var type by AddressTable.type
    var person by PersonEntity referencedOn AddressTable.person
}

fun AddressEntity.toDomain() = Address(id.value, street, city, type)
