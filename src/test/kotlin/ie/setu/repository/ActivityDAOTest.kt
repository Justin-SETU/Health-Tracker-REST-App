package ie.setu.repository


import ie.setu.domain.Activity
import ie.setu.domain.db.Activities
import ie.setu.domain.db.Users
import ie.setu.domain.repository.ActivityDAO
import ie.setu.domain.repository.UserDAO
import ie.setu.helpers.activities
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

    internal fun populateActivityTable(): ActivityDAO {
        SchemaUtils.create(Activities)
        val activityDAO = ActivityDAO()
        activityDAO.save(activity1)
        activityDAO.save(activity2)
        activityDAO.save(activity3)
        return activityDAO
    }

    internal fun populateUserTable(): UserDAO {
        SchemaUtils.create(Users)
        val userDAO = UserDAO()
        userDAO.save(user1)
        userDAO.save(user2)
        userDAO.save(user3)
        return userDAO
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
        fun `get activity by id that doesn't exist, results in no activity returned`() {
            transaction {

                //Arrange - create and populate table with three activities
                populateUserTable()
                val activityDAO = populateActivityTable()

                //Act & Assert
                assertEquals(null, activityDAO.findByActivityId(4))
            }
        }

        @Test
        fun `get activity by id that exists, results in a correct activity returned`() {
            transaction {

                //Arrange - create and populate table with three activities
                populateUserTable()
                val activityDAO = populateActivityTable()

                //Act & Assert
                assertEquals(null, activityDAO.findByActivityId(4))
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
    inner class CreateActivitys {
        @Test
        fun `multiple activitys added to table can be retrieved successfully`() {
            transaction {

                //Arrange - create and populate table with three activitys
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
    inner class UpdateActivitys {
        @Test
        fun ` updating existing activitys in table results in successful updating of table`() {
            transaction {

                //Arrange - create the table with 3 activitys
                populateUserTable()
                val activityDAO = populateActivityTable()

                //Act and Assert
                val activity3Updated = Activity(description = "Walking", duration = 30.0, calories = 101, started = DateTime.now(), userId = 1, id = 1)
                activityDAO.updateActivity(activity3.id, activity3Updated)
                assertEquals(activity3Updated, activityDAO.findByActivityId(3))
            }
        }
    }
}