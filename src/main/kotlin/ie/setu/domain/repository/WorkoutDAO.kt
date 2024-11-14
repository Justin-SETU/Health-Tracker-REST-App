package ie.setu.domain.repository

import ie.setu.domain.Workout
import ie.setu.domain.db.Workouts
import ie.setu.utils.mapToWorkout
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

class WorkoutDAO {

    //Get all the workouts in the database regardless of user id
    fun getAll(): ArrayList<Workout> {
        val workoutsList: ArrayList<Workout> = arrayListOf()
        transaction {
            Workouts.selectAll().map {
                workoutsList.add(mapToWorkout(it)) }
        }
        return workoutsList
    }

    //Find a specific Workout by Workout id
    fun findByWorkoutId(id: Int): Workout?{
        return transaction {
            Workouts
                .selectAll().where { Workouts.id eq id}
                .map{mapToWorkout(it)}
                .firstOrNull()
        }
    }

    //Find all workouts for a specific user id
    fun findByUserId(userId: Int): List<Workout>{
        return transaction {
            Workouts
                .selectAll().where {Workouts.userId eq userId}
                .map {mapToWorkout(it)}
        }
    }

    //Save an Workout to the database
    fun save(Workout: Workout){
        transaction {
            Workouts.insert {
                it[workout] = Workout.workout
                it[duration] = Workout.duration
                it[started] = Workout.started
                it[userId] = Workout.userId
            }
        }
    }

    //delete an activity from database
    fun delete(id: Int){
        return transaction {
            Workouts.deleteWhere { Workouts.id eq id}
        }
    }



}