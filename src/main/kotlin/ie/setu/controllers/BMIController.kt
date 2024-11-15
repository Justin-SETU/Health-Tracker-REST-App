package ie.setu.controllers

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.joda.JodaModule
import ie.setu.domain.repository.UserDAO

import io.javalin.http.Context

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import ie.setu.domain.Bmi

import ie.setu.domain.repository.BmiDAO

//main endpoints and http requests for handling API requests, handles different things
object BMIController {
    private val userDao = UserDAO()
    private val bmiDAO = BmiDAO()

    //--------------------------------------------------------------
    // BmiDAO specifics
    //-------------------------------------------------------------

    fun getAllBmis(ctx: Context) {
        //mapper handles the deserialization of Joda date into a String.
        val mapper = jacksonObjectMapper()
            .registerModule(JodaModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        val bmis = bmiDAO.getAll()
        if (bmis.size != 0) {
            ctx.json(mapper.writeValueAsString( bmis ))
            ctx.status(200)
        } else {
            ctx.status(404)
        }
    }

    fun getBmisByUserId(ctx: Context) {
        if (userDao.findById(ctx.pathParam("user-id").toInt()) != null) {
            val bmis = bmiDAO.findByUserId(ctx.pathParam("user-id").toInt())
            if (bmis.isNotEmpty()) {
                //mapper handles the deserialization of Joda date into a String.
                val mapper = jacksonObjectMapper()
                    .registerModule(JodaModule())
                    .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                ctx.json(mapper.writeValueAsString(bmis))
                ctx.status(200)
            }
            else {
                ctx.status(404)
            }
        }
    }

    fun addBmi(ctx: Context) {
        //mapper handles the serialisation of Joda date into a String.
        val mapper = jacksonObjectMapper()
            .registerModule(JodaModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        val bmi = mapper.readValue<Bmi>(ctx.body())
        val userId = userDao.findById(bmi.userId)
        if (userId != null) {
            bmiDAO.save(bmi)
            ctx.json(bmi)
            ctx.status(201)
        }
        else {
            ctx.status(404)
        }
    }


    fun deleteBmiById(ctx: Context) {
        if((bmiDAO.delete(ctx.pathParam("id").toInt()))!=0){
            ctx.status(204)
        } else {
            ctx.status(404)
        }
    }

    //update the bmi as per bmi id
    fun updateBmi(ctx: Context) {

        val mapper = jacksonObjectMapper().registerModule(JodaModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        val bmiUpdates = mapper.readValue<Bmi>(ctx.body())
        if((bmiDAO.updateBmi(id = ctx.pathParam("id").toInt(), bmi=bmiUpdates))!=0){
            ctx.status(204)
        }else {
            ctx.status(404)
        }
    }


}