package parquettransformer

import scala.annotation.{StaticAnnotation, compileTimeOnly}
import scala.language.experimental.macros
import scala.reflect.api.Trees
import scala.reflect.macros.whitebox

@compileTimeOnly("enable macro paradise to expand macro annotations")
class toCaseClass extends StaticAnnotation {
  def macroTransform(annottees: Any*): Any = macro ToCaseClass.generate
}

// pour chacun des éléments : (name, type, columnName, lambdaFonction)


//case class KeywordPerformance(day: String, ad_group: String)

//object KeywordPerformance {
//
//  val columnsList: Array[String] = ReportFetcher.COLUMNS_TO_RETRIEVE.split(", ")
//
//  def apply(line: Array[String]): KeywordPerformance = {
//    KeywordPerformance(day = line(columnsList.indexOf("Date")),
//      ad_group = line(columnsList.indexOf("AdGroupName")))
//  }
//}
object ToCaseClass {
  def generate(c: whitebox.Context)(annottees: c.Expr[Any]*): c.Expr[Any] = {
    import c.universe._

    annottees.map(_.tree) match {
      case List(l: List[Tuple4[String, String, String, Function1[Any, Any]]]) =>
        val caseClassDecl = l.map { case (name, tipe, columnName, lambdaFunction) => q"$name: $tipe" }
        c.Expr[Any](
          q"case class KeywordPerfGen(..$caseClassDecl)"
        )
      case Seq(q"val $lName = $l") =>
        println(l)
        val children: List[Trees#Tree] = l.children
        val caseClassDecl = children.filter {
          case nice: Apply =>
            true
          case _ =>
            false
        }
          .map(elt => {
            val q"($name, classOf[$clazz], $columnName, $myFunc)" = elt
            println(show(clazz))
            println(showRaw(clazz))
            val Literal(Constant(realName: String)) = name
            ValDef(Modifiers(Flag.DEFERRED), TermName(realName),
              clazz.asInstanceOf[Tree], EmptyTree)
          }
          )
        c.Expr[Any](q"case class KeywordPerfGen(..$caseClassDecl)")
    }
  }
}

object Use {
  def use(): Unit = {
    val myFunc: Function[String, String] = coucou => coucou
    val unit: Class[String] = classOf[String]
    //    @toCaseClass val list = Seq(("bla", classOf[String], "Bla", myFunc))


  }
}