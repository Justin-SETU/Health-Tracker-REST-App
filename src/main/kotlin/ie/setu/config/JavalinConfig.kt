package ie.setu.config

import ie.setu.controllers.*
import io.javalin.Javalin
import io.javalin.json.JavalinJackson
import ie.setu.utils.jsonObjectMapper

//configuring the javalin instance, server settings, custom json configuration, port setting, exception handling
class JavalinConfig {

    fun startJavalinService(): Javalin {
        val app = Javalin.create {
            //add this jsonMapper to serialise objects to json
            it.jsonMapper(JavalinJackson(jsonObjectMapper()))
        }
            .apply{
                exception(Exception::class.java) { e, ctx -> e.printStackTrace() }
                error(404) { ctx -> ctx.json("404 - Not Found") }
            }
            .start(getRemoteAssignedPort())

        registerRoutes(app)
        return app
    }

    private fun registerRoutes(app: Javalin) {
        //User Features
        //User Login
        app.post("/api/users/login-user", UserController::loginUser)
        //Show users
        app.get("/api/users", UserController::getAllUsers)
        //create a new user
        app.post("/api/users/create-user", UserController::addUser)
        //Delete a user
        app.delete("/api/users/delete/{user-id}", UserController::deleteUser)
        //Update a user
        app.patch("/api/users/update/{user-id}", UserController::updateUser)
        //Addons
        app.get("/api/users/id/{user-id}", UserController::getUserByUserId)
        app.get("/api/users/email/{email}", UserController::getUserByEmail)
        //-----------------------------

        //Activity Features
        app.get("/api/activities", ActivityController ::getAllActivities)
        app.post("/api/activities/add", ActivityController::addActivity)
        app.get("/api/activities/{user-id}", ActivityController:: getActivitiesByUserId)
        app.delete("/api/activities/{id}", ActivityController::deleteActivityById)
        app.patch("/api/activities/update/{id}", ActivityController::updateActivity)
        //--------------------------------

        //Bmi Features
        app.get("/api/bmi", BMIController ::getAllBmis)
        app.post("/api/bmi/add", BMIController::addBmi)
        app.get("/api/bmi/{user-id}", BMIController:: getBmisByUserId)
        app.delete("/api/bmi/{id}", BMIController::deleteBmiById)
        app.patch("/api/bmi/update/{id}", BMIController::updateBmi)
        //----------------------------------

        //Workout Features
        app.get("/api/workouts", WorkoutController::getAllWorkouts)
        app.post("/api/workouts/add", WorkoutController::addWorkout)
        app.get("/api/users/{user-id}/workouts", WorkoutController::getWorkoutsByUserId)
        app.delete("/api/workouts/{user-id}", WorkoutController::deleteWorkoutById)
        //----------------------------------

        //Recommendation Feature

    }
    
    private fun getRemoteAssignedPort(): Int {
        val remotePort = System.getenv("PORT")
        return if (remotePort != null) {
            Integer.parseInt(remotePort)
        } else 7001
    }
}