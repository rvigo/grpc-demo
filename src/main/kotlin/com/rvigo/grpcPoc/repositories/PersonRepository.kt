package com.rvigo.grpcPoc.repositories

import com.rvigo.grpcPoc.infra.tables.Persons
import com.rvigo.grpcPoc.infra.tables.fromRow
import com.rvigo.grpcPoc.models.Person
import mu.KotlinLogging
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Repository

@Repository
class PersonRepository(
    @Value("\${datasource.jdbc_url}") url: String,
    @Value("\${datasource.username}") user: String,
    @Value("\${datasource.password}") password: String
) {
    private val logger = KotlinLogging.logger {}

    init {
        logger.info { "connecting to database" }
        Database.connect(
            url = url, driver = "org.postgresql.Driver",
            user = user, password = password
        )

        logger.info { "database connected" }
        createTables()
        populate()
    }

    private fun createTables() {
        logger.info { "creating tables" }
        transaction {
            SchemaUtils.create(Persons)
            commit()
        }
    }

    private fun populate() {
        logger.info { "populating tables" }
        transaction {
            if (Persons.selectAll().empty()) {
                Persons.insert {
                    it[name] = "Person 1"
                    it[email] = "person01@gmail.com"
                    it[cpf] = "1234567890"
                }
                Persons.insert {
                    it[name] = "Person 2"
                    it[email] = "person02@gmail.com"
                    it[cpf] = "1234567892"
                }
            }
        }
    }

    fun findPersonByCpfOrNull(cpf: String): Person? = transaction {
        Persons.select { Persons.cpf eq cpf }.singleOrNull()?.let { Persons.fromRow(it) }
    }
}
