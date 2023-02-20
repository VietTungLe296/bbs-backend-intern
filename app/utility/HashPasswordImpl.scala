package utility

import com.github.t3hnar.bcrypt._

class HashPasswordImpl {
  def hashPassword(password: String): String = {
    password.bcryptBounded(generateSalt)
  }

  def checkPassword(password: String, passwordHash: String): Boolean = {
    password.isBcryptedBounded(passwordHash)
  }
}
