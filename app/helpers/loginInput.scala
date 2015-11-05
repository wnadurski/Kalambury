package helpers

/**
 * Created by nojas_000 on 2015-11-05.
 */
object loginInput {
  import views.html.helper.FieldConstructor
  implicit val myFields = FieldConstructor(myFieldConstructorTemplate.f)
}
