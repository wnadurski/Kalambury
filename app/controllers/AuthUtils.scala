package controllers

import play.api.libs.Codecs._
import play.api.mvc._
import models._

/**
 * Created by Hyster on 2015-11-02.
 */
object AuthUtils {
  def parseUserFromCookie(implicit request: RequestHeader) = {
    println(s"USername: ${request.session.get("username")} pass: ")
    request.session.get("username").flatMap(username => User.findByName(username))

  }

  def parseUserFromForm(implicit request: Request[AnyContent]) = {
    request.body.asFormUrlEncoded match {
      case Some(body) =>
        val mappedBody = body.map { case (key, value) => key-> value.mkString }
        val username = mappedBody.get("username")
        val password = mappedBody.get("password")
        println(s"USasername: $username pass: $password")

        (username, password) match {
          case (Some(u), Some(p)) => User.findByName(u).filter( user => user.checkPassword(md5(p.getBytes)))
          case _ => None
        }

      case None =>
        None
    }
  }

  def parseUserFromQueryString(implicit request : RequestHeader):Option[User] = {
    val query = request.queryString.map { case (key, value) => key -> value.mkString }

    val username = query.get("username")
    val password = query.get("password")

    println(s"USername: $username pass: $password")

    (username, password) match {
      case (Some(u), Some(p)) => User.findByName(u).filter( user => user.checkPassword(md5(p.getBytes)))
      case _ => None
    }

  }

  def parseUserFromRequest(implicit request:Request[AnyContent]):Option[User] = {
    parseUserFromQueryString orElse parseUserFromCookie orElse parseUserFromForm
  }
}
