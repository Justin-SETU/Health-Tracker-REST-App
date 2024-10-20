package ie.setu.config

import org.jetbrains.exposed.sql.Database
import org.postgresql.util.PSQLException

class DbConfig {

    private lateinit var dbConfig: Database

    fun getDbConnection(): Database {

        val PGHOST = "dpg-cs77nhq3esus73chnieg-a.frankfurt-postgres.render.com"
        val PGPORT = "5432"
        val PGUSER = "healthtracker_hs9q_user"
        val PGPASSWORD = "EfY7VKlrTp3ZsJlIpAy41HpDGYNGemK5"
        val PGDATABASE = "healthtracker_hs9q"

        val dbUrl = "jdbc:postgresql://$PGHOST:$PGPORT/$PGDATABASE"

        try {
            dbConfig = Database.connect(
                url = dbUrl, driver = "org.postgresql.Driver",
                user = PGUSER, password = PGPASSWORD
            )
        } catch (e: PSQLException) {
            // Handle exception
        }
        return dbConfig
    }
}
