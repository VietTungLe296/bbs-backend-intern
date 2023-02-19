package domain.post

import com.google.inject.ImplementedBy
import port.secondary.post.PostRepoOnJDBCImpl

import scala.util.Try

@ImplementedBy(classOf[PostRepoOnJDBCImpl])
trait PostRepository {
  def getAllPostsPaginated(page: Int): List[Post]

  def getPostByID(id: Long): Option[Post]

  def addPost(post: Post): Try[PostID]
}
