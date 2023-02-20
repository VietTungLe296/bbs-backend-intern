package port.secondary.user

import domain.user.{User, UserID, UserRepository}
import scalikejdbc.{DB, scalikejdbcSQLInterpolationImplicitDef}

import scala.util.{Success, Try}

class UserRepoOnJDBCImpl extends UserRepository {
  override def addUser(user: User): Try[UserID] = {
    DB localTx { implicit session =>
      val u = UserDAO.column
      val userID: Long = UserDAO.createWithNamedValues(
        u.user_name -> user.user_name,
        u.email -> user.email,
        u.password -> user.password
      ).value

      Success(UserID(userID))
    }
  }

  override def findUserByUsername(username: String): Option[User] = UserDAO.findBy(sqls"user_name = $username")

  override def findUserByID(id: Long): Option[User] = UserDAO.findById(UserID(id))
}
