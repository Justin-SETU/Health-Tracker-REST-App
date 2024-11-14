package ie.setu.domain.repository

import ie.setu.domain.Recommend
import ie.setu.domain.Workout

class RecommendDAO {
    fun getRecommend(workout: String, userId: Int): List<String> {
        return when(workout.lowercase()){
            "cardio" -> listOf("Running", "Cycling", "Jump Rope")
            "high intensity" -> listOf("HIIT", "Circuit Training", "Sprints")
            "low intensity" -> listOf("Walking", "Yoga", "Pilates")
            else -> listOf("General Fitness", "Stretching", "Light Cardio")
        }
    }
}