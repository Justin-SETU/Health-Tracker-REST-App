package ie.setu.utils

import ie.setu.domain.Activity
import ie.setu.domain.Bmi
import ie.setu.domain.db.Activities

import ie.setu.domain.User
import ie.setu.domain.db.BmiValues
import ie.setu.domain.db.Users

import org.jetbrains.exposed.sql.ResultRow

//converts results from database into user classes
fun mapToUser(it: ResultRow) = User(
    id = it[Users.id],
    name = it[Users.name],
    email = it[Users.email],
    password = it[Users.password]
)
//converts results from database into activity classes
fun mapToActivity(it: ResultRow) = Activity(
    id = it[Activities.id],
    description = it[Activities.description],
    duration = it[Activities.duration],
    started = it[Activities.started],
    calories = it[Activities.calories],
    userId = it[Activities.userId]
)

//converts results from database into bmi classes
fun mapToBmi(it: ResultRow) = Bmi(
    id = it[BmiValues.id],
    weight = it[BmiValues.weight],
    height = it[BmiValues.height],
    userId = it[BmiValues.userId],
    bmiCalc = it[BmiValues.BmiCalc],
    timestamp = it[BmiValues.timestamp],
)