package com.rvigo.grpcPoc.infra.tables

import org.jetbrains.exposed.dao.id.IntIdTable

object Persons : IntIdTable() {
    val name = varchar("name", 50).index()
    val email = varchar("email", 100)
    val cpf = varchar("cpf", 14)
}
