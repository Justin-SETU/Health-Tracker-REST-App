package ie.setu.controllers

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import ie.setu.domain.Workout
import ie.setu.domain.repository.RecommendDAO
import io.javalin.http.Context

object RecommendController {

    private val recommendDAO = RecommendDAO()

    fun getRecommendation(ctx: Context) {

        val mapper = jacksonObjectMapper()
        val workoutRequest = mapper.readValue<Workout>(ctx.body())

        val workoutType = workoutRequest.workout
        val userId = workoutRequest.userId

        val recommendations = recommendDAO.getRecommend(workoutType, userId)

        if(recommendations!=null){
            ctx.json(mapper.writeValueAsString(recommendations))
            ctx.status(201)
        }else {
            ctx.status(404)
        }

    }
}
