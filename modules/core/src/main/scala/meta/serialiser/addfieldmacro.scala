package meta.serialiser

import scala.reflect.macros.blackbox
import scala.language.experimental.macros
import scala.annotation.StaticAnnotation

//(className, fields, parents, body)
object addFieldMacro{
   def impl(c: blackbox.Context)(annottees: c.Expr[Any]*): c.Expr[Any] = {
    import c.universe._
    import Flag._
     annottees map (_.tree) toList match {
         case (classDecl: ClassDef) :: Nil => 
            classDecl match {
               case q"case class $className(..$fields) extends ..$parents { ..$body }" =>
                  c.Expr[Any](q"""case class $className (blah:String)""") 
            }
         case _ => c.abort(c.enclosingPosition, "Invalid annottee")
      }
   }
  
}


class add_field_filepath extends StaticAnnotation {
   def macroTransform(annottees: Any*): Any = macro addFieldMacro.impl 
}