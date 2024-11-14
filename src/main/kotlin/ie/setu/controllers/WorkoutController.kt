package ie.setu.controllers

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.joda.JodaModule
import ie.setu.domain.repository.UserDAO

import io.javalin.http.Context

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import ie.setu.domain.Workout
import ie.setu.domain.repository.WorkoutDAO

object WorkoutController {
    private val workoutDAO = WorkoutDAO()
    private val userDao = UserDAO()

    //--------------------------------------------------------------
    // workoutDAO specifics
    //-------------------------------------------------------------

    fun getAllworkouts(ctx: Context) {
        //mapper handles the deserialization of Joda date into a String.
        val mapper = jacksonObjectMapper()
            .registerModule(JodaModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        ctx.json(mapper.writeValueAsString( workoutDAO.getAll() ))
    }

    fun getworkoutsByUserId(ctx: Context) {
        if (userDao.findById(ctx.pathParam("user-id").toInt()) != null) {
            val workouts = workoutDAO.findByUserId(ctx.pathParam("user-id").toInt())
            if (workouts.isNotEmpty()) {
                //mapper handles the deserialization of Joda date into a String.
                val mapper = jacksonObjectMapper()
                    .registerModule(JodaModule())
                    .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                ctx.json(mapper.writeValueAsString(workouts))
            }
        }
    }

    fun addworkout(ctx: Context) {
        //mapper handles the serialisation of Joda date into a String.
        val mapper = jacksonObjectMapper()
            .registerModule(JodaModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        val workout = mapper.readValue<Workout>(ctx.body())
        workoutDAO.save(workout)
        ctx.json(workout)
    }

    fun deleteWorkoutById(ctx: Context) {

        workoutDAO.delete(ctx.pathParam("id").toInt())
    }

}

