package application.user

import domain.user.{User, UserID, UserRepository}

import javax.inject.Inject
import scala.util.Try

class UserService @Inject()(val userRepository: UserRepository) {
  def register(user: User): Try[UserID] = userRepository.addUser(user)

  def findUserByID(id: Long): Option[User] = userRepository.findUserByID(id)
}
