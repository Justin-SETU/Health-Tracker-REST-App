package ie.setu.domain.repository

import ie.setu.domain.Meal
import ie.setu.domain.db.Meals
import ie.setu.utils.mapToMeal
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

//workout data access objects that handles database operations
class MealDAO {

    //Get all the activities in the database regardless of user id
    fun getAll(): ArrayList<Meal> {
        val mealsList: ArrayList<Meal> = arrayListOf()
        transaction {
            Meals.selectAll().map { mealsList.add(mapToMeal(it)) }
        }
        return mealsList
    }

    //Find a specific workout by workout ids
    fun findByMealId(id: Int): Meal?{
        return transaction {
            Meals.selectAll().where { Meals.id eq id}.map{ mapToMeal(it) }.firstOrNull()
        }
    }

    //Find all meals for a specific user id
    fun findByUserId(userId: Int): List<Meal>{
        return transaction {
            Meals.selectAll().where {Meals.userId eq userId}.map { mapToMeal(it) }
        }
    }

    //Save an workout to the database
    fun save(meals: Meal){
        transaction {
            Meals.insert {
                it[food] = meals.food
                it[calories] = meals.calories
                it[started] = meals.started
                it[userId] = meals.userId
            }
        }
    }

    //delete by user id of an meal from database
    fun delete(id: Int): Int {
        return transaction {
            Meals.deleteWhere { Meals.id eq id}
        }
    }

    //update and meal in the database with meal id
    fun updateMeal(id: Int, meal: Meal): Int{
        return transaction {
            Meals.update({ Meals.id eq id }) {
                it[food] = meal.food
                it[calories] = meal.calories
                it[started] = meal.started
                it[userId] = meal.userId
            }
        }
    }

}