package ie.setu.repository

import ie.setu.domain.Water
import ie.setu.domain.db.Waters
import ie.setu.domain.repository.WaterDAO
import ie.setu.helpers.waters
import ie.setu.helpers.populateWaterTable
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
val water1 = waters[0]
val water2 = waters[1]
val water3 = waters[2]

class WaterDAOTest {

    companion object {

        //Make a connection to a local, in memory H2 database.
        @BeforeAll
        @JvmStatic
        internal fun setupInMemoryDatabaseConnection() {
            Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver", user = "root", password = "")
        }
    }



    @Nested
    inner class ReadWaters {
        @Test
        fun `getting all water from a populated table returns all rows`() {
            transaction {

                //Arrange - create and populate table with three water
                populateUserTable()
                val waterDAO = populateWaterTable()

                //Act and Assert
                assertEquals(3, waterDAO.getAll().size)
            }
        }

        @Test
        fun `get water by id that doesn't exist, results in no water returned`() {
            transaction {

                //Arrange - create and populate table with three waters
                populateUserTable()
                val waterDAO = populateWaterTable()

                //Act & Assert
                assertEquals(null, waterDAO.findByWaterId(4))
            }
        }

        @Test
        fun `get water by id that exists, results in a correct water returned`() {
            transaction {

                //Arrange - create and populate table with three waters
                populateUserTable()
                val waterDAO = populateWaterTable()

                //Act & Assert
                assertEquals(water1, waterDAO.findByWaterId(1))
                assertEquals(water3, waterDAO.findByWaterId(3))
            }
        }

        @Test
        fun `get waters by a specific user by userid`() {
            transaction {
                // Arrange - create and populate tables with three users and three waters
                val userDAO = populateUserTable()
                val waterDAO = populateWaterTable()
                assertEquals(3, waterDAO.findByUserId(1).size)
            }
        }

        @Test
        fun `get water by user id that has no activities, results in no record returned`() {
            transaction {
                //Arrange - create and populate tables with three users and three water
                val userDAO = populateUserTable()
                val waterDAO = populateWaterTable()
                //Act & Assert
                assertEquals(0, waterDAO.findByUserId(3).size)
            }
        }

        @Test
        fun `get all water over empty table return null`() {
            transaction {
                //Arrange - create and set up waterDAO object
                SchemaUtils.create(Waters)
                val waterDAO = WaterDAO()

                //Act and assert
                assertEquals(0, waterDAO.getAll().size)
            }
        }
    }


    @Nested
    inner class CreateWater {
        @Test
        fun `multiple water added to table can be retrieved successfully`() {
            transaction {

                //Arrange - create and populate table with three water
                populateUserTable()
                val waterDAO = populateWaterTable()

                //Act & Assert
                assertEquals(3, waterDAO.getAll().size)
                assertEquals(water1, waterDAO.findByWaterId(water1.id))
                assertEquals(water2, waterDAO.findByWaterId(water2.id))
                assertEquals(water3, waterDAO.findByWaterId(water3.id))
            }
        }
    }

    @Nested
    inner class DeleteWaters {
        @Test
        fun `deleting a non-existant water in table results in no deletion`() {
            transaction {
                //Arrange - create and populate table with 3 water
                populateUserTable()
                val waterDAO = populateWaterTable()

                //Act and Assert
                assertEquals(3, waterDAO.getAll().size)
                waterDAO.delete(4)
                assertEquals(3, waterDAO.getAll().size)
            }
        }

        @Test
        fun `deleting an existing water in table results in record being deleted`() {
            transaction {
                //Arrange - create table with 3 water
                populateUserTable()
                val waterDAO = populateWaterTable()

                //Act and Assert
                assertEquals(3, waterDAO.getAll().size)
                waterDAO.delete(water3.id)
                assertEquals(2, waterDAO.getAll().size)
            }
        }
    }


    @Nested
    inner class UpdateWater {
        @Test
        fun ` updating existing waters in table results in successful updating of table`() {
            transaction {

                //Arrange - create the table with 3 waters
                populateUserTable()
                val waterDAO = populateWaterTable()

                //Act and Assert
                val water3Updated = Water(waterintake = 5.0, started = DateTime.now(), userId = 1, id = 3)
                waterDAO.updateWater(water3.id, water3Updated)
                assertEquals(water3Updated, waterDAO.findByWaterId(3))
            }
        }

        @Test
        fun `updating non-existant water in table results in no updates`() {
            transaction {
                // Arrange - create and populate tables with three users and three activities
                val userDAO = populateUserTable()
                val waterDAO = populateWaterTable()

                // Act & Assert
                val water4updated = Water(waterintake = 10.0, started = DateTime.now(), userId = 1, id = 2)
                waterDAO.updateWater(4, water4updated)
                assertEquals(null, waterDAO.findByWaterId(4))
                assertEquals(3, waterDAO.getAll().size)
            }
        }
    }
}