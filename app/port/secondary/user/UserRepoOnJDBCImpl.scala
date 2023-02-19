package port.secondary.user

import domain.user.{UserDTO, UserID, UserRepository}

import scala.util.{Random, Try}

class UserRepoOnJDBCImpl extends UserRepository {
  override def addUser(user: UserDTO): Try[Unit] = ???
}
