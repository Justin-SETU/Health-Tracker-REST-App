package ie.setu.domain.repository

import ie.setu.domain.Log
import ie.setu.domain.db.Logs
import ie.setu.utils.mapToLog
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

//workout data access objects that handles database operations
class LogDAO {

    //Get all the logs in the database regardless of user id
    fun getAll(): ArrayList<Log> {
        val logsList: ArrayList<Log> = arrayListOf()
        transaction {
            Logs.selectAll().map { logsList.add(mapToLog(it)) }
        }
        return logsList
    }

    //Find a specific workout by workout id
    fun findByLogId(id: Int): Log?{
        return transaction {
            Logs.selectAll().where { Logs.id eq id}.map{mapToLog(it)}.firstOrNull()
        }
    }

    //Find all logs for a specific user id
    fun findByUserId(userId: Int): List<Log>{
        return transaction {
            Logs.selectAll().where {Logs.userId eq userId}.map {mapToLog(it)}
        }
    }

    //Save an workout to the database
    fun save(logs: Log){
        transaction {
            Logs.insert {
                it[status_type] = logs.status_type
                it[summary] = logs.summary
                it[started] = logs.started
                it[userId] = logs.userId
            }
        }
    }

    //delete by user id of an workout from database
    fun delete(id: Int): Int {
        return transaction {
            Logs.deleteWhere { Logs.id eq id}
        }
    }

    //update and workout in the database with workout id
    fun updateLog(id: Int, logs: Log): Int{
        return transaction {
            Logs.update({ Logs.id eq id }) {
                it[status_type] = logs.status_type
                it[summary] = logs.summary
                it[started] = logs.started
                it[userId] = logs.userId
            }
        }
    }

}