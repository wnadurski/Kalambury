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
        case None => Redirect("/").flashing("message" -> "Nieprawid&#322;owe dane logowania.")
      }
    }

      def WithAuthenticationReq(f: Request[AnyContent] => Result) = Action { implicit request =>
        val user = AuthUtils.parseUserFromRequest
        println(s"USerasda: $user")

        user match {
          case Some(u) => f(request)
          case None => Redirect("/").flashing("message" -> "Nieprawid&#322;owe dane logowania.")
        }
      }
}
