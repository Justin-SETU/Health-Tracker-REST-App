package ie.setu.controllers

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.joda.JodaModule
import ie.setu.domain.repository.UserDAO

import io.javalin.http.Context

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import ie.setu.domain.Meal

import ie.setu.domain.repository.MealDAO

//main endpoints and http requests for handling API requests, handles different things
object MealController {
    private val userDao = UserDAO()
    private val mealDAO = MealDAO()

    //--------------------------------------------------------------
    // MealDAO specifics
    //-------------------------------------------------------------

    fun getAllMeal(ctx: Context) {
        //mapper handles the deserialization of Joda date into a String.
        val mapper = jacksonObjectMapper()
            .registerModule(JodaModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        val meals = mealDAO.getAll()
        if (meals.size != 0) {
            ctx.json(mapper.writeValueAsString( meals ))
            ctx.status(200)
        } else {
            ctx.status(404)
        }
    }


    fun getMealByUserId(ctx: Context) {
        if (userDao.findById(ctx.pathParam("user-id").toInt()) != null) {
            val meals = mealDAO.findByUserId(ctx.pathParam("user-id").toInt())
            if (meals.isNotEmpty()) {
                //mapper handles the deserialization of Joda date into a String.
                val mapper = jacksonObjectMapper()
                    .registerModule(JodaModule())
                    .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                ctx.json(mapper.writeValueAsString(meals))
                ctx.status(200)
            }
            else {
                ctx.status(404)
            }
        }
    }

    fun addMeal(ctx: Context) {
        //mapper handles the serialisation of Joda date into a String.
        val mapper = jacksonObjectMapper()
            .registerModule(JodaModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        val meal = mapper.readValue<Meal>(ctx.body())
        val userId = userDao.findById(meal.userId)
        if (userId != null) {
            mealDAO.save(meal)
            ctx.json(meal)
            ctx.status(201)
        }
        else {
            ctx.status(404)
        }
    }


    fun deleteMealById(ctx: Context) {
        if((mealDAO.delete(ctx.pathParam("id").toInt()))!=0){
            ctx.status(204)
        } else {
            ctx.status(404)
        }
    }

    //update the meal as per meal id
    fun updateMeal(ctx: Context) {

        val mapper = jacksonObjectMapper().registerModule(JodaModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        val mealUpdates = mapper.readValue<Meal>(ctx.body())
        if((mealDAO.updateMeal(id = ctx.pathParam("id").toInt(), meal=mealUpdates))!=0){
            ctx.status(204)
        }else {
            ctx.status(404)
        }
    }



}