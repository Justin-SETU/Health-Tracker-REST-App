package ie.setu.controllers

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.joda.JodaModule
import ie.setu.domain.repository.UserDAO

import io.javalin.http.Context

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import ie.setu.domain.Activity

import ie.setu.domain.repository.ActivityDAO

//main endpoints and http requests for handling API requests, handles different things
object ActivityController {
    private val userDao = UserDAO()
    private val activityDAO = ActivityDAO()

    //--------------------------------------------------------------
    // ActivityDAO specifics
    //-------------------------------------------------------------

    fun getAllActivities(ctx: Context) {
        //mapper handles the deserialization of Joda date into a String.
        val mapper = jacksonObjectMapper()
            .registerModule(JodaModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        val activities = activityDAO.getAll()
        if (activities.size != 0) {
            ctx.json(mapper.writeValueAsString( activities ))
            ctx.status(200)
        } else {
            ctx.status(404)
        }
    }

    fun getActivitiesByUserId(ctx: Context) {
        if (userDao.findById(ctx.pathParam("user-id").toInt()) != null) {
            val activities = activityDAO.findByUserId(ctx.pathParam("user-id").toInt())
            if (activities.isNotEmpty()) {
                //mapper handles the deserialization of Joda date into a String.
                val mapper = jacksonObjectMapper()
                    .registerModule(JodaModule())
                    .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                ctx.json(mapper.writeValueAsString(activities))
                ctx.status(200)
            }
            else {
                ctx.status(404)
            }
        }
    }

    fun addActivity(ctx: Context) {
        //mapper handles the serialisation of Joda date into a String.
        val mapper = jacksonObjectMapper()
            .registerModule(JodaModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        val activity = mapper.readValue<Activity>(ctx.body())
        val userId = userDao.findById(activity.userId)
        if (userId != null) {
            activityDAO.save(activity)
            ctx.json(activity)
            ctx.status(201)
        }
        else {
            ctx.status(404)
        }
    }


    fun deleteActivityById(ctx: Context) {
        if((activityDAO.delete(ctx.pathParam("id").toInt()))!=0){
            ctx.status(204)
        } else {
            ctx.status(404)
        }
    }

    //update the activity as per activity id
    fun updateActivity(ctx: Context) {

        val mapper = jacksonObjectMapper().registerModule(JodaModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        val activityUpdates = mapper.readValue<Activity>(ctx.body())
        if((activityDAO.updateActivity(id = ctx.pathParam("id").toInt(), activity=activityUpdates))!=0){
            ctx.status(204)
        }else {
            ctx.status(404)
        }
    }



}