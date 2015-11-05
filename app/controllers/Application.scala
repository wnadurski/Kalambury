package controllers
import play.api._
import play.api.mvc._
import models._
import play.api.mvc.Session

class Application extends Controller {

  def index = Action { implicit request =>

    Ok(views.html.index())
  }
}
