package meta.serialiser


@add_field_filepath() case class Foo()

@hello
object Test extends App {
  println(this.hello)
  val f = Foo("abe")
  println(f)
}