package ie.setu.repository


import ie.setu.domain.Log
import ie.setu.domain.db.Activities
import ie.setu.domain.db.Logs
import ie.setu.domain.repository.LogDAO
import ie.setu.helpers.logs
import ie.setu.helpers.populateLogTable
import ie.setu.helpers.populateUserTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

//retrieving some test data from Fixtures
val log1 = logs[0]
val log2 = logs[1]
val log3 = logs[2]

class LogDAOTest {

    companion object {

        //Make a connection to a local, in memory H2 database.
        @BeforeAll
        @JvmStatic
        internal fun setupInMemoryDatabaseConnection() {
            Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver", user = "root", password = "")
        }
    }



    @Nested
    inner class ReadActivities {
        @Test
        fun `getting all logs from a populated table returns all rows`() {
            transaction {

                //Arrange - create and populate table with three logs
                populateUserTable()
                val logDAO = populateLogTable()

                //Act and Assert
                assertEquals(3, logDAO.getAll().size)
            }
        }

        @Test
        fun `get log by id that doesn't exist, results in no log id returned`() {
            transaction {

                //Arrange - create and populate table with three logs
                populateUserTable()
                val logDAO = populateLogTable()

                //Act & Assert
                assertEquals(null, logDAO.findByLogId(4))
            }
        }

        @Test
        fun `get log by log id that exists, results in a correct log id returned`() {
            transaction {

                //Arrange - create and populate table with three logs
                populateUserTable()
                val logDAO = populateLogTable()

                //Act & Assert
                assertEquals(log1, logDAO.findByLogId(1))
                assertEquals(log3, logDAO.findByLogId(3))
            }
        }

        @Test
        fun `get log by user id that has no logs, results in no record returned`() {
            transaction {
                //Arrange - create and populate tables with three users and three logs
                val userDAO = populateUserTable()
                val logDAO = populateLogTable()
                //Act & Assert
                assertEquals(0, logDAO.findByUserId(3).size)
            }
        }


        @Test
        fun `get logs by a specific user by userid`() {
            transaction {
                // Arrange - create and populate tables with three users and three logs
                val userDAO = populateUserTable()
                val logDAO = populateLogTable()
                assertEquals(3, logDAO.findByUserId(1).size)
            }
        }

        @Test
        fun `get all logs over empty table return null`() {
            transaction {
                //Arrange - create and set up logDAO object
                SchemaUtils.create(Logs)
                val logDAO = LogDAO()

                //Act and assert
                assertEquals(0, logDAO.getAll().size)
            }
        }
    }


    @Nested
    inner class CreateLog {
        @Test
        fun `multiple logs added to table can be retrieved successfully`() {
            transaction {

                //Arrange - create and populate table with three log
                populateUserTable()
                val logDAO = populateLogTable()

                //Act & Assert
                assertEquals(3, logDAO.getAll().size)
                assertEquals(log1, logDAO.findByLogId(log1.id))
                assertEquals(log2, logDAO.findByLogId(log2.id))
                assertEquals(log3, logDAO.findByLogId(log3.id))
            }
        }
    }

    @Nested
    inner class DeleteLogs {
        @Test
        fun `deleting a non-existant log in table results in no deletion`() {
            transaction {
                //Arrange - create and populate table with 3 logs
                populateUserTable()
                val logDAO = populateLogTable()

                //Act and Assert
                assertEquals(3, logDAO.getAll().size)
                logDAO.delete(4)
                assertEquals(3, logDAO.getAll().size)
            }
        }

        @Test
        fun `deleting an existing log in table results in record being deleted`() {
            transaction {
                //Arrange - create table with 3 logs
                populateUserTable()
                val logDAO = populateLogTable()

                //Act and Assert
                assertEquals(3, logDAO.getAll().size)
                logDAO.delete(log3.id)
                assertEquals(2, logDAO.getAll().size)
            }
        }

    }


    @Nested
    inner class UpdateLog {

        @Test
        fun `update log of a user, results in the record in the table being updated`() {
            transaction {
                // Arrange - create and populate tables with three users and three logs
                val userDAO = populateUserTable()
                val logDAO = populateLogTable()
                val log1updated = Log(status_type = "Daily", summary = "5 hours", started = DateTime.now(), userId = 1, id = 1)
                logDAO.updateLog(log1updated.id, log1updated)
                assertEquals(log1updated, logDAO.findByLogId(1))
            }
        }

        @Test
        fun `updating non-existant log in table results in no updates`() {
            transaction {
                // Arrange - create and populate tables with three users and three logs
                val userDAO = populateUserTable()
                val logDAO = populateLogTable()

                // Act & Assert
                val log4updated = Log(status_type = "Daily", summary = "5 hours", started = DateTime.now(), userId = 1, id = 1)
                logDAO.updateLog(4, log4updated)
                assertEquals(null, logDAO.findByLogId(4))
                assertEquals(3, logDAO.getAll().size)
            }
        }

    }
}