package ie.setu.controllers

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.joda.JodaModule
import ie.setu.domain.repository.UserDAO

import io.javalin.http.Context

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import ie.setu.domain.Workout

import ie.setu.domain.repository.WorkoutDAO

//main endpoints and http requests for handling API requests, handles different things
object WorkoutController {
    private val userDao = UserDAO()
    private val workoutDAO = WorkoutDAO()

    //--------------------------------------------------------------
    // WorkoutDAO specifics
    //-------------------------------------------------------------

    fun getAllWorkouts(ctx: Context) {
        //mapper handles the deserialization of Joda date into a String.
        val mapper = jacksonObjectMapper()
            .registerModule(JodaModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        val workouts = workoutDAO.getAll()
        if (workouts.size != 0) {
            ctx.json(mapper.writeValueAsString( workouts ))
            ctx.status(200)
        } else {
            ctx.status(404)
        }
    }

    fun getWorkoutsByUserId(ctx: Context) {
        if (userDao.findById(ctx.pathParam("user-id").toInt()) != null) {
            val workouts = workoutDAO.findByUserId(ctx.pathParam("user-id").toInt())
            if (workouts.isNotEmpty()) {
                //mapper handles the deserialization of Joda date into a String.
                val mapper = jacksonObjectMapper()
                    .registerModule(JodaModule())
                    .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                ctx.json(mapper.writeValueAsString(workouts))
                ctx.status(200)
            }
            else {
                ctx.status(404)
            }
        }
    }

    fun addWorkout(ctx: Context) {
        //mapper handles the serialisation of Joda date into a String.
        val mapper = jacksonObjectMapper()
            .registerModule(JodaModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        val workout = mapper.readValue<Workout>(ctx.body())
        val userId = userDao.findById(workout.userId)
        if (userId != null) {
            workoutDAO.save(workout)
            ctx.json(workout)
            ctx.status(201)
        }
        else {
            ctx.status(404)
        }
    }


    fun deleteWorkoutById(ctx: Context) {
        if((workoutDAO.delete(ctx.pathParam("id").toInt()))!=0){
            ctx.status(204)
        } else {
            ctx.status(404)
        }
    }

    //update the workout as per workout id
    fun updateWorkout(ctx: Context) {

        val mapper = jacksonObjectMapper().registerModule(JodaModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        val workoutUpdates = mapper.readValue<Workout>(ctx.body())
        if((workoutDAO.updateWorkout(id = ctx.pathParam("id").toInt(), workouts =workoutUpdates))!=0){
            ctx.status(204)
        }else {
            ctx.status(404)
        }
    }


}