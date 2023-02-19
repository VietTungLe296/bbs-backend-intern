package port.secondary.post

import domain.post.{Post, PostID, PostRepository}
import scalikejdbc.DB
import scalikejdbc.jodatime.JodaParameterBinderFactory.jodaDateTimeParameterBinderFactory

import scala.util.{Success, Try}

class PostRepoOnJDBCImpl extends PostRepository {
  override def addPost(post: Post): Try[PostID] = {
    DB localTx { implicit session =>
      val p = PostDAO.column
      val postID: Long = PostDAO.createWithNamedValues(
        p.user_id -> post.user_id.value,
        p.title -> post.title,
        p.content -> post.content,
        p.thumbnail -> post.thumbnail,
        p.created_at -> post.created_at,
      ).value

      Success(PostID(postID))
    }
  }

  override def getAllPostsPaginated(page: Int): List[Post] = {
    val pageSize = 10
    val offset = (page - 1) * 10
    val orderings = Seq(PostDAO.defaultAlias.created_at.desc)
    PostDAO.findAllWithLimitOffset(limit = pageSize, offset, orderings)
  }

  override def getPostByID(id: Long): Option[Post] =
    PostDAO.findById(PostID(id))
}
