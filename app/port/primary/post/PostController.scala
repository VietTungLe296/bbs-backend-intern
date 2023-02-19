package port.primary.post

import application.post.PostService
import domain.post.Post
import domain.user.{User, UserID}
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import play.api.libs.json.Json
import play.api.mvc.{Action, AnyContent, BaseController, ControllerComponents}

import javax.inject.{Inject, Singleton}
import scala.util.{Failure, Success}

@Singleton
class PostController @Inject()(val postService: PostService,
                               val controllerComponents: ControllerComponents) extends BaseController {
  private final val user = User(UserID(1), "VTS", "vt@gmail.com", "123456")
  private final val formatter = DateTimeFormat.forPattern("yyyy-MM-dd")

  def addPost(): Action[AnyContent] = Action { implicit request =>
    val json = request.body.asJson.get
    val post = Post(
      id = null,
      user_id = user.id,
      title = json("title").as[String],
      content = json("content").as[String],
      thumbnail = json("thumbnail").as[String],
      created_at = DateTime.now(),
      updated_at = None
    )

    postService.addPost(post) match {
      case Success(value) => Ok(value.toString)
      case Failure(exception) =>
        exception.printStackTrace()
        BadRequest(exception.toString)
    }
  }

  def getAllPostsPaginated(page: Int): Action[AnyContent] = Action { implicit request =>
    val posts = postService.getAllPostsPaginated(page)

    posts.size match {
      case 0 => Ok("Empty")
      case _ =>
        val postJson = posts.map(post => Json.obj(
          "id" -> post.id.value,
          "user_id" -> post.user_id.value,
          "title" -> post.title,
          "content" -> post.content,
          "thumbnail" -> post.thumbnail,
          "created_at" -> post.created_at.toString(formatter),
          "updated_at" -> post.updated_at.fold("")(value => value.toString(formatter))
        ))
        Ok(Json.toJson(postJson))
    }
  }

  def getPostByID(id: Long): Action[AnyContent] = Action { implicit request =>
    postService.getPostByID(id) match {
      case Some(post) =>
        val postJson = Json.obj(
          "title" -> post.title,
          "content" -> post.content,
          "thumbnail" -> post.thumbnail,
          "created_at" -> post.created_at.toString(formatter),
          "updated_at" -> post.updated_at.fold("")(value => value.toString(formatter))
        )
        Ok(Json.toJson(postJson))

      case None => NotFound(s"Post with ID $id not existed!")
    }
  }
}


