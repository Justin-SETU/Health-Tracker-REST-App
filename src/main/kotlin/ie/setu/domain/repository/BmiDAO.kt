package ie.setu.domain.repository

import ie.setu.domain.Bmi
import ie.setu.domain.db.Bmis
import ie.setu.utils.mapToBmi
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update


class BmiDAO {

    fun getBmilist(): ArrayList<Bmi> {
        val bmilist : ArrayList<Bmi> = arrayListOf()
        transaction {
            Bmis.selectAll().map{bmilist.add(mapToBmi(it))}
        }
        return bmilist
    }

    fun saveBmi(bmi: Bmi) {
        transaction {
            Bmis.insert {
                it[id] = bmi.id
                it[weight] = bmi.weight
                it[height] = bmi.height
                it[bmiCalc] = bmi.bmiCalc
                it[timestamp] = bmi.timestamp
                it[userId] = bmi.userId
            }
        }
    }

    fun updateBmi(id:Int, bmi: Bmi) {
        transaction {
            Bmis.update({ Bmis.id eq id }) {
                it[Bmis.id] = bmi.id
                it[weight] = bmi.weight
                it[height] = bmi.height
                it[bmiCalc] = bmi.bmiCalc
                it[timestamp] = bmi.timestamp
                it[userId] = bmi.userId
            }
        }
    }

    fun deleteBmi(id: Int) {
        return transaction {
            Bmis.deleteWhere { Bmis.id eq id }
        }
    }

    fun findBmibyId(id: Int): Bmi? {
        return transaction {
            Bmis.selectAll().where { Bmis.id eq id }.map { mapToBmi(it) }.firstOrNull()
        }
    }

}


