package ie.setu.repository


import ie.setu.domain.Activity
import ie.setu.domain.db.Activities
import ie.setu.domain.repository.ActivityDAO
import ie.setu.helpers.activities
import ie.setu.helpers.populateActivityTable
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
val activity1 = activities[0]
val activity2 = activities[1]
val activity3 = activities[2]

class ActivityDAOTest {

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
        fun `getting all activities from a populated table returns all rows`() {
            transaction {

                //Arrange - create and populate table with three activities
                populateUserTable()
                val activityDAO = populateActivityTable()

                //Act and Assert
                assertEquals(3, activityDAO.getAll().size)
            }
        }

        @Test
        fun `get activity by id that doesn't exist, results in no activity id returned`() {
            transaction {

                //Arrange - create and populate table with three activities
                populateUserTable()
                val activityDAO = populateActivityTable()

                //Act & Assert
                assertEquals(null, activityDAO.findByActivityId(4))
            }
        }

        @Test
        fun `get activity by activity id that exists, results in a correct activity id returned`() {
            transaction {

                //Arrange - create and populate table with three activities
                populateUserTable()
                val activityDAO = populateActivityTable()

                //Act & Assert
                assertEquals(activity1, activityDAO.findByActivityId(1))
                assertEquals(activity3, activityDAO.findByActivityId(3))
            }
        }

        @Test
        fun `get activity by user id that has no activities, results in no record returned`() {
            transaction {
                //Arrange - create and populate tables with three users and three activities
                val userDAO = populateUserTable()
                val activityDAO = populateActivityTable()
                //Act & Assert
                assertEquals(0, activityDAO.findByUserId(3).size)
            }
        }


        @Test
        fun `get activities by a specific user by userid`() {
            transaction {
                // Arrange - create and populate tables with three users and three activities
                val userDAO = populateUserTable()
                val activityDAO = populateActivityTable()
                assertEquals(3, activityDAO.findByUserId(1).size)
            }
        }

        @Test
        fun `get all activities over empty table return null`() {
            transaction {
                //Arrange - create and set up activityDAO object
                SchemaUtils.create(Activities)
                val activityDAO = ActivityDAO()

                //Act and assert
                assertEquals(0, activityDAO.getAll().size)
            }
        }
    }


    @Nested
    inner class CreateActivity {
        @Test
        fun `multiple activities added to table can be retrieved successfully`() {
            transaction {

                //Arrange - create and populate table with three activity
                populateUserTable()
                val activityDAO = populateActivityTable()

                //Act & Assert
                assertEquals(3, activityDAO.getAll().size)
                assertEquals(activity1, activityDAO.findByActivityId(activity1.id))
                assertEquals(activity2, activityDAO.findByActivityId(activity2.id))
                assertEquals(activity3, activityDAO.findByActivityId(activity3.id))
            }
        }
    }

    @Nested
    inner class DeleteActivitys {
        @Test
        fun `deleting a non-existant activity in table results in no deletion`() {
            transaction {
                //Arrange - create and populate table with 3 activitys
                populateUserTable()
                val activityDAO = populateActivityTable()

                //Act and Assert
                assertEquals(3, activityDAO.getAll().size)
                activityDAO.delete(4)
                assertEquals(3, activityDAO.getAll().size)
            }
        }

        @Test
        fun `deleting an existing activity in table results in record being deleted`() {
            transaction {
                //Arrange - create table with 3 activitys
                populateUserTable()
                val activityDAO = populateActivityTable()

                //Act and Assert
                assertEquals(3, activityDAO.getAll().size)
                activityDAO.delete(activity3.id)
                assertEquals(2, activityDAO.getAll().size)
            }
        }

    }


    @Nested
    inner class UpdateActivity {

        @Test
        fun `update activity of a user, results in the record in the table being updated`() {
            transaction {
                // Arrange - create and populate tables with three users and three activities
                val userDAO = populateUserTable()
                val activityDAO = populateActivityTable()
                val activity1updated = Activity(description = "Walking and singing", duration = 40.0, calories = 101, started = DateTime.now(), userId = 1, id = 1)
                activityDAO.updateActivity(activity1updated.id, activity1updated)
                assertEquals(activity1updated, activityDAO.findByActivityId(1))
            }
        }

        @Test
        fun `updating non-existant activity in table results in no updates`() {
            transaction {
                // Arrange - create and populate tables with three users and three activities
                val userDAO = populateUserTable()
                val activityDAO = populateActivityTable()

                // Act & Assert
                val activity4updated = Activity(id = 4, description = "Cardio", duration = 42.0, calories = 220, started = DateTime.now(), userId = 2,)
                activityDAO.updateActivity(4, activity4updated)
                assertEquals(null, activityDAO.findByActivityId(4))
                assertEquals(3, activityDAO.getAll().size)
            }
        }

    }
}