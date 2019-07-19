package besmart.elderlycare.model.register

data class RegisterRequest(
    var typeID: Int,
    var staffID: String,
    var cardID: String,
    var password: String,
    var verifyPassword: String,
    var firstName: String,
    var lastName: String,
    var birthday: String,
    var genderID: Int,
    var address: String,
    var phone: String,
    var username: String
)