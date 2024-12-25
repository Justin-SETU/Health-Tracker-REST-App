package ie.setu.controllers

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.joda.JodaModule
import ie.setu.domain.repository.UserDAO

import io.javalin.http.Context

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import ie.setu.domain.Sleep

import ie.setu.domain.repository.SleepDAO

//main endpoints and http requests for handling API requests, handles different things
object SleepController {
    private val userDao = UserDAO()
    private val sleepDAO = SleepDAO()

    //--------------------------------------------------------------
    // SleepDAO specifics
    //-------------------------------------------------------------

    fun getAllSleep(ctx: Context) {
        //mapper handles the deserialization of Joda date into a String.
        val mapper = jacksonObjectMapper()
            .registerModule(JodaModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        val sleeps = sleepDAO.getAll()
        if (sleeps.size != 0) {
            ctx.json(mapper.writeValueAsString( sleeps ))
            ctx.status(200)
        } else {
            ctx.status(404)
        }
    }


    fun getSleepByUserId(ctx: Context) {
        if (userDao.findById(ctx.pathParam("user-id").toInt()) != null) {
            val sleeps = sleepDAO.findByUserId(ctx.pathParam("user-id").toInt())
            if (sleeps.isNotEmpty()) {
                //mapper handles the deserialization of Joda date into a String.
                val mapper = jacksonObjectMapper()
                    .registerModule(JodaModule())
                    .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                ctx.json(mapper.writeValueAsString(sleeps))
                ctx.status(200)
            }
            else {
                ctx.status(404)
            }
        }
    }

    fun addSleep(ctx: Context) {
        //mapper handles the serialisation of Joda date into a String.
        val mapper = jacksonObjectMapper()
            .registerModule(JodaModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        val sleep = mapper.readValue<Sleep>(ctx.body())
        val userId = userDao.findById(sleep.userId)
        if (userId != null) {
            sleepDAO.save(sleep)
            ctx.json(sleep)
            ctx.status(201)
        }
        else {
            ctx.status(404)
        }
    }


    fun deleteSleepById(ctx: Context) {
        if((sleepDAO.delete(ctx.pathParam("id").toInt()))!=0){
            ctx.status(204)
        } else {
            ctx.status(404)
        }
    }

    //update the sleep as per sleep id
    fun updateSleep(ctx: Context) {

        val mapper = jacksonObjectMapper().registerModule(JodaModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        val sleepUpdates = mapper.readValue<Sleep>(ctx.body())
        if((sleepDAO.updateSleep(id = ctx.pathParam("id").toInt(), sleep=sleepUpdates))!=0){
            ctx.status(204)
        }else {
            ctx.status(404)
        }
    }



}