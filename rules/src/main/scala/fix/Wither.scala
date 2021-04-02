package fix

import scalafix.v1._
import scala.meta._

class Wither extends SemanticRule("Wither") {

  override def fix(implicit doc: SemanticDocument): Patch = {
    doc.tree.collect {
      case cls: Defn.Class =>

      // if has ANY annotation that's called Wither...
      val hasWitherAnnotation = cls.mods.collect { case annot: Mod.Annot =>
        annot.init.tpe.asInstanceOf[Type.Name].value == "Wither"
      }.exists(_ == true)

      // a = a, b = b ....
      val namedArgs = cls.ctor.paramss.map { paramsList =>
        paramsList.map { p =>
          Term.Assign(Term.Name(p.name.value), Term.Name(p.name.value))
        }
      }

      val template = cls.templ
      val hasBody = template.stats.nonEmpty
      val patches =
        if (!hasWitherAnnotation) {
          List.empty
        } else if (hasBody) {
          val lastBodyToken = template.tokens.last
          getParams(cls).flatMap { param =>
            val witherNameStr = s"with${upperFirst(param.name.value)}"
            val newMethods = getWitherMethods(cls, param, witherNameStr, namedArgs)
            val patches = findMethod(template.stats, witherNameStr) match {
              case Some(m) =>
                List(Patch.empty) // skip if already present
              case None =>
                newMethods.map { nm =>
                  Patch.addLeft(lastBodyToken, nm.toString + "\n")
                }
            }
            patches
          }
        } else {
          val newMethods = getParams(cls).flatMap { param =>
            val witherNameStr = s"with${upperFirst(param.name.value)}"
            getWitherMethods(cls, param, witherNameStr, namedArgs)
          }
          val newTemplate = template"""{
            ..$newMethods 
          }"""

          val newCls = cls.copy(templ = newTemplate)
          List(Patch.addRight(cls, newTemplate.toString)).filterNot(_ => newMethods.isEmpty)
        }

      patches
    }.flatten.asPatch
  }

  private def findMethod(stats: List[Stat], witherNameStr: String): Option[Defn.Def] =
    stats.collect { case meth: Defn.Def => meth }
    .filter(_.name.value == witherNameStr).headOption

  private def getParams(cls: Defn.Class)(implicit doc: SemanticDocument): List[Term.Param] =
    cls.ctor.paramss.flatten.filter { param =>
      val paramInfo = param.symbol.info.get
      !paramInfo.isImplicit &&      
      paramInfo.isPublic
    }
  
  private def getWitherMethods(cls: Defn.Class, param: Term.Param, name: String, namedArgs: List[List[Term.Assign]]) = {
    val witherName = Term.Name(name)
    param.decltpe.get match {
      case t"Option[..$tpesnel]" =>
        val namedArg = Term.Assign(
          Term.Name(param.name.value), 
          Term.Apply(Term.Name("Option"), List(Term.Name(param.name.value)))
        )
        val optArgs = namedArgs.map { paramList =>
          paramList.find(_.lhs.asInstanceOf[Term.Name].value == param.name.value) match {
            case None => paramList
            case Some(p) =>
              paramList.filterNot(_ == p) ++ List(namedArg)
          }
        }
        List(
          q"""
            def $witherName(${param.name}: ${param.decltpe.get}): ${cls.name} =
              new ${cls.name}(...$namedArgs)
          """,
          q"""
            def $witherName(${param.name}: ${tpesnel.head}): ${cls.name} =
              new ${cls.name}(...$optArgs)
          """
        )
      case t"List[..$tpesnel]" =>
        val toListName = q"toList"
        val tn = Term.Name(param.name.value)
        val namedArg = q"$tn = $tn.toList"
        val listArgs = namedArgs.map { paramList =>
          paramList.find(_.lhs.asInstanceOf[Term.Name].value == param.name.value) match {
            case None => paramList
            case Some(p) =>
              paramList.filterNot(_ == p) ++ List(namedArg)
          }
        }
        List(
          q"""
            def $witherName(${param.name}: ${param.decltpe.get}): ${cls.name} =
              new ${cls.name}(...$namedArgs)
          """,
          q"""
            def $witherName(${param.name}: ${tpesnel.head}*): ${cls.name} =
              new ${cls.name}(...$listArgs)
          """
        )
      case _ =>
        List(
          q"""
            def $witherName(${param.name}: ${param.decltpe.get}): ${cls.name} =
              new ${cls.name}(...$namedArgs)
          """)
    }
  }

  private def upperFirst(s: String): String =
    s.head.toUpper + s.tail

}
