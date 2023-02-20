package port.secondary.user

import domain.user.{User, UserID}
import scalikejdbc.{AutoSession, WrappedResultSet}
import skinny.orm.{Alias, SkinnyCRUDMapperWithId}

import scala.util.Random

object UserDAO extends SkinnyCRUDMapperWithId[UserID, User] {
  implicit val session: AutoSession = AutoSession

  override val tableName = "user"

  override def defaultAlias: Alias[User] = createAlias("u")

  override def idToRawValue(id: UserID): Any = id.value

  override def rawValueToId(value: Any): UserID = UserID(value.toString.toLong)

  override def extract(rs: WrappedResultSet, n: scalikejdbc.ResultName[User]): User =
    User(
      id = UserID(rs.get(n.id)),
      user_name = rs.get(n.user_name),
      email = rs.get(n.email),
      password = rs.get(n.password),
    )

  override def useExternalIdGenerator = true

  override def generateId: UserID = UserID(Random.nextLong(10000000))
}
