package ie.setu.controllers

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.joda.JodaModule
import ie.setu.domain.repository.UserDAO

import io.javalin.http.Context

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import ie.setu.domain.Log

import ie.setu.domain.repository.LogDAO

//main endpoints and http requests for handling API requests, handles different things
object LogController {
    private val userDao = UserDAO()
    private val logDAO = LogDAO()

    //--------------------------------------------------------------
    // LogDAO specifics
    //-------------------------------------------------------------

    fun getAllLogs(ctx: Context) {
        //mapper handles the deserialization of Joda date into a String.
        val mapper = jacksonObjectMapper()
            .registerModule(JodaModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        val logs = logDAO.getAll()
        if (logs.size != 0) {
            ctx.json(mapper.writeValueAsString( logs ))
            ctx.status(200)
        } else {
            ctx.status(404)
        }
    }

    fun getLogsByUserId(ctx: Context) {
        if (userDao.findById(ctx.pathParam("user-id").toInt()) != null) {
            val logs = logDAO.findByUserId(ctx.pathParam("user-id").toInt())
            if (logs.isNotEmpty()) {
                //mapper handles the deserialization of Joda date into a String.
                val mapper = jacksonObjectMapper()
                    .registerModule(JodaModule())
                    .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                ctx.json(mapper.writeValueAsString(logs))
                ctx.status(200)
            }
            else {
                ctx.status(404)
            }
        }
    }

    fun addLog(ctx: Context) {
        //mapper handles the serialisation of Joda date into a String.
        val mapper = jacksonObjectMapper()
            .registerModule(JodaModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        val log = mapper.readValue<Log>(ctx.body())
        val userId = userDao.findById(log.userId)
        if (userId != null) {
            logDAO.save(log)
            ctx.json(log)
            ctx.status(201)
        }
        else {
            ctx.status(404)
        }
    }


    fun deleteLogById(ctx: Context) {
        if((logDAO.delete(ctx.pathParam("id").toInt()))!=0){
            ctx.status(204)
        } else {
            ctx.status(404)
        }
    }

    //update the log as per log id
    fun updateLog(ctx: Context) {

        val mapper = jacksonObjectMapper().registerModule(JodaModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        val logUpdates = mapper.readValue<Log>(ctx.body())
        if((logDAO.updateLog(id = ctx.pathParam("id").toInt(), logs =logUpdates))!=0){
            ctx.status(204)
        }else {
            ctx.status(404)
        }
    }


}