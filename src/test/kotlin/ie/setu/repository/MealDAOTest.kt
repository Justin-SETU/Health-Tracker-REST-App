package ie.setu.repository

import ie.setu.domain.Meal
import ie.setu.domain.db.Meals
import ie.setu.domain.repository.MealDAO
import ie.setu.helpers.meals
import ie.setu.helpers.populateMealTable
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
val meal1 = meals[0]
val meal2 = meals[1]
val meal3 = meals[2]

class MealDAOTest {

    companion object {

        //Make a connection to a local, in memory H2 database.
        @BeforeAll
        @JvmStatic
        internal fun setupInMemoryDatabaseConnection() {
            Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver", user = "root", password = "")
        }
    }



    @Nested
    inner class ReadMeals {
        @Test
        fun `getting all meal from a populated table returns all rows`() {
            transaction {

                //Arrange - create and populate table with three meal
                populateUserTable()
                val mealDAO = populateMealTable()

                //Act and Assert
                assertEquals(3, mealDAO.getAll().size)
            }
        }

        @Test
        fun `get meal by id that doesn't exist, results in no meal returned`() {
            transaction {

                //Arrange - create and populate table with three meals
                populateUserTable()
                val mealDAO = populateMealTable()

                //Act & Assert
                assertEquals(null, mealDAO.findByMealId(4))
            }
        }

        @Test
        fun `get meal by id that exists, results in a correct meal returned`() {
            transaction {

                //Arrange - create and populate table with three meals
                populateUserTable()
                val mealDAO = populateMealTable()

                //Act & Assert
                assertEquals(meal1, mealDAO.findByMealId(1))
                assertEquals(meal3, mealDAO.findByMealId(3))
            }
        }

        @Test
        fun `get meals by a specific user by userid`() {
            transaction {
                // Arrange - create and populate tables with three users and three meals
                val userDAO = populateUserTable()
                val mealDAO = populateMealTable()
                assertEquals(3, mealDAO.findByUserId(1).size)
            }
        }

        @Test
        fun `get meal by user id that has no activities, results in no record returned`() {
            transaction {
                //Arrange - create and populate tables with three users and three meal
                val userDAO = populateUserTable()
                val mealDAO = populateMealTable()
                //Act & Assert
                assertEquals(0, mealDAO.findByUserId(3).size)
            }
        }

        @Test
        fun `get all meal over empty table return null`() {
            transaction {
                //Arrange - create and set up mealDAO object
                SchemaUtils.create(Meals)
                val mealDAO = MealDAO()

                //Act and assert
                assertEquals(0, mealDAO.getAll().size)
            }
        }
    }


//    @Nested
//    inner class CreateMeal {
//        @Test
//        fun `multiple meal added to table can be retrieved successfully`() {
//            transaction {
//
//                //Arrange - create and populate table with three meal
//                populateUserTable()
//                val mealDAO = populateMealTable()
//
//                //Act & Assert
//                assertEquals(3, mealDAO.getAll().size)
//                assertEquals(meal1, mealDAO.findByMealId(meal1.id))
//                assertEquals(meal2, mealDAO.findByMealId(meal2.id))
//                assertEquals(meal3, mealDAO.findByMealId(meal3.id))
//            }
//        }
//    }

    @Nested
    inner class DeleteMeals {
        @Test
        fun `deleting a non-existant meal in table results in no deletion`() {
            transaction {
                //Arrange - create and populate table with 3 meal
                populateUserTable()
                val mealDAO = populateMealTable()

                //Act and Assert
                assertEquals(3, mealDAO.getAll().size)
                mealDAO.delete(4)
                assertEquals(3, mealDAO.getAll().size)
            }
        }

        @Test
        fun `deleting an existing meal in table results in record being deleted`() {
            transaction {
                //Arrange - create table with 3 meal
                populateUserTable()
                val mealDAO = populateMealTable()

                //Act and Assert
                assertEquals(3, mealDAO.getAll().size)
                mealDAO.delete(meal3.id)
                assertEquals(2, mealDAO.getAll().size)
            }
        }
    }


    @Nested
    inner class UpdateMeal {
        @Test
        fun ` updating existing meals in table results in successful updating of table`() {
            transaction {

                //Arrange - create the table with 3 meals
                populateUserTable()
                val mealDAO = populateMealTable()

                //Act and Assert
                val meal3Updated = Meal(food = "Sandwich", calories = 50.0, started = DateTime.now(), userId = 1, id = 3)
                mealDAO.updateMeal(meal3.id, meal3Updated)
                assertEquals(meal3Updated, mealDAO.findByMealId(3))
            }
        }

        @Test
        fun `updating non-existant meal in table results in no updates`() {
            transaction {
                // Arrange - create and populate tables with three users and three activities
                val userDAO = populateUserTable()
                val mealDAO = populateMealTable()

                // Act & Assert
                val meal4updated = Meal(food = "burger", calories = 60.0, started = DateTime.now(), userId = 1, id = 2)
                mealDAO.updateMeal(4, meal4updated)
                assertEquals(null, mealDAO.findByMealId(4))
                assertEquals(3, mealDAO.getAll().size)
            }
        }
    }
}