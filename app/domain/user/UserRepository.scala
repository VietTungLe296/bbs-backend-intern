package domain.user

import com.google.inject.ImplementedBy
import port.secondary.user.UserRepoOnJDBCImpl

import scala.util.Try

@ImplementedBy(classOf[UserRepoOnJDBCImpl])
trait UserRepository {
  def addUser(user: UserDTO): Try[Unit]
}
