package ie.setu.config

import org.jetbrains.exposed.sql.Database
import org.postgresql.util.PSQLException
import io.github.oshai.kotlinlogging.KotlinLogging

class DbConfig {

    private val logger = KotlinLogging.logger {}
    private lateinit var dbConfig: Database

    fun getDbConnection(): Database {

        val PGHOST = "dpg-cthe26ogph6c73dca4a0-a.frankfurt-postgres.render.com"
        val PGPORT = "5432"
        val PGUSER = "health_tracker_app_7ilw_user"
        val PGPASSWORD = "ohMaRNbWMmeh74KsGdvwJ8SLauHu0K59"
        val PGDATABASE = "health_tracker_app_7ilw"

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
