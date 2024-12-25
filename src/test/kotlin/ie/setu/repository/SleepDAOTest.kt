package ie.setu.repository

import ie.setu.domain.Sleep
import ie.setu.domain.db.Sleeps
import ie.setu.domain.repository.SleepDAO
import ie.setu.helpers.sleeps
import ie.setu.helpers.populateSleepTable
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
val sleep1 = sleeps[0]
val sleep2 = sleeps[1]
val sleep3 = sleeps[2]

class SleepDAOTest {

    companion object {

        //Make a connection to a local, in memory H2 database.
        @BeforeAll
        @JvmStatic
        internal fun setupInMemoryDatabaseConnection() {
            Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver", user = "root", password = "")
        }
    }



    @Nested
    inner class ReadSleeps {
        @Test
        fun `getting all sleep from a populated table returns all rows`() {
            transaction {

                //Arrange - create and populate table with three sleep
                populateUserTable()
                val sleepDAO = populateSleepTable()

                //Act and Assert
                assertEquals(3, sleepDAO.getAll().size)
            }
        }

        @Test
        fun `get sleep by id that doesn't exist, results in no sleep returned`() {
            transaction {

                //Arrange - create and populate table with three sleeps
                populateUserTable()
                val sleepDAO = populateSleepTable()

                //Act & Assert
                assertEquals(null, sleepDAO.findBySleepId(4))
            }
        }

        @Test
        fun `get sleep by id that exists, results in a correct sleep returned`() {
            transaction {

                //Arrange - create and populate table with three sleeps
                populateUserTable()
                val sleepDAO = populateSleepTable()

                //Act & Assert
                assertEquals(sleep1, sleepDAO.findBySleepId(1))
                assertEquals(sleep3, sleepDAO.findBySleepId(3))
            }
        }

        @Test
        fun `get sleeps by a specific user by userid`() {
            transaction {
                // Arrange - create and populate tables with three users and three sleeps
                val userDAO = populateUserTable()
                val sleepDAO = populateSleepTable()
                assertEquals(3, sleepDAO.findByUserId(1).size)
            }
        }

        @Test
        fun `get sleep by user id that has no activities, results in no record returned`() {
            transaction {
                //Arrange - create and populate tables with three users and three sleep
                val userDAO = populateUserTable()
                val sleepDAO = populateSleepTable()
                //Act & Assert
                assertEquals(0, sleepDAO.findByUserId(3).size)
            }
        }

        @Test
        fun `get all sleep over empty table return null`() {
            transaction {
                //Arrange - create and set up sleepDAO object
                SchemaUtils.create(Sleeps)
                val sleepDAO = SleepDAO()

                //Act and assert
                assertEquals(0, sleepDAO.getAll().size)
            }
        }
    }


    @Nested
    inner class CreateSleep {
        @Test
        fun `multiple sleep added to table can be retrieved successfully`() {
            transaction {

                //Arrange - create and populate table with three sleep
                populateUserTable()
                val sleepDAO = populateSleepTable()

                //Act & Assert
                assertEquals(3, sleepDAO.getAll().size)
                assertEquals(sleep1, sleepDAO.findBySleepId(sleep1.id))
                assertEquals(sleep2, sleepDAO.findBySleepId(sleep2.id))
                assertEquals(sleep3, sleepDAO.findBySleepId(sleep3.id))
            }
        }
    }

    @Nested
    inner class DeleteSleeps {
        @Test
        fun `deleting a non-existant sleep in table results in no deletion`() {
            transaction {
                //Arrange - create and populate table with 3 sleep
                populateUserTable()
                val sleepDAO = populateSleepTable()

                //Act and Assert
                assertEquals(3, sleepDAO.getAll().size)
                sleepDAO.delete(4)
                assertEquals(3, sleepDAO.getAll().size)
            }
        }

        @Test
        fun `deleting an existing sleep in table results in record being deleted`() {
            transaction {
                //Arrange - create table with 3 sleep
                populateUserTable()
                val sleepDAO = populateSleepTable()

                //Act and Assert
                assertEquals(3, sleepDAO.getAll().size)
                sleepDAO.delete(sleep3.id)
                assertEquals(2, sleepDAO.getAll().size)
            }
        }
    }


    @Nested
    inner class UpdateSleep {
        @Test
        fun ` updating existing sleeps in table results in successful updating of table`() {
            transaction {

                //Arrange - create the table with 3 sleeps
                populateUserTable()
                val sleepDAO = populateSleepTable()

                //Act and Assert
                val sleep3Updated = Sleep(duration = 30.4, started = DateTime.now(), userId = 1, id = 3)
                sleepDAO.updateSleep(sleep3.id, sleep3Updated)
                assertEquals(sleep3Updated, sleepDAO.findBySleepId(3))
            }
        }

        @Test
        fun `updating non-existant sleep in table results in no updates`() {
            transaction {
                // Arrange - create and populate tables with three users and three activities
                val userDAO = populateUserTable()
                val sleepDAO = populateSleepTable()

                // Act & Assert
                val sleep4updated = Sleep(duration = 30.5, started = DateTime.now(), userId = 1, id = 2)
                sleepDAO.updateSleep(4, sleep4updated)
                assertEquals(null, sleepDAO.findBySleepId(4))
                assertEquals(3, sleepDAO.getAll().size)
            }
        }
    }
}