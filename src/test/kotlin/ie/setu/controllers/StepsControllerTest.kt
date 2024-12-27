package ie.setu.controllers

import ie.setu.config.DbConfig
import ie.setu.domain.Step
import ie.setu.helpers.*
import ie.setu.utils.jsonToObject
import kong.unirest.HttpResponse
import kong.unirest.JsonNode
import kong.unirest.Unirest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class StepControllerTest {

    private val db = DbConfig().getDbConnection()
    private val app = ServerContainer.instance
    private val origin = "http://localhost:" + app.port()

    // Helper functions
    private fun addStep(userId: Int, date: String, steps: Int): HttpResponse<JsonNode> {
        return Unirest.post("$origin/api/steps/add")
            .body("{\"userId\":$userId, \"date\":\"$date\", \"steps\":$steps}")
            .asJson()
    }

    private fun retrieveStepsByUserId(userId: Int): HttpResponse<String> {
        return Unirest.get("$origin/api/steps/$userId").asString()
    }

    private fun retrieveAllSteps(): HttpResponse<String> {
        return Unirest.get("$origin/api/steps").asString()
    }

    private fun deleteStepById(id: Int): HttpResponse<String> {
        return Unirest.delete("$origin/api/steps/$id").asString()
    }

    private fun updateStep(id: Int, userId: Int, date: String, steps: Int): HttpResponse<JsonNode> {
        return Unirest.patch("$origin/api/steps/update/$id")
            .body("{\"userId\":$userId, \"date\":\"$date\", \"steps\":$steps}")
            .asJson()
    }

    @Nested
    inner class ReadSteps {
        @Test
        fun `get all steps returns 200 when steps exist`() {
            val response = retrieveAllSteps()
            assertEquals(200, response.status)
        }



        @Test
        fun `get steps by user ID returns 404 when no steps exist`() {
            val nonExistingUserId = -1
            val response = retrieveStepsByUserId(nonExistingUserId)
            assertEquals(404, response.status)
        }
    }

    @Nested
    inner class AddSteps {
        @Test
        fun `add a step with valid data returns 201`() {
            val response = addStep(validUserId, "2024-01-01", 1000)
            assertEquals(201, response.status)
        }

        @Test
        fun `add a step with invalid user ID returns 404`() {
            val response = addStep(-1, "2024-01-01", 1000)
            assertEquals(404, response.status)
        }
    }

    @Nested
    inner class UpdateSteps {
        @Test
        fun `update a step when it exists returns 204`() {
            val addResponse = addStep(validUserId, "2024-01-01", 1000)
            val addedStep: Step = jsonToObject(addResponse.body.toString())

            val updateResponse = updateStep(addedStep.id, validUserId, "2024-01-02", 2000)
            assertEquals(204, updateResponse.status)
        }

        @Test
        fun `update a step when it does not exist returns 404`() {
            val response = updateStep(-1, validUserId, "2024-01-01", 1000)
            assertEquals(404, response.status)
        }
    }

    @Nested
    inner class DeleteSteps {
        @Test
        fun `delete a step when it exists returns 204`() {
            val addResponse = addStep(validUserId, "2024-01-01", 1000)
            val addedStep: Step = jsonToObject(addResponse.body.toString())

            val deleteResponse = deleteStepById(addedStep.id)
            assertEquals(204, deleteResponse.status)
        }

        @Test
        fun `delete a step when it does not exist returns 404`() {
            val response = deleteStepById(-1)
            assertEquals(404, response.status)
        }
    }
}
