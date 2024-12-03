package ie.setu.repository


import ie.setu.domain.Workout
import ie.setu.domain.db.Workouts
import ie.setu.domain.db.Users
import ie.setu.domain.repository.WorkoutDAO
import ie.setu.domain.repository.UserDAO
import ie.setu.helpers.workouts
import ie.setu.helpers.populateWorkoutTable
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
val workout1 = workouts[0]
val workout2 = workouts[1]
val workout3 = workouts[2]

class WorkoutDAOTest {

    companion object {

        //Make a connection to a local, in memory H2 database.
        @BeforeAll
        @JvmStatic
        internal fun setupInMemoryDatabaseConnection() {
            Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver", user = "root", password = "")
        }
    }



    @Nested
    inner class ReadWorkouts {
        @Test
        fun `getting all workout from a populated table returns all rows`() {
            transaction {

                //Arrange - create and populate table with three workout
                populateUserTable()
                val workoutDAO = populateWorkoutTable()

                //Act and Assert
                assertEquals(3, workoutDAO.getAll().size)
            }
        }

        @Test
        fun `get workout by id that doesn't exist, results in no workout returned`() {
            transaction {

                //Arrange - create and populate table with three workouts
                populateUserTable()
                val workoutDAO = populateWorkoutTable()

                //Act & Assert
                assertEquals(null, workoutDAO.findByWorkoutId(4))
            }
        }

        @Test
        fun `get workout by id that exists, results in a correct workout returned`() {
            transaction {

                //Arrange - create and populate table with three workouts
                populateUserTable()
                val workoutDAO = populateWorkoutTable()

                //Act & Assert
                assertEquals(workout1, workoutDAO.findByWorkoutId(1))
                assertEquals(workout3, workoutDAO.findByWorkoutId(3))
            }
        }

        @Test
        fun `get workouts by a specific user by userid`() {
            transaction {
                // Arrange - create and populate tables with three users and three workouts
                val userDAO = populateUserTable()
                val workoutDAO = populateWorkoutTable()
                assertEquals(3, workoutDAO.findByUserId(1).size)
            }
        }

        @Test
        fun `get workout by user id that has no activities, results in no record returned`() {
            transaction {
                //Arrange - create and populate tables with three users and three workout
                val userDAO = populateUserTable()
                val workoutDAO = populateWorkoutTable()
                //Act & Assert
                assertEquals(0, workoutDAO.findByUserId(3).size)
            }
        }

        @Test
        fun `get all workout over empty table return null`() {
            transaction {
                //Arrange - create and set up workoutDAO object
                SchemaUtils.create(Workouts)
                val workoutDAO = WorkoutDAO()

                //Act and assert
                assertEquals(0, workoutDAO.getAll().size)
            }
        }
    }


    @Nested
    inner class CreateWorkout {
        @Test
        fun `multiple workout added to table can be retrieved successfully`() {
            transaction {

                //Arrange - create and populate table with three workout
                populateUserTable()
                val workoutDAO = populateWorkoutTable()

                //Act & Assert
                assertEquals(3, workoutDAO.getAll().size)
                assertEquals(workout1, workoutDAO.findByWorkoutId(workout1.id))
                assertEquals(workout2, workoutDAO.findByWorkoutId(workout2.id))
                assertEquals(workout3, workoutDAO.findByWorkoutId(workout3.id))
            }
        }
    }

    @Nested
    inner class DeleteWorkouts {
        @Test
        fun `deleting a non-existant workout in table results in no deletion`() {
            transaction {
                //Arrange - create and populate table with 3 workout
                populateUserTable()
                val workoutDAO = populateWorkoutTable()

                //Act and Assert
                assertEquals(3, workoutDAO.getAll().size)
                workoutDAO.delete(4)
                assertEquals(3, workoutDAO.getAll().size)
            }
        }

        @Test
        fun `deleting an existing workout in table results in record being deleted`() {
            transaction {
                //Arrange - create table with 3 workout
                populateUserTable()
                val workoutDAO = populateWorkoutTable()

                //Act and Assert
                assertEquals(3, workoutDAO.getAll().size)
                workoutDAO.delete(workout3.id)
                assertEquals(2, workoutDAO.getAll().size)
            }
        }
    }


    @Nested
    inner class UpdateWorkout {
        @Test
        fun ` updating existing workouts in table results in successful updating of table`() {
            transaction {

                //Arrange - create the table with 3 workouts
                populateUserTable()
                val workoutDAO = populateWorkoutTable()

                //Act and Assert
                val workout3Updated = Workout(workout = "Running", duration = 50.0, started = DateTime.now(), userId = 1, id = 3)
                workoutDAO.updateWorkout(workout3.id, workout3Updated)
                assertEquals(workout3Updated, workoutDAO.findByWorkoutId(3))
            }
        }

        @Test
        fun `updating non-existant workout in table results in no updates`() {
            transaction {
                // Arrange - create and populate tables with three users and three activities
                val userDAO = populateUserTable()
                val workoutDAO = populateWorkoutTable()

                // Act & Assert
                val workout4updated = Workout(workout = "Running", duration = 60.0, started = DateTime.now(), userId = 1, id = 2)
                workoutDAO.updateWorkout(4, workout4updated)
                assertEquals(null, workoutDAO.findByWorkoutId(4))
                assertEquals(3, workoutDAO.getAll().size)
            }
        }
    }
}