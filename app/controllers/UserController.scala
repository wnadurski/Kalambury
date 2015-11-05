package controllers

import models._
import play.api.data._
import play.api.data.Forms._
import play.api.mvc._
import play.api.Play.current
import play.api.i18n.Messages.Implicits._
/**
 * Created by Hyster on 2015-11-02.
 */
class UserController extends Controller with Authentication {

  def validate(userData : UserData) = {
    val user = User.findByName(userData.login)

    user match {
      case None => false
      case Some(user) => user.checkPassword(userData.password)
    }
  }

  val registerForm = Form (
    mapping(
      "login" -> nonEmptyText,
      "password" -> nonEmptyText
    )(UserData.apply)(UserData.unapply)
  )

  val loggingForm = Form (
    mapping(
      "login" -> nonEmptyText,
      "password" -> nonEmptyText
    )(UserData.apply)(UserData.unapply) verifying("Wrong login parameters", fields => fields match {
      case userData => validate(userData)
    })
  )

  def register = Action {
    Ok(views.html.register(registerForm))
  }

  def registerPost() = Action{ implicit request =>
    registerForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.register(formWithErrors))
      },
      userData => {
        println(s"login: ${userData.login} pass: ${userData.password}")
        User.create(userData) match {
          case None => Redirect(routes.UserController.register)
          case Some(user) =>Redirect("/").withSession("username" -> userData.login)
        }
      }
    )
  }

  def loggingPost() = WithAuthentication {
    user =>
      Redirect("/").withSession("username" -> user.login)
  }

  def logoutPost() = WithAuthentication {
    user =>
      println(s"USer ${user.login} tried to logout")
      Redirect("/").withNewSession
  }

}
