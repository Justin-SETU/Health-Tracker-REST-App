package ie.setu.config

import ie.setu.controllers.*
import io.javalin.Javalin
import io.javalin.json.JavalinJackson
import ie.setu.utils.jsonObjectMapper
import io.javalin.vue.VueComponent

//configuring the javalin instance, server settings, custom json configuration, port setting, exception handling
class JavalinConfig {

    fun startJavalinService(): Javalin {
        val app = Javalin.create{
            //added this jsonMapper for our integration tests - serialise objects to json
            it.jsonMapper(JavalinJackson(jsonObjectMapper()))
            it.staticFiles.enableWebjars()
            it.vue.vueInstanceNameInJs = "app" // only required for Vue 3, is defined in layout.html
        }.apply {
            exception(Exception::class.java) { e, _ -> e.printStackTrace() }
            error(404) { ctx -> ctx.json("404 : Not Found") }
        }.start(getRemoteAssignedPort())

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
        app.get("/api/users/{user-id}", UserController::getUserByUserId)
        app.get("/api/users/email/{email}", UserController::getUserByEmail)
        app.get("/api/users/{user-id}/activities", ActivityController::getActivitiesByUserId)
        //-----------------------------

        //Activity Features
        app.get("/api/activities", ActivityController ::getAllActivities)
        app.post("/api/activities/add", ActivityController::addActivity)
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
        app.get("/api/users/workouts/{user-id}", WorkoutController::getWorkoutsByUserId)
        app.delete("/api/workouts/{user-id}", WorkoutController::deleteWorkoutById)
        app.patch("/api/workouts/update/{id}", WorkoutController::updateWorkout)
        //----------------------------------

        //Status Features
        app.get("/api/log", LogController ::getAllLogs)
        app.post("/api/log/add", LogController::addLog)
        app.get("/api/log/{user-id}", LogController:: getLogsByUserId)
        app.delete("/api/log/{id}", LogController::deleteLogById)
        app.patch("/api/log/update/{id}", LogController::updateLog)
        //---------------------

        //Step Features
        app.get("/api/steps", StepController ::getAllStep)
        app.post("/api/steps/add", StepController::addStep)
        app.get("/api/steps/{user-id}", StepController:: getStepByUserId)
        app.delete("/api/steps/{id}", StepController::deleteStepById)
        app.patch("/api/steps/update/{id}", StepController::updateStep)
        //----------------------------------

        //Sleep Features
        app.get("/api/sleep", SleepController ::getAllSleep)
        app.post("/api/sleep/add", SleepController::addSleep)
        app.get("/api/sleep/{user-id}", SleepController:: getSleepByUserId)
        app.delete("/api/sleep/{id}", SleepController::deleteSleepById)
        app.patch("/api/sleep/update/{id}", SleepController::updateSleep)
        //----------------------------------

        //Water Features
        app.get("/api/water", WaterController ::getAllWater)
        app.post("/api/water/add", WaterController::addWater)
        app.get("/api/water/{user-id}", WaterController:: getWaterByUserId)
        app.delete("/api/water/{id}", WaterController::deleteWaterById)
        app.patch("/api/water/update/{id}", WaterController::updateWater)
        //----------------------------------

        //Meal Features
        app.get("/api/meal", MealController ::getAllMeal)
        app.post("/api/meal/add", MealController::addMeal)
        app.get("/api/meal/{user-id}", MealController:: getMealByUserId)
        app.delete("/api/meal/{id}", MealController::deleteMealById)
        app.patch("/api/meal/update/{id}", MealController::updateMeal)
        //----------------------------------



        //VUE
        // The @routeComponent that we added in layout.html earlier will be replaced
        // by the String inside the VueComponent. This means a call to / will load
        // the layout and display our <home-page> component.
        app.get("/", VueComponent("<home-page></home-page>"))
        app.get("/users", VueComponent("<user-overview></user-overview>"))
        app.get("/users/{user-id}", VueComponent("<user-profile></user-profile>"))
        app.get("/users/{user-id}/activities", VueComponent("<user-activity-overview></user-activity-overview>"))
        app.get("/activities", VueComponent("<activity-overview></activity-overview>"))
        app.get("/bmi", VueComponent("<bmi-overview></bmi-overview>"))
        app.get("/users/{user-id}/bmi", VueComponent("<user-bmi-overview></user-bmi-overview>"))


    }
    
    private fun getRemoteAssignedPort(): Int {
        val remotePort = System.getenv("PORT")
        return if (remotePort != null) {
            Integer.parseInt(remotePort)
        } else 7001
    }
}