package domain.user

import com.google.inject.ImplementedBy
import port.secondary.user.UserRepoOnJDBCImpl

import scala.util.Try

@ImplementedBy(classOf[UserRepoOnJDBCImpl])
trait UserRepository {
  def addUser(user: User): Try[UserID]

  def findUserByUsername(username: String): Option[User]

  def findUserByID(id: Long): Option[User]
}
