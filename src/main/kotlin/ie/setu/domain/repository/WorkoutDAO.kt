package ie.setu.domain.repository

import ie.setu.domain.Workout
import ie.setu.domain.db.Workouts
import ie.setu.utils.mapToWorkout
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

//workout data access objects that handles database operations
class WorkoutDAO {

    //Get all the workouts in the database regardless of user id
    fun getAll(): ArrayList<Workout> {
        val workoutsList: ArrayList<Workout> = arrayListOf()
        transaction {
            Workouts.selectAll().map { workoutsList.add(mapToWorkout(it)) }
        }
        return workoutsList
    }

    //Find a specific workout by workout id
    fun findByWorkoutId(id: Int): Workout?{
        return transaction {
            Workouts.selectAll().where { Workouts.id eq id}.map{mapToWorkout(it)}.firstOrNull()
        }
    }

    //Find all workouts for a specific user id
    fun findByUserId(userId: Int): List<Workout>{
        return transaction {
            Workouts.selectAll().where {Workouts.userId eq userId}.map {mapToWorkout(it)}
        }
    }

    //Save an workout to the database
    fun save(workouts: Workout){
        transaction {
            Workouts.insert {
                it[workout] = workouts.workout
                it[duration] = workouts.duration
                it[started] = workouts.started
                it[userId] = workouts.userId
            }
        }
    }

    //delete by user id of an workout from database
    fun delete(id: Int): Int {
        return transaction {
            Workouts.deleteWhere { Workouts.id eq id}
        }
    }

    //update and workout in the database with workout id
    fun updateWorkout(id: Int, workouts: Workout): Int{
        return transaction {
            Workouts.update({ Workouts.id eq id }) {
                it[workout] = workouts.workout
                it[duration] = workouts.duration
                it[started] = workouts.started
                it[userId] = workouts.userId
            }
        }
    }

}