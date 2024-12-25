package ie.setu.controllers

import ie.setu.domain.Step
import ie.setu.domain.repository.UserDAO
import io.javalin.http.Context
import ie.setu.domain.repository.StepDAO
import ie.setu.utils.jsonToObject

//main endpoints and http requests for handling API requests, handles different things
object StepController {
    private val userDao = UserDAO()
    private val stepDAO = StepDAO()
    //--------------------------------------------------------------
    // StepDAO specifics
    //-------------------------------------------------------------

    fun getAll(ctx: Context) {
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



}