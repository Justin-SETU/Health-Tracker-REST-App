package ie.setu.controllers

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.joda.JodaModule
import ie.setu.domain.repository.UserDAO

import io.javalin.http.Context

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import ie.setu.domain.Step
import ie.setu.domain.Step

import ie.setu.domain.repository.StepDAO

//main endpoints and http requests for handling API requests, handles different things
object StepController {
    private val userDao = UserDAO()
    private val stepDAO = StepDAO()
    //--------------------------------------------------------------
    // StepDAO specifics
    //-------------------------------------------------------------

    fun getAllActivities(ctx: Context) {
        val steps = stepDAO.getAll()
        if (steps.size != 0) {
            ctx.status(200)
        }
        else{
            ctx.status(404)
        }
        ctx.json(steps)
    }

    fun getStepsByUserId(ctx: Context) {
        if (userDao.findById(ctx.pathParam("user-id").toInt()) != null) {
            val steps = stepDAO.findByUserId(ctx.pathParam("user-id").toInt())
            if (steps.isNotEmpty()) {
                ctx.json(steps)
                ctx.status(200)
            }
            else{
                ctx.status(404)
            }
        }
        else{
            ctx.status(404)
        }
    }

    fun getStepsByActivityId(ctx: Context) {
        val step = stepDAO.findByStepId((ctx.pathParam("step-id").toInt()))
        if (step != null){
            ctx.json(step)
            ctx.status(200)
        }
        else{
            ctx.status(404)
        }
    }

    fun addStep(ctx: Context) {
        val step : Step = jsonToObject(ctx.body())
        val userId = userDao.findById(step.userId)
        if (userId != null) {
            val stepId = stepDAO.save(step)
            step.id = stepId
            ctx.json(step)
            ctx.status(201)
        }
        else{
            ctx.status(404)
        }
    }

    fun deleteStepByActivityId(ctx: Context){
        if (stepDAO.deleteByActivityId(ctx.pathParam("step-id").toInt()) != 0)
            ctx.status(204)
        else
            ctx.status(404)
    }

    fun deleteStepByUserId(ctx: Context){
        if (stepDAO.deleteByUserId(ctx.pathParam("user-id").toInt()) != 0)
            ctx.status(204)
        else
            ctx.status(404)
    }

    fun updateStep(ctx: Context){
        val step : Step = jsonToObject(ctx.body())
        if (stepDAO.updateByActivityId(
                stepId = ctx.pathParam("step-id").toInt(),
                stepToUpdate = step) != 0)
            ctx.status(204)
        else
            ctx.status(404)
    }
}