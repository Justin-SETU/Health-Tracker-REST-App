package ie.setu.domain

//Represent the user in the system, stores user data
data class User (var id: Int,
                 var name:String,
                 var email:String,
                 var password:String)

data class LoginModel(
    val email: String,
    val password: String
)
