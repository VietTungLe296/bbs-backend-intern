package port.primary.post

import application.post.PostService
import application.user.UserService
import domain.post.Post
import domain.user.{User, UserID}
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import play.api.libs.json.Json
import play.api.mvc.{Action, AnyContent, BaseController, ControllerComponents}

import java.nio.file.{Files, Paths}
import java.util.UUID
import javax.inject.{Inject, Singleton}
import scala.util.{Failure, Success}

@Singleton
class PostController @Inject()(val postService: PostService,
                               val userService: UserService,
                               val controllerComponents: ControllerComponents) extends BaseController {
  private final val tempUser = User(UserID(1), "VTS", "vt@gmail.com", "123456")
  private final val formatter = DateTimeFormat.forPattern("yyyy-MM-dd")

  def addPost(): Action[AnyContent] = Action { implicit request =>
    val json = request.body.asJson.get
    val post = Post(
      id = null,
      user_id = tempUser.id,
      title = json("title").as[String],
      content = json("content").as[String],
      thumbnail = json("thumbnail").as[String],
      created_at = DateTime.now(),
      updated_at = DateTime.now()
    )

    postService.addPost(post) match {
      case Success(value) => Ok(value.toString)
      case Failure(exception) =>
        exception.printStackTrace()
        BadRequest("Cannot create new post")
    }
  }

  def getAllPostsPaginated(page: Int): Action[AnyContent] = Action {
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
          "updated_at" -> post.updated_at.toString(formatter)
        ))
        Ok(Json.toJson(postJson))
    }
  }

  def getPostByID(id: Long): Action[AnyContent] = Action {
    postService.getPostByID(id) match {
      case Some(post) =>
        val postJson = Json.obj(
          "title" -> post.title,
          "content" -> post.content,
          "author_name" -> tempUser.user_name,
          "created_at" -> post.created_at.toString(formatter),
          "updated_at" -> post.updated_at.toString(formatter)
        )
        Ok(Json.toJson(postJson))

      case None => NotFound(s"Post with ID $id not existed!")
    }
  }

  def upload() = Action(parse.multipartFormData) { implicit request =>
    request.body.file("image").map { uploadFile =>
      val contentType = uploadFile.contentType.getOrElse("")
      if (contentType != "image/png" && contentType != "image/jpeg") {
        BadRequest("Only PNG/JPEG files are allowed")
      } else {
        val fileName = UUID.randomUUID().toString + "-" + uploadFile.filename
        val path = Paths.get("public/images/thumbnail/", fileName)

        Files.write(path, Files.readAllBytes(uploadFile.ref))

        Ok("File uploaded")
      }
    }.getOrElse {
      BadRequest("Cannot upload file")
    }
  }

  def exportToCSV(id: Long): Action[AnyContent] = Action {
    val post = postService.getPostByID(id).get
    val author = userService.findUserByID(post.user_id.value).get
    val csvData = s"${author.user_name},${post.title},${post.created_at},${post.updated_at}\n"
    val csvContent = s"author_name,title,created_at,updated_at\n$csvData"
    val csvFilename = "posts.csv"

    Ok(csvContent)
      .withHeaders(
        "Content-Disposition" -> s"""attachment; filename="$csvFilename"""",
        "Content-Type" -> "text/csv"
      )
  }
}


