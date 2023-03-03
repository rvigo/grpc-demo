package com.rvigo.grpcPoc.configurations


import com.rvigo.grpcPoc.infra.entities.AddressEntity.AddressTable
import com.rvigo.grpcPoc.infra.entities.PersonEntity.PersonsTable
import mu.KotlinLogging
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class DatabaseConfiguration(
    @Value("\${datasource.jdbc_url}")
    val url: String,
    @Value("\${datasource.username}")
    val user: String,
    @Value("\${datasource.password}")
    val password: String
) {
    private val logger = KotlinLogging.logger {}


    @Bean
    fun createDatabase(): Database {
        logger.info { "connecting to database" }
        val db = Database.connect(
            url = url, driver = "org.postgresql.Driver",
            user = user, password = password
        )

        drop()
        createTables()

        return db
    }

    private fun drop() {
        logger.info { "dropping tables" }
        transaction {
            SchemaUtils.drop(AddressTable)
            SchemaUtils.drop(PersonsTable)

            commit()
        }
    }

    private fun createTables() {
        logger.info { "creating tables" }
        transaction {
            addLogger(StdOutSqlLogger)
            SchemaUtils.createMissingTablesAndColumns(PersonsTable, AddressTable)
            commit()
        }
    }
}

// Ensures that only one db instance will be used
fun <T> Database.query(block: () -> T): T = transaction(this) {
    addLogger(StdOutSqlLogger)
    block()
}

