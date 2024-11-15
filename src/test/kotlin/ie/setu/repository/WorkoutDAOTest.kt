package ie.setu.repository


import ie.setu.domain.Workout
import ie.setu.domain.db.Activities
import ie.setu.domain.db.Users
import ie.setu.domain.db.Workouts
import ie.setu.domain.repository.WorkoutDAO
import ie.setu.domain.repository.UserDAO
import ie.setu.helpers.workouts
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

    internal fun populateWorkoutTable(): WorkoutDAO {
        SchemaUtils.create(Workouts)
        val workoutDAO = WorkoutDAO()
        workoutDAO.save(workout1)
        workoutDAO.save(workout2)
        workoutDAO.save(workout3)
        return workoutDAO
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
        fun `getting all workouts from a populated table returns all rows`() {
            transaction {

                //Arrange - create and populate table with three workouts
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
                assertEquals(null, workoutDAO.findByWorkoutId(4))
            }
        }

        @Test
        fun `get all workouts over empty table return null`() {
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
    inner class CreateWorkouts {
        @Test
        fun `multiple workouts added to table can be retrieved successfully`() {
            transaction {

                //Arrange - create and populate table with three workouts
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
                //Arrange - create and populate table with 3 workouts
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
                //Arrange - create table with 3 workouts
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
    inner class UpdateWorkouts {
        @Test
        fun ` updating existing workouts in table results in successful updating of table`() {
            transaction {

                //Arrange - create the table with 3 workouts
                populateUserTable()
                val workoutDAO = populateWorkoutTable()

                //Act and Assert
                val workout3Updated = Workout(workout = "Walking", duration = 30.0, started = DateTime.now(), userId = 1, id = 1)
                workoutDAO.updateWorkout(workout3.id, workout3Updated)
                assertEquals(workout3Updated, workoutDAO.findByWorkoutId(3))
            }
        }
    }
}