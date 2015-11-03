package controllers
import play.api._
import play.api.mvc._
import models._

/**
 * Created by nojas_000 on 2015-11-03.
 */
class GameController extends Controller {
  def enter = Action {
    Ok(views.html.game())
  }
}
