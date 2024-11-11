package ie.setu.controllers

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import ie.setu.domain.Bmi
import ie.setu.domain.repository.BmiDAO
import ie.setu.domain.repository.UserDAO
import io.javalin.http.Context
import ie.setu.utils.jsonToObject

object BMIController {

    private val userDao = UserDAO()
    private var bmiDAO = BmiDAO()


    fun calculateBmi(ctx: Context) {
        val bmi: Bmi = jsonToObject(ctx.body())
        val userId = userDao.findById(bmi.userId)
        if (userId != null) {
            val bmiId = bmiDAO.findBmi(bmi)
            bmi.id = bmiId
            ctx.json(bmi)
            ctx.status(201)
        } else {
            ctx.status(404)
        }
    }

}


