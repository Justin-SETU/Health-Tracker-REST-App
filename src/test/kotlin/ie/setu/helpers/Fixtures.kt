package ie.setu.helpers

import ie.setu.domain.Activity
import ie.setu.domain.User
import org.joda.time.DateTime

val nonExistingEmail = "112233445566778testUser@xxxxx.xx"
val validName = "Test User 1"
val validEmail = "testuser1@test.com"

val users = arrayListOf<User>(
    User(name = "Alice Wonderland", email = "alice@wonderland.com", id = 1, password = "pass123"),
    User(name = "Bob Cat", email = "bob@cat.ie", id = 2, password = "pass1234"),
    User(name = "Mary Contrary", email = "mary@contrary.com", id = 3, password = "pass12345"),
    User(name = "Carol Singer", email = "carol@singer.com", id = 4, password = "pass1"),
)

val activities = arrayListOf<Activity>(
    Activity(description = "Walking", duration = 30.0, calories = 101, started = DateTime.now(), userId = 1, id = 1),
    Activity(description = "Running", duration = 30.0, calories = 120, started = DateTime.now(), userId = 1, id = 2),
    Activity(description = "Jogging", duration = 30.0, calories = 101, started = DateTime.now(), userId = 1, id = 3),
)
