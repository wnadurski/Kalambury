package models

import anorm._
import play.api.db.DB
import anorm.SqlParser._
import play.api.Play.current
import play.api.libs.Codecs.md5
/**
 * Created by Hyster on 2015-10-29.
 */
case class UserData(login:String, password: String)

case class User(id: Int, login: String, password: String, ranking: Int, totalPoints: Int, role: Role) {
  def checkPassword(pass : String) = this.password == pass
}

object User {
  val parser = {
    int("Users.id") ~
    str("Users.login") ~
    str("Users.password") ~
    int("Users.ranking") ~
    int("Users.totalPoints") ~
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

  private def selectWhere(where:String) = {
    select + " AND " + where
  }

  def findAll : Seq[User] = {
    DB.withConnection {
      implicit connection =>
        SQL(select).as(parser *)
    }
  }

  def findByName(username: String) : Option[User] = {
    DB.withConnection { implicit connection =>
        SQL(selectWhere("login = {username}")).on('username -> username).as(parser.singleOpt)
    }
  }

  def findById(id: Int) : Option[User] = {
    DB.withConnection { implicit connection =>
      SQL(selectWhere("users.id = {id}")).on('id -> id).as(parser.singleOpt)
    }
  }

  def create(data:UserData) = {
    DB.withConnection { implicit connection =>
      SQL(selectWhere("login = {name}")).on( "name" ->data.login).as(parser.singleOpt) match {
        case Some(user) => println("User: " + user)
          None
        case None => println("DUPA LYSA")
          val id = SQL("insert into Users(login, password, ranking, totalPoints, roleId) values ({login}, {password}, {ranking}, {totalPoints}, {roleId})").on(
            "login" -> data.login,
            "password" -> md5(data.password.getBytes),
            "ranking" -> 0,
            "totalPoints" -> 0,
            "roleId" -> Role.findByName("simple").id
          ).executeInsert(SqlParser.int("SCOPE_IDENTITY()").single)

          User.findById(id)
      }
    }
  }
}