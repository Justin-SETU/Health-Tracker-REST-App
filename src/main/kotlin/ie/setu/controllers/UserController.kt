package ie.setu.controllers

import ie.setu.domain.repository.UserDAO

import io.javalin.http.Context

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import ie.setu.domain.LoginModel

import ie.setu.domain.User


//main endpoints and http requests for handling API requests, handles different things
object UserController {

    private val userDao = UserDAO()

    //Api end point for user login using email and password
    fun loginUser(ctx: Context) {
        val mapper = jacksonObjectMapper()
        val user = mapper.readValue<LoginModel>(ctx.body())
        val userExist = userDao.loginUser(user)

        if (userExist) {
            ctx.status(200).json(mapOf("success" to true))
        }
        else
        {
            ctx.status(400).json(mapOf("error" to "error"))
        }
    }


    //Register User
    fun addUser(ctx: Context) {
        val mapper = jacksonObjectMapper()
        val user = mapper.readValue<User>(ctx.body())
        userDao.save(user)
        ctx.json(user)
    }

    //Show user details
    fun getAllUsers(ctx: Context) {
        val users = userDao.getAll()
        if (users.size != 0) {
            ctx.status(200)
            ctx.json(users)
        }
        else{
            ctx.status(404)
        }

    }

    //Api request to delete a used
    fun deleteUser(ctx: Context){
        if (userDao.delete(ctx.pathParam("user-id").toInt()) != 0)
            ctx.status(204)
        else
            ctx.status(404)
    }

    //Api request to update a user
    fun updateUser(ctx: Context){
        val mapper = jacksonObjectMapper()
        val userUpdates : User = mapper.readValue<User>(ctx.body())
        userDao.update(id = ctx.pathParam("user-id").toInt(), user=userUpdates)
        ctx.status(204)
    }





    //--------------------------------------------------
    //not added in features yet
    fun getUserByUserId(ctx: Context) {
        val user = userDao.findById(ctx.pathParam("user-id").toInt())
        if (user != null) {
            ctx.json(user)
        }
    }

    fun getUserByEmail(ctx: Context) {
        val user = userDao.findByEmail(ctx.pathParam("email"))
        if (user != null) {
            ctx.json(user)
        }
    }
    //------------------------------------------------------
}