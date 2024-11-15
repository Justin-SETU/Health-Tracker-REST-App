//package ie.setu.repository
//
//import ie.setu.domain.Activity
//import ie.setu.domain.db.Activities
//import ie.setu.domain.repository.ActivityDAO
//import ie.setu.helpers.activities
//import ie.setu.helpers.populateActivityTable
//import ie.setu.helpers.populateUserTable
//import org.jetbrains.exposed.sql.Database
//import org.jetbrains.exposed.sql.SchemaUtils
//import org.jetbrains.exposed.sql.transactions.transaction
//import org.joda.time.DateTime
//import org.junit.jupiter.api.BeforeAll
//import org.junit.jupiter.api.Nested
//import org.junit.jupiter.api.Test
//import kotlin.test.assertEquals
//
//val activity1 = activities[0]
//val activity2 = activities[1]
//val activity3 = activities[2]
//class ActivityDAOTest {
//    companion object {
//        // Make a connection to a local, in memory H2 database
//        @BeforeAll
//        @JvmStatic
//        internal fun setupInMemoryDatabaseConnection() {
//            Database.connect("jdbc:h2:mem:test", driver = "org.h2.Driver", user = "root", password = "")
//        }
//    }
//
//    internal fun populateActivityTable(): ActivityDAO {
//        SchemaUtils.create(Activities)
//        val activityDAO = ActivityDAO()
//        activityDAO.save(activity1)
//        activityDAO.save(activity2)
//        activityDAO.save(activity3)
//        return activityDAO
//    }
//
//    @Nested
//    inner class CreateActivities {
//        @Test
//        fun `multiple activities added to the table can be retrieved successfully`() {
//            transaction {
//                // Arrange - create and populate tables with three users and three activities
//                val activityDAO = populateActivityTable()
//
//                // Act and Insert
//                assertEquals(3, activityDAO.getAll().size)
//                assertEquals(activity1, activityDAO.findByActivityId(activity1.id))
//                assertEquals(activity2, activityDAO.findByActivityId(activity2.id))
//                assertEquals(activity3, activityDAO.findByActivityId(activity3.id))
//            }
//        }
//    }
//
//    @Nested
//    inner class ReadActivities {
//        @Test
//        fun `getting all activities from a populated table returns all rows`() {
//            transaction {
//                // Arrange - create and populate tables with three users and three activities
//
//                val activityDAO = populateActivityTable()
//                assertEquals(3, activityDAO.getAll().size)
//            }
//        }
//
//        @Test
//        fun `get activity by id that doesnt exist, returns no activity`() {
//            transaction {
//                // Arrange - create and populate tables with three users and three activities
//
//                val activityDAO = populateActivityTable()
//                assertEquals(null, activityDAO.findByActivityId(4))
//            }
//        }
//
//        @Test
//        fun `get all activities over empty table returns none`() {
//            transaction {
//                SchemaUtils.create(Activities)
//                val activityDAO = ActivityDAO()
//
//                assertEquals(0, activityDAO.getAll().size)
//            }
//        }
//
//        @Test
//        fun `get activities by a specific user by userid`() {
//            transaction {
//                // Arrange - create and populate tables with three users and three activities
//
//                val activityDAO = populateActivityTable()
//                assertEquals(3, activityDAO.findByUserId(1).size)
//            }
//        }
//
//        @Test
//        fun `get activity by user id that has no activities, results in no record returned`() {
//            transaction {
//                // Arrange - create and populate tables with three users and three activities
//
//                val activityDAO = populateActivityTable()
//                // Act & Assert
//                assertEquals(0, activityDAO.findByUserId(3).size)
//            }
//        }
//
//        @Test
//        fun `get all activities of same id works`() {
//            transaction {
//                // Arrange - create and populate tables with three users and three activities
//
//                val activityDAO = populateActivityTable()
//                assertEquals(1, activityDAO.getAllActivitiesOfSameId(2).size)
//            }
//        }
//    }
//
//    @Nested
//    inner class DeleteActivities {
//        @Test
//        fun `delete activity by activity id that exists works`() {
//            transaction {
//                // Arrange - create and populate tables with three users and three activities
//                val userDAO = populateUserTable()
//                val activityDAO = populateActivityTable()
//
//                // Act & Assert
//                assertEquals(3, activityDAO.getAll().size)
//                activityDAO.deleteByActivityId(activity3.id)
//                assertEquals(2, activityDAO.getAll().size)
//            }
//        }
//
//        @Test
//        fun `delete activity by activity id that doesnt exist results in no deletion`() {
//            transaction {
//                // Arrange - create and populate tables with three users and three activities
//                val userDAO = populateUserTable()
//                val activityDAO = populateActivityTable()
//
//                // Act & Assert
//                assertEquals(3, activityDAO.getAll().size)
//                activityDAO.deleteByActivityId(4)
//                assertEquals(3, activityDAO.getAll().size)
//            }
//        }
//
//        @Test
//        fun `deleting activities when none exist for user id results in no record deletion`() {
//            transaction {
//                // Arrange - create and populate tables with three users and three activities
//                val userDAO = populateUserTable()
//                val activityDAO = populateActivityTable()
//
//                // Act & Assert
//                assertEquals(3, activityDAO.getAll().size)
//                activityDAO.deleteByUserId(3)
//                assertEquals(3, activityDAO.getAll().size)
//            }
//        }
//
//        @Test
//        fun `delete all activities of a user results in all activites of a user in table being deleted`() {
//            transaction {
//                // Arrange - create and populate tables with three users and three activities
//                val userDAO = populateUserTable()
//                val activityDAO = populateActivityTable()
//
//                // Act & Assert
//                assertEquals(3, activityDAO.getAll().size)
//                activityDAO.deleteByUserId(1)
//                assertEquals(0, activityDAO.getAll().size)
//            }
//        }
//    }
//
//    @Nested
//    inner class UpdateActivity {
//        @Test
//        fun `update activity of a user results in the record in the table being updated`() {
//            transaction {
//                // Arrange - create and populate tables with three users and three activities
//
//                val activityDAO = populateActivityTable()
//                val activity1updated = Activity(description = "Walking and singing", duration = 40.0, calories = 101, started = DateTime.now(), userId = 1, id = 1)
//                activityDAO.updateByActivityId(activity1updated.id, activity1updated)
//                assertEquals(activity1updated, activityDAO.findByActivityId(1))
//            }
//        }
//
//        @Test
//        fun `updating non-existant activity in table results in no updates`() {
//            transaction {
//                // Arrange - create and populate tables with three users and three activities
//
//                val activityDAO = populateActivityTable()
//
//                // Act & Assert
//                val activity4updated = Activity(
//                    id = 4,
//                    description = "Cardio",
//                    duration = 42.0,
//                    calories = 220,
//                    started = DateTime.now(),
//                    userId = 2,
//                )
//                activityDAO.updateByActivity(4, activity4updated)
//                assertEquals(null, activityDAO.findByActivityId(4))
//                assertEquals(3, activityDAO.getAll().size)
//            }
//        }
//    }
//}