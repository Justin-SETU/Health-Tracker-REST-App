package ie.setu.helpers

import ie.setu.domain.*
import ie.setu.domain.db.*
import ie.setu.domain.repository.*
import ie.setu.repository.*
import org.jetbrains.exposed.sql.SchemaUtils
import org.joda.time.DateTime

val nonExistingEmail = "112233445566778testUser@xxxxx.xx"
val validName = "Test User 1"
val validEmail = "testuser1@test.com"
val validPassword = "password123"

val users = arrayListOf<User>(
    User(name = "Alice Wonderland", email = "alice@wonderland.com", id = 1, password = "pass123"),
    User(name = "Bob Cat", email = "bob@cat.ie", id = 2, password = "pass1234"),
    User(name = "Mary Contrary", email = "mary@contrary.com", id = 3, password = "pass12345"),
)

val activities = arrayListOf<Activity>(
    Activity(description = "Walking", duration = 30.0, calories = 101, started = DateTime.now(), userId = 1, id = 1),
    Activity(description = "Running", duration = 40.0, calories = 120, started = DateTime.now(), userId = 1, id = 2),
    Activity(description = "Jogging", duration = 50.0, calories = 101, started = DateTime.now(), userId = 1, id = 3),
)

val workouts = arrayListOf<Workout>(
    Workout(workout = "Walking", duration = 30.0, started = DateTime.now(), userId = 1, id = 1),
    Workout(workout = "Running", duration = 40.0, started = DateTime.now(), userId = 1, id = 2),
    Workout(workout = "Jogging", duration = 50.0,  started = DateTime.now(), userId = 1, id = 3),
)

val bmis = arrayListOf<Bmi>(
    Bmi(height = 40.0, weight = 30.0, bmivalue= 0.01875, started = DateTime.now(), userId = 1, id = 1),
    Bmi(height = 50.0, weight = 40.0, bmivalue= 0.016, started = DateTime.now(), userId = 2, id = 2),
    Bmi(height = 20.0, weight = 50.0, bmivalue= 0.125, started = DateTime.now(), userId = 1, id = 3),
)

val logs = arrayListOf<Log>(
    Log(status_type = "Daily", summary = "5 hours", started = DateTime.now(), userId = 1, id = 1),
    Log(status_type = "weekly", summary = "5 hours", started = DateTime.now(), userId = 1, id = 2),
    Log(status_type = "weekly", summary = "6 hours",  started = DateTime.now(), userId = 1, id = 3),
)




internal fun populateUserTable(): UserDAO {
    SchemaUtils.create(Users)
    val userDAO = UserDAO()
    userDAO.save(user1)
    userDAO.save(user2)
    userDAO.save(user3)
    return userDAO
}

internal fun populateActivityTable(): ActivityDAO {
    SchemaUtils.create(Activities)
    val activityDAO = ActivityDAO()
    activityDAO.save(activity1)
    activityDAO.save(activity2)
    activityDAO.save(activity3)
    return activityDAO
}

internal fun populateBmiTable(): BmiDAO {
    SchemaUtils.create(Bmis)
    val bmiDAO = BmiDAO()
    bmiDAO.save(bmi1)
    bmiDAO.save(bmi2)
    bmiDAO.save(bmi3)
    return bmiDAO
}

internal fun populateWorkoutTable(): WorkoutDAO {
    SchemaUtils.create(Workouts)
    val workoutDAO = WorkoutDAO()
    workoutDAO.save(workout1)
    workoutDAO.save(workout2)
    workoutDAO.save(workout3)
    return workoutDAO
}

internal fun populateLogTable(): LogDAO {
    SchemaUtils.create(Logs)
    val logDAO = LogDAO()
    logDAO.save(log1)
    logDAO.save(log2)
    logDAO.save(log3)
    return logDAO
}


