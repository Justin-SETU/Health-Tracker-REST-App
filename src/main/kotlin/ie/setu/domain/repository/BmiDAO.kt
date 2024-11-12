package ie.setu.domain.repository

import ie.setu.domain.Bmi
import ie.setu.domain.db.BmiValues
import ie.setu.utils.mapToBmi
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class BmiDAO {
    fun findBmi(bmi: Bmi): Int {
        val bmicalculate = bmiCalculate(bmi.weight, bmi.height)

        return transaction {
            BmiValues.insert {
                it[weight] = bmi.weight
                it[height] = bmi.height
                it[BmiCalc] = bmicalculate
                it[userId] = bmi.userId
                it[timestamp] = bmi.timestamp
            }
        } get BmiValues.id

    }

    private fun bmiCalculate(weight: Double, height: Double): String {

        if (weight <= 0 || height <= 0) {
            throw IllegalArgumentException("Weight and height must be greater than zero")
        }

        val heightInMeters = height / 100.0
        // Calculate BMI using the formula: weight (kg) / (height (m) * height (m))
        return (weight / (heightInMeters * heightInMeters)).toString()
    }

    // Get all BMI entries
    fun getbmi(): ArrayList<Bmi> {
        val bmiList: ArrayList<Bmi> = arrayListOf()
        transaction {
            BmiValues.selectAll().map {
                bmiList.add(mapToBmi(it))
            }
        }
        return bmiList
    }

}


