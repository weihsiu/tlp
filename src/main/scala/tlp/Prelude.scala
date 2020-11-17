package tlp

import scala.compiletime._
import scala.quoted._
import scala.quoted.staging._

given Toolbox = Toolbox.make(getClass.getClassLoader)

def showType[T : Type](using QuoteContext): String = summon[Type[T]].show

type If[C <: Boolean, T, F] = C match
  case true => T
  case false => F

extension (sc: StringContext)
  def b32(args: Any*): Int =
    val s = sc
      .parts
      .zipAll(args.map(_.toString), "", "")
      .map((_1, _2) => _1 ++ _2)
      .mkString
    Integer.parseInt("""\D""".r.replaceAllIn(s, ""), 2)
