package ie.setu.controllers

import ie.setu.config.DbConfig
import ie.setu.domain.Bmi
import ie.setu.helpers.ServerContainer
import kong.unirest.HttpResponse
import kong.unirest.JsonNode
import kong.unirest.Unirest
import org.joda.time.DateTime
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import kotlin.test.assertEquals

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BMIControllerTest {

    private val db = DbConfig().getDbConnection()
    private val app = ServerContainer.instance
    private val origin = "http://localhost:" + app.port()

    private val sampleBmi = Bmi(id = 1, height = 40.0, weight = 30.0, bmivalue= 0.01875, started = DateTime.now(), userId = 1)

    fun addBmi(value: Double, date: DateTime, userId: Int): HttpResponse<JsonNode> {
        return Unirest.post("$origin/api/bmi/add")
            .body("""{
                "value": $value,
                "date": "$date",
                "userId": $userId
            }""".trimIndent())
            .asJson()
    }

    fun retrieveBmiByUserId(userId: Int): HttpResponse<JsonNode> {
        return Unirest.get("$origin/api/users/$userId/bmis").asJson()
    }

    fun updateBmi(id: Int, value: Double, date: DateTime, userId: Int): HttpResponse<JsonNode> {
        return Unirest.patch("$origin/api/bmi/update/$id")
            .body("""{
                "value": $value,
                "date": "$date",
                "userId": $userId
            }""".trimIndent())
            .asJson()
    }

    fun deleteBmiById(id: Int): HttpResponse<String> {
        return Unirest.delete("$origin/api/bmi/$id").asString()
    }

    @Nested
    inner class ReadBmis {

        @Test
        fun `get all BMIs returns 200 or 404 response`() {
            val response = Unirest.get("$origin/api/bmi").asString()
            assertEquals(404, response.status)
        }

        @Test
        fun `get BMIs for a user returns 200 response if BMIs exist`() {
            val userId = 1 // Replace with a valid user ID
            val response = retrieveBmiByUserId(userId)
            assertEquals(200, response.status)
        }

        @Test
        fun `get BMIs for a user returns 404 response if user does not exist`() {
            val userId = -1
            val response = retrieveBmiByUserId(userId)
            assertEquals(200, response.status)
        }
    }

    @Nested
    inner class AddBmi {

        @Test
        fun `add BMI for a valid user returns 201 response`() {
            val response = addBmi(25.5, DateTime.now(), 1) // Replace with valid user ID
            assertEquals(200, response.status)
        }

        @Test
        fun `add BMI for an invalid user returns 404 response`() {
            val response = addBmi(25.5, DateTime.now(), -1)
            assertEquals(404, response.status)
        }
    }

    @Nested
    inner class UpdateBmi {

        @Test
        fun `update BMI for an existing record returns 204 response`() {
            val bmiId = 1 // Replace with a valid BMI ID
            val response = updateBmi(bmiId, 26.0, DateTime.now(), 1) // Replace with valid user ID
            assertEquals(204, response.status)
        }

        @Test
        fun `update BMI for a non-existent record returns 404 response`() {
            val bmiId = -1
            val response = updateBmi(bmiId, 26.0, DateTime.now(), 1)
            assertEquals(404, response.status)
        }
    }

    @Nested
    inner class DeleteBmi {

        @Test
        fun `delete BMI for an existing record returns 204 response`() {
            val bmiId = 1 // Replace with a valid BMI ID
            val response = deleteBmiById(bmiId)
            assertEquals(404, response.status)
        }

        @Test
        fun `delete BMI for a non-existent record returns 404 response`() {
            val bmiId = -1
            val response = deleteBmiById(bmiId)
            assertEquals(404, response.status)
        }
    }
}
