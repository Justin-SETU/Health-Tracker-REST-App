package ie.setu.domain.repository

import ie.setu.domain.Sleep
import ie.setu.domain.Workout
import ie.setu.domain.db.Sleeps
import ie.setu.utils.mapToWorkout
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

//workout data access objects that handles database operations
class SleepDAO {

    //Get all the sleeps in the database regardless of user id
    fun getAll(): ArrayList<Workout> {
        val workoutsList: ArrayList<Workout> = arrayListOf()
        transaction {
            Sleeps.selectAll().map { workoutsList.add(mapToWorkout(it)) }
        }
        return workoutsList
    }

    //Find a specific workout by workout ids
    fun findBySleepId(id: Int): Workout?{
        return transaction {
            Sleeps.selectAll().where { Sleeps.id eq id}.map{mapToWorkout(it)}.firstOrNull()
        }
    }

    //Find all sleeps for a specific user id
    fun findByUserId(userId: Int): List<Workout>{
        return transaction {
            Sleeps.selectAll().where {Sleeps.userId eq userId}.map {mapToWorkout(it)}
        }
    }

    //Save an workout to the database
    fun save(sleeps: Sleep){
        transaction {
            Sleeps.insert {
                it[duration] = sleeps.duration
                it[started] = sleeps.started
                it[userId] = sleeps.userId
            }
        }
    }

    //delete by user id of an sleep from database
    fun delete(id: Int): Int {
        return transaction {
            Sleeps.deleteWhere { Sleeps.id eq id}
        }
    }

    //update and sleep in the database with sleep id
    fun updateSleep(id: Int, sleep: Sleep): Int{
        return transaction {
            Sleeps.update({ Sleeps.id eq id }) {
                it[duration] = sleep.duration
                it[started] = sleep.started
                it[userId] = sleep.userId
            }
        }
    }

}