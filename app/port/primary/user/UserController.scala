package port.primary.user

import application.user.UserService
import domain.user.{User, UserValidator}
import play.api.mvc.{Action, AnyContent, BaseController, ControllerComponents}
import utility.HashPasswordImpl

import javax.inject.{Inject, Singleton}
import scala.util.{Failure, Success}

@Singleton
class UserController @Inject()(val userValidator: UserValidator, val hashing: HashPasswordImpl, val userService: UserService, val controllerComponents: ControllerComponents) extends BaseController {
  def register(): Action[AnyContent] = Action { implicit request =>
    val json = request.body.asJson.get
    val username = (json \ "username").as[String]
    val email = (json \ "email").as[String]
    val password = (json \ "password").as[String]

    if (!userValidator.validateUsername(username)) UnprocessableEntity("Invalid username")
    else if (!userValidator.validateEmail(email)) UnprocessableEntity("Invalid email")
    else if (!userValidator.validatePassword(password)) UnprocessableEntity("Invalid password")
    else if (!userValidator.isUsernameUnique(username)) UnprocessableEntity("Username has been used")
    else {
      val hashPassword = hashing.hashPassword(password)
      userService.register(User(null, username, email, hashPassword)) match {
        case Success(value) => Ok(value.toString)
        case Failure(exception) => exception.printStackTrace()
          BadRequest("Cannot create user!")
      }
    }
  }
}


