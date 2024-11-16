package ie.setu.config

import org.jetbrains.exposed.sql.Database
import org.postgresql.util.PSQLException
import io.github.oshai.kotlinlogging.KotlinLogging

class DbConfig {

    private val logger = KotlinLogging.logger {}
    private lateinit var dbConfig: Database

    fun getDbConnection(): Database {

        val PGHOST = "dpg-csr5a5q3esus73ca0sg0-a.frankfurt-postgres.render.com"
        val PGPORT = "5432"
        val PGUSER = "justin"
        val PGPASSWORD = "Os09jWRiWxP7zZwODpMMRjXAwmacXCcD"
        val PGDATABASE = "healthtracker_ughk"

        val dbUrl = "jdbc:postgresql://$PGHOST:$PGPORT/$PGDATABASE"

        try {
            logger.info { "Starting DB connection $dbUrl" }
            dbConfig = Database.connect(
                url = dbUrl, driver = "org.postgresql.Driver",
                user = PGUSER, password = PGPASSWORD
            )
            logger.info { "Connected to database." + dbConfig.url }
        } catch (e: PSQLException) {
            // Handle exception
            logger.info{"Error in DB connection ..${e.printStackTrace()}"}
        }
        return dbConfig
    }
}
