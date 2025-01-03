package ie.setu.utils

import ie.setu.domain.*
import ie.setu.domain.db.*
import ie.setu.domain.db.Logs.summary
import org.jetbrains.exposed.sql.ResultRow
import ie.setu.domain.Log
import ie.setu.domain.db.Logs


//designed to map a database row with a data object
//When query database, results are returned as ResultRow

//converts results from database into user object
fun mapToUser(it: ResultRow) = User(
    id = it[Users.id],
    name = it[Users.name],
    email = it[Users.email],
    password = it[Users.password]
)
//converts results from database into activity
fun mapToActivity(it: ResultRow) = Activity(
    id = it[Activities.id],
    description = it[Activities.description],
    duration = it[Activities.duration],
    started = it[Activities.started],
    calories = it[Activities.calories],
    userId = it[Activities.userId]
)

//converts results from database into bmi
fun mapToBmi(it: ResultRow) = Bmi(
    id = it[Bmis.id],
    height = it[Bmis.height],
    weight = it[Bmis.weight],
    bmivalue = it[Bmis.bmivalue],
    started = it[Bmis.started],
    userId = it[Bmis.userId]
)

fun mapToWorkout(it: ResultRow) = Workout(
    id = it[Workouts.id],
    workout = it[Workouts.workout],
    duration = it[Workouts.duration],
    started = it[Workouts.started],
    userId = it[Workouts.userId]
)

fun mapToLog(it: ResultRow) = Log(
    id = it[Logs.id],
    started = it[Logs.started],
    summary = it[Logs.summary],
    status_type = it[Logs.status_type],
    userId = it[Logs.userId]
)

fun mapToStep(it: ResultRow) = Step(
    id = it[Steps.id],
    distance = it[Steps.distance],
    stepcount = it[Steps.stepcount],
    userId = it[Steps.userId]
)

fun mapToSleep(it: ResultRow) = Sleep(
    id = it[Sleeps.id],
    duration = it[Sleeps.duration],
    started = it[Sleeps.started],
    userId = it[Sleeps.userId]
)

fun mapToWater(it: ResultRow) = Water(
    id = it[Waters.id],
    waterintake = it[Waters.waterintake],
    started = it[Waters.started],
    userId = it[Waters.userId]
)

fun mapToMeal(it: ResultRow) = Meal(
    id = it[Meals.id],
    food = it[Meals.food],
    calories = it[Meals.calories],
    started = it[Meals.started],
    userId = it[Meals.userId]
)