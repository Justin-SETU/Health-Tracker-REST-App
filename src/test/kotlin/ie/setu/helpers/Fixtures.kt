package ie.setu.helpers

import ie.setu.domain.Activity
import ie.setu.domain.Bmi
import ie.setu.domain.User
import ie.setu.domain.Workout
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
    Bmi(height = 40.0, weight = 30.0, bmivalue= 27.1, started = DateTime.now(), userId = 1, id = 1),
    Bmi(height = 50.0, weight = 40.0, bmivalue= 28.2, started = DateTime.now(), userId = 2, id = 2),
    Bmi(height = 60.0, weight = 50.0, bmivalue= 29.3, started = DateTime.now(), userId = 2, id = 3),
)