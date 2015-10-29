package models

import anorm._
import play.api.db.DB
import anorm.SqlParser._
/**
 * Created by Hyster on 2015-10-29.
 */
case class Role (id: Int, name: String)

object Role {
  val parser = {
    int("roles.id") ~
    str("roles.name") map {
      case id ~ name => Role(id, name)
    }
  }
}