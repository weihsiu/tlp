import scala.compiletime.error
import scala.compiletime.ops.any._
import scala.compiletime.ops.int._
import scala.Tuple._
import scala.util.Not

type Foo[X <: Int] = X match
  case 0 => "Zero"
  case 1 => "One"
  case _ => X

val r0: Foo[0] = "Zero"
val r1: Foo[1] = "One"
val r3: Foo[123] = 123

type Id[A] = A

type T1 = (Int, Char, Boolean)
val t1: Map[T1, Id] = (1, 'a', true)
val t2: Map[T1, Option] = (Some(1), None, Some(false))

type F1[T] = T match
  case Int => Double
  case Char => String
  case _ => T

val t3: Map[T1, F1] = (1.0, "a", false)

type F2[T] = T match
  case _ => (T, T)

val t4: FlatMap[T1, F2] = (1, 2, 'a', 'b', true, false)

type F3[T, Z] = T match
  case Int => Z + 1
  case _ => Z

val t5: Fold[t4.type, 0, F3] = 2

import scala.quoted._
import scala.quoted.staging._
given Toolbox = Toolbox.make(getClass.getClassLoader)
def showType[T : Type](using QuoteContext): String = summon[Type[T]].show
withQuoteContext:
  showType[Foo[2]]

val i: 1 + 2 = 3
val b: 1 > 2 = false
{
  import scala.compiletime.ops.string._
  val s: "a" + "b" = "ab"
}

