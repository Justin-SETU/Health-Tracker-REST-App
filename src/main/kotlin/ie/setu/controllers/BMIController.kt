package ie.setu.controllers

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

import com.fasterxml.jackson.module.kotlin.readValue
import ie.setu.domain.Bmi
import ie.setu.domain.repository.BmiDAO


import io.javalin.http.Context


object BMIController {

    private val bmiDAO = BmiDAO()

    fun saveBmi(ctx: Context) {
        val mapper = jacksonObjectMapper()
            .registerModule(JavaTimeModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        val bmi = mapper.readValue<Bmi>(ctx.body())
        bmiDAO.saveBmi(bmi)
        ctx.json(bmi)
    }


    fun getBmi(ctx: Context) {
        val mapper = jacksonObjectMapper()
        .registerModule(JavaTimeModule())
        .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        ctx.json(mapper.writeValueAsString(bmiDAO.getBmilist()))
    }

    fun deleteBmi(ctx: Context) {
        bmiDAO.deleteBmi(ctx.pathParam("id").toInt())
    }

    fun updateBmi(ctx: Context) {
        val mapper = jacksonObjectMapper().registerModule(JavaTimeModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        val updateBmi = mapper.readValue<Bmi>(ctx.body())
        bmiDAO.updateBmi(id = ctx.pathParam("id").toInt(), bmi = updateBmi)
    }
}


