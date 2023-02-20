package port.secondary.post

import domain.post.{Post, PostID}
import domain.user.UserID
import scalikejdbc.{AutoSession, WrappedResultSet}
import skinny.orm.{Alias, SkinnyCRUDMapperWithId}

import scala.util.Random

object PostDAO extends SkinnyCRUDMapperWithId[PostID, Post]  {
  implicit val session: AutoSession = AutoSession

  override val tableName = "post"

  override def defaultAlias: Alias[Post] = createAlias("p")

  override def idToRawValue(id: PostID): Any = id.value

  override def rawValueToId(value: Any): PostID = PostID(value.toString.toLong)

  override def extract(rs: WrappedResultSet, n: scalikejdbc.ResultName[Post]): Post =
    Post(
      id = PostID(rs.get(n.id)),
      user_id = UserID(rs.get(n.user_id)),
      title = rs.get(n.title),
      content = rs.get(n.content),
      thumbnail = rs.get(n.thumbnail),
      created_at = rs.get(n.created_at),
      updated_at = rs.get(n.updated_at)
    )

  override def useExternalIdGenerator = true

  override def generateId: PostID = PostID(Random.nextLong(10000000))
}



