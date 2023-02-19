package domain.post

import domain.user.UserID
import org.joda.time.DateTime

case class Post(id: PostID, user_id : UserID, title: String, content: String, thumbnail: String, created_at: DateTime, updated_at: Option[DateTime])
