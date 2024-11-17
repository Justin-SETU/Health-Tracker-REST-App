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


    fun addUser (description: String, duration: Double, calories: Int, started: DateTime ): HttpResponse<JsonNode> {
        return Unirest.post("$origin/api/users/create-user")
            .body("{\"description\":\"$description\", \"duration\":\"$duration\", \"calories\":\"$calories\", \"started\":\"$started\"}")
            .asJson()
    }

    // helper function to retrieve a test user from the database by id
    private fun retrieveUserById(id: Int): HttpResponse<String> {
        return Unirest.get(origin + "/api/users/$id").asString()
    }

    // helper function to add a test user to the database
    private fun updateActivity(
        id: Int,
        description: String,
        duration: Double,
        calories: Int,
        started: DateTime,
        userId: Int, ): HttpResponse<JsonNode> {
        return Unirest.patch(origin + "/api/activities/$id")
            .body("""{
                  "description":"$description",
                  "duration":$duration,
                  "calories":$calories,
                  "started":"$started",
                  "userId":$userId
                }
                """.trimIndent(),
            ).asJson()
    }

    @Nested
    inner class ReadActivities {

        @Test
        fun `get all activities from the database returns 200 or 404 response`() {
            val response = Unirest.get(origin + "/api/activities/").asString()
            assertEquals(200, response.status)
        }
    }


    @Nested
    inner class UpdateActivities {

        @Test
        fun `updating a activity when it doesn't exist, returns a 404 response`() {

            val userId = -1
            val activityID = -1

            //Arrange - creating some text fixture data
            assertEquals(404, retrieveUserById(userId).status)

            //Act & Assert - attempt to update the email and name of activity that doesn't exist
            assertEquals(404,
                updateActivity(
                    activityID,
                    updatedDescription,
                    updatedDuration,
                    updatedCalories,
                    updatedStarted,
                    userId,
                ).status,
            )
        }
    }

}

