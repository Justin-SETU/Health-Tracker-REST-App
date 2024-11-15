package ie.setu.domain.repository

import ie.setu.domain.Bmi
import ie.setu.domain.db.Bmis
import ie.setu.utils.mapToBmi
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

//bmi data access objects that handles database operations
class BmiDAO {

    //Get all the Bmi in the database regardless of user id
    fun getAll(): ArrayList<Bmi> {
        val BmisList: ArrayList<Bmi> = arrayListOf()
        transaction {
            Bmis.selectAll().map { BmisList.add(mapToBmi(it)) }
        }
        return BmisList
    }

    //Find a specific bmi by bmi id
    fun findByBmiId(id: Int): Bmi?{
        return transaction {
            Bmis.selectAll().where { Bmis.id eq id}.map{mapToBmi(it)}.firstOrNull()
        }
    }

    //Find all Bmi for a specific user id
    fun findByUserId(userId: Int): List<Bmi>{
        return transaction {
            Bmis.selectAll().where {Bmis.userId eq userId}.map {mapToBmi(it)}
        }
    }

    //Save an bmi to the database
    fun save(bmi: Bmi){
        transaction {
            Bmis.insert {
                it[height] = bmi.height
                it[weight] = bmi.weight
                it[bmivalue] = bmi.bmivalue
                it[started] = bmi.started
                it[userId] = bmi.userId
            }
        }
    }

    //delete by user id of an bmi from database
    fun delete(id: Int): Int {
        return transaction {
            Bmis.deleteWhere { Bmis.id eq id}
        }
    }

    //update and bmis in the database with bmi id
    fun updateBmi(id: Int, bmi: Bmi): Int{
        return transaction {
            Bmis.update({ Bmis.id eq id }) {
                it[height] = bmi.height
                it[weight] = bmi.weight
                it[bmivalue] = bmi.bmivalue
                it[started] = bmi.started
                it[userId] = bmi.userId
            }
        }
    }

}