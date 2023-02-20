package domain.user

import javax.inject.Inject

final class UserValidator @Inject()(val userRepository: UserRepository) {
  def validateUsername(username: String) = username.matches("^[A-Za-z][A-Za-z0-9_]{6,50}$")

  def validateEmail(email: String) = email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")

  def validatePassword(password: String) = password.matches("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,20}$")

  def isUsernameUnique(username: String): Boolean = {
    userRepository.findUserByUsername(username) match {
      case Some(_) => false
      case None => true
    }
  }
}
