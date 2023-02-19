package application.post

import domain.post.{Post, PostID, PostRepository}

import javax.inject.Inject
import scala.util.Try

class PostService @Inject()(val postRepository: PostRepository) {
  def addPost(post: Post): Try[PostID] = postRepository.addPost(post)

  def getAllPostsPaginated(page: Int): List[Post] = postRepository.getAllPostsPaginated(page)

  def getPostByID(id: Long): Option[Post] = postRepository.getPostByID(id)
}
