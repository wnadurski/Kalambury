package models

import anorm._
import play.api.db.DB
import anorm.SqlParser._
import play.api.Play.current
/**
 * Created by Hyster on 2015-10-29.
 */
case class User(id: Int, login: String, password: String, ranking: Int, totalPoints: Int, role: Role)

object User {
  val parser = {
    int("users.id") ~
    str("users.login") ~
    str("users.password") ~
    int("users.ranking") ~
    int("users.totalPoints") ~
    Role.parser map {
      case id ~ login ~ password~ranking~tp~role => User(id,login, password, ranking, tp, role)
    }
  }

  private val select =
    """
      |SELECT * From
      |   Users, Roles
      |   WHERE Users.roleId = Roles.id
    """.stripMargin

  def findAll : Seq[User] = {
    DB.withConnection {
      implicit connection =>
        SQL(select).as(parser *)
    }
  }
}