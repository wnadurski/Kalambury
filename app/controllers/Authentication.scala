package controllers

import models.User
import play.api.mvc._

/**
 * Created by Hyster on 2015-11-02.
 */
trait Authentication {
  self: Controller =>

    def WithAuthentication(f: User => Result) = Action { implicit request =>
      val user = AuthUtils.parseUserFromRequest
      println(s"USerasda: $user")

      user match {
        case Some(u) => f(u)
        case None => Forbidden("Brak dostepu")
      }
    }
}
