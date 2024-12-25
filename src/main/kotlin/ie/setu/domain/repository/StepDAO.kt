package ie.setu.domain.repository

import ie.setu.domain.Step
import ie.setu.domain.db.Steps
import ie.setu.utils.mapToStep
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

//workout data access objects that handles database operations
class StepDAO {

    //Get all the activities in the database regardless of user id
    fun getAll(): ArrayList<Step> {
        val stepsList: ArrayList<Step> = arrayListOf()
        transaction {
            Steps.selectAll().map { stepsList.add(mapToStep(it)) }
        }
        return stepsList
    }

    //Find a specific workout by workout ids
    fun findByStepId(id: Int): Step?{
        return transaction {
            Steps.selectAll().where { Steps.id eq id}.map{ mapToStep(it) }.firstOrNull()
        }
    }

    //Find all steps for a specific user id
    fun findByUserId(userId: Int): List<Step>{
        return transaction {
            Steps.selectAll().where {Steps.userId eq userId}.map { mapToStep(it) }
        }
    }

    //Save an workout to the database
    fun save(steps: Step){
        transaction {
            Steps.insert {
                it[distance] = steps.distance
                it[stepcount] = steps.stepcount
                it[userId] = steps.userId
            }
        }
    }

    //delete by user id of an step from database
    fun delete(id: Int): Int {
        return transaction {
            Steps.deleteWhere { Steps.id eq id}
        }
    }

    //update and step in the database with step id
    fun updateStep(id: Int, step: Step): Int{
        return transaction {
            Steps.update({ Steps.id eq id }) {
                it[distance] = step.distance
                it[stepcount] = step.stepcount
                it[userId] = step.userId
            }
        }
    }

}