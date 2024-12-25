package ie.setu.repository

import ie.setu.domain.Step
import ie.setu.domain.db.Steps
import ie.setu.domain.repository.StepDAO
import ie.setu.helpers.steps
import ie.setu.helpers.populateStepTable
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
val step1 = steps[0]
val step2 = steps[1]
val step3 = steps[2]

class StepDAOTest {

    companion object {

        //Make a connection to a local, in memory H2 database.
        @BeforeAll
        @JvmStatic
        internal fun setupInMemoryDatabaseConnection() {
            Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver", user = "root", password = "")
        }
    }



    @Nested
    inner class ReadSteps {
        @Test
        fun `getting all step from a populated table returns all rows`() {
            transaction {

                //Arrange - create and populate table with three step
                populateUserTable()
                val stepDAO = populateStepTable()

                //Act and Assert
                assertEquals(3, stepDAO.getAll().size)
            }
        }

        @Test
        fun `get step by id that doesn't exist, results in no step returned`() {
            transaction {

                //Arrange - create and populate table with three steps
                populateUserTable()
                val stepDAO = populateStepTable()

                //Act & Assert
                assertEquals(null, stepDAO.findByStepId(4))
            }
        }

        @Test
        fun `get step by id that exists, results in a correct step returned`() {
            transaction {

                //Arrange - create and populate table with three steps
                populateUserTable()
                val stepDAO = populateStepTable()

                //Act & Assert
                assertEquals(step1, stepDAO.findByStepId(1))
                assertEquals(step3, stepDAO.findByStepId(3))
            }
        }

        @Test
        fun `get steps by a specific user by userid`() {
            transaction {
                // Arrange - create and populate tables with three users and three steps
                val userDAO = populateUserTable()
                val stepDAO = populateStepTable()
                assertEquals(3, stepDAO.findByUserId(1).size)
            }
        }

        @Test
        fun `get step by user id that has no activities, results in no record returned`() {
            transaction {
                //Arrange - create and populate tables with three users and three step
                val userDAO = populateUserTable()
                val stepDAO = populateStepTable()
                //Act & Assert
                assertEquals(0, stepDAO.findByUserId(3).size)
            }
        }

        @Test
        fun `get all step over empty table return null`() {
            transaction {
                //Arrange - create and set up stepDAO object
                SchemaUtils.create(Steps)
                val stepDAO = StepDAO()

                //Act and assert
                assertEquals(0, stepDAO.getAll().size)
            }
        }
    }


    @Nested
    inner class CreateStep {
        @Test
        fun `multiple step added to table can be retrieved successfully`() {
            transaction {

                //Arrange - create and populate table with three step
                populateUserTable()
                val stepDAO = populateStepTable()

                //Act & Assert
                assertEquals(3, stepDAO.getAll().size)
                assertEquals(step1, stepDAO.findByStepId(step1.id))
                assertEquals(step2, stepDAO.findByStepId(step2.id))
                assertEquals(step3, stepDAO.findByStepId(step3.id))
            }
        }
    }

    @Nested
    inner class DeleteSteps {
        @Test
        fun `deleting a non-existant step in table results in no deletion`() {
            transaction {
                //Arrange - create and populate table with 3 step
                populateUserTable()
                val stepDAO = populateStepTable()

                //Act and Assert
                assertEquals(3, stepDAO.getAll().size)
                stepDAO.delete(4)
                assertEquals(3, stepDAO.getAll().size)
            }
        }

        @Test
        fun `deleting an existing step in table results in record being deleted`() {
            transaction {
                //Arrange - create table with 3 step
                populateUserTable()
                val stepDAO = populateStepTable()

                //Act and Assert
                assertEquals(3, stepDAO.getAll().size)
                stepDAO.delete(step3.id)
                assertEquals(2, stepDAO.getAll().size)
            }
        }
    }


    @Nested
    inner class UpdateStep {
        @Test
        fun ` updating existing steps in table results in successful updating of table`() {
            transaction {

                //Arrange - create the table with 3 steps
                populateUserTable()
                val stepDAO = populateStepTable()

                //Act and Assert
                val step3Updated = Step(distance = 30.6, stepcount = 1000, userId = 1, id = 3)
                stepDAO.updateStep(step3.id, step3Updated)
                assertEquals(step3Updated, stepDAO.findByStepId(3))
            }
        }

        @Test
        fun `updating non-existant step in table results in no updates`() {
            transaction {
                // Arrange - create and populate tables with three users and three activities
                val userDAO = populateUserTable()
                val stepDAO = populateStepTable()

                // Act & Assert
                val step4updated = Step(distance = 40.0, stepcount = 2000, userId = 1, id = 2)
                stepDAO.updateStep(4, step4updated)
                assertEquals(null, stepDAO.findByStepId(4))
                assertEquals(3, stepDAO.getAll().size)
            }
        }
    }
}