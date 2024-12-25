package ie.setu.controllers

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.joda.JodaModule
import ie.setu.domain.repository.UserDAO

import io.javalin.http.Context

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import ie.setu.domain.Water
import ie.setu.domain.repository.WaterDAO

//main endpoints and http requests for handling API requests, handles different things
object WaterController {
    private val userDao = UserDAO()
    private val waterDAO = WaterDAO()

    //--------------------------------------------------------------
    // WaterDAO specifics
    //-------------------------------------------------------------

    fun getAllWater(ctx: Context) {
        //mapper handles the deserialization of Joda date into a String.
        val mapper = jacksonObjectMapper()
            .registerModule(JodaModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        val waters = waterDAO.getAll()
        if (waters.size != 0) {
            ctx.json(mapper.writeValueAsString( waters ))
            ctx.status(200)
        } else {
            ctx.status(404)
        }
    }


    fun getWaterByUserId(ctx: Context) {
        if (userDao.findById(ctx.pathParam("user-id").toInt()) != null) {
            val waters = waterDAO.findByUserId(ctx.pathParam("user-id").toInt())
            if (waters.isNotEmpty()) {
                //mapper handles the deserialization of Joda date into a String.
                val mapper = jacksonObjectMapper()
                    .registerModule(JodaModule())
                    .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                ctx.json(mapper.writeValueAsString(waters))
                ctx.status(200)
            }
            else {
                ctx.status(404)
            }
        }
    }

    fun addWater(ctx: Context) {
        //mapper handles the serialisation of Joda date into a String.
        val mapper = jacksonObjectMapper()
            .registerModule(JodaModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        val water = mapper.readValue<Water>(ctx.body())
        val userId = userDao.findById(water.userId)
        if (userId != null) {
            waterDAO.save(water)
            ctx.json(water)
            ctx.status(201)
        }
        else {
            ctx.status(404)
        }
    }


    fun deleteWaterById(ctx: Context) {
        if((waterDAO.delete(ctx.pathParam("id").toInt()))!=0){
            ctx.status(204)
        } else {
            ctx.status(404)
        }
    }

    //update the water as per water id
    fun updateWater(ctx: Context) {

        val mapper = jacksonObjectMapper().registerModule(JodaModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        val waterUpdates = mapper.readValue<Water>(ctx.body())
        if((waterDAO.updateWater(id = ctx.pathParam("id").toInt(), water=waterUpdates))!=0){
            ctx.status(204)
        }else {
            ctx.status(404)
        }
    }



}