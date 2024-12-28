package ie.setu.controllers

import ie.setu.config.DbConfig
import ie.setu.helpers.*
import kong.unirest.HttpResponse
import kong.unirest.JsonNode
import kong.unirest.Unirest
import org.joda.time.DateTime
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import kotlin.test.assertEquals


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ActivityControllerTest {

    private val db = DbConfig().getDbConnection()
    private val app = ServerContainer.instance
    private val origin = "http://localhost:" + app.port()

    private val validUserId = 1 // Replace with a valid user ID from your database
    private val invalidUserId = -1
    private val validActivityId = 1 // Replace with a valid activity ID from your database
    private val invalidActivityId = -1

    @Nested
    inner class CreateActivities {

//        @Test
//        fun `adding an activity for an existing user returns 201`() {
//            val response = Unirest.post("$origin/api/activities")
//                .body(
//                    """{
//                        "description": "Running",
//                        "duration": 30.0,
//                        "calories": 250,
//                        "started": "2024-12-26T10:00:00",
//                        "userId": $validUserId
//                    }"""
//                ).asJson()
//
//            assertEquals(201, response.status)
//        }

        @Test
        fun `adding an activity for a non-existing user returns 404`() {
            val response = Unirest.post("$origin/api/activities")
                .body(
                    """{
                        "description": "Swimming",
                        "duration": 60.0,
                        "calories": 400,
                        "started": "2024-12-26T12:00:00",
                        "userId": $invalidUserId
                    }"""
                ).asJson()

            assertEquals(404, response.status)
        }
    }

    @Nested
    inner class ReadActivities {

//        @Test
//        fun `get all activities returns 200 when activities exist`() {
//            val response = Unirest.get("$origin/api/activities").asJson()
//            assertEquals(200, response.status)
//        }

//        @Test
//        fun `get all activities returns 404 when no activities exist`() {
//            // Mock or ensure no activities exist in the database
//            val response = Unirest.get("$origin/api/activities").asJson()
//            assertEquals(404, response.status)
//        }

//        @Test
//        fun `get activities by valid user ID returns 200`() {
//            val response = Unirest.get("$origin/api/activities/user/$validUserId").asJson()
//            assertEquals(200, response.status)
//        }

        @Test
        fun `get activities by invalid user ID returns 404`() {
            val response = Unirest.get("$origin/api/activities/user/$invalidUserId").asJson()
            assertEquals(404, response.status)
        }
    }

    @Nested
    inner class UpdateActivities {

//        @Test
//        fun `updating an existing activity returns 204`() {
//            val response = Unirest.patch("$origin/api/activities/$validActivityId")
//                .body(
//                    """{
//                        "description": "Cycling",
//                        "duration": 45.0,
//                        "calories": 300,
//                        "started": "2024-12-26T15:00:00",
//                        "userId": $validUserId
//                    }"""
//                ).asJson()
//
//            assertEquals(204, response.status)
//        }

        @Test
        fun `updating a non-existing activity returns 404`() {
            val response = Unirest.patch("$origin/api/activities/$invalidActivityId")
                .body(
                    """{
                        "description": "Yoga",
                        "duration": 60.0,
                        "calories": 200,
                        "started": "2024-12-26T16:00:00",
                        "userId": $validUserId
                    }"""
                ).asJson()

            assertEquals(404, response.status)
        }
    }

    @Nested
    inner class DeleteActivities {

//        @Test
//        fun `deleting an existing activity returns 204`() {
//            val response = Unirest.delete("$origin/api/activities/$validActivityId").asString()
//            assertEquals(204, response.status)
//        }

        @Test
        fun `deleting a non-existing activity returns 404`() {
            val response = Unirest.delete("$origin/api/activities/$invalidActivityId").asString()
            assertEquals(404, response.status)
        }
    }
}
