package ie.setu.domain.repository

import ie.setu.domain.Recommend
import ie.setu.domain.Workout
import ie.setu.domain.db.Recommends
import ie.setu.utils.mapToRecommend
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class RecommendDAO {
    fun getRecommend(workout: String, userId: Int): List<String> {
        return when(workout.lowercase()){
            "cardio" -> listOf("Running", "Cycling", "Jump Rope")
            "high intensity" -> listOf("HIIT", "Circuit Training", "Sprints")
            "low intensity" -> listOf("Walking", "Yoga", "Pilates")
            else -> listOf("General Fitness", "Stretching", "Light Cardio")
        }
    }

//    fun getAll(): ArrayList<Recommends> {
//        val recommendsList: ArrayList<Recommends> = arrayListOf()
//        transaction {
//            Recommends.selectAll().map { recommendsList.add(mapToRecommend(it)) }
//        }
//        return recommendsList
//    }
}