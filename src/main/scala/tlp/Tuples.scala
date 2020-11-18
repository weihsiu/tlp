package tlp

import scala.Tuple._
import scala.compiletime.ops.int._

object Tuples:

  type Id[A] = A

  type Triple = (Int, Char, Boolean)

  // type Map[Tup <: Tuple, F[_]] <: Tuple
  // Converts a tuple `(T1, ..., Tn)` to `(F[T1], ..., F[Tn])
  val t1: Map[Triple, Id] = (1, 'a', true)

  summon[Map[Triple, Id] =:= Triple]

  val t2: Map[Triple, Option] = t1.map([t] => (x: t) => Some(x)) // (Some(1), Some('a'), Some(true))

  summon[Map[Triple, Option] =:= (Option[Int], Option[Char], Option[Boolean])]

  type Upgrade[T] = T match
    case Int => Double
    case Char => String
    case Boolean => Boolean

  val upgrade: [t] => t => Upgrade[t] = new PolyFunction:
    def apply[T](x: T): Upgrade[T] = x match
      case x: Int => x.toDouble
      case x: Char => x.toString
      case x: Boolean => !x

  val t3: Map[Triple, Upgrade] = t1.map(upgrade) // (1.0, "a", true)

  summon[Map[Triple, Upgrade] =:= (Double, String, Boolean)]

  type Couple[T] = T match
    case _ => (T, T)

  // type FlatMap[Tup <: Tuple, F[_] <: Tuple] <: Tuple
  // Converts a tuple `(T1, ..., Tn)` to a flattened `(..F[T1], ..., ..F[Tn])`
  val t4: FlatMap[Triple, Couple] = (1, 2, 'a', 'b', true, false)

  summon[FlatMap[Triple, Couple] =:= (Int, Int, Char, Char, Boolean, Boolean)]

  type CharOnly[T] <: Boolean = T match
    case Char => true
    case _ => false

  // type Filter[Tup <: Tuple, P[_] <: Boolean] <: Tuple
  val t5: Filter[t4.type, CharOnly] = ('c', 'd')
  
  summon[Filter[t4.type, CharOnly] =:= (Char, Char)]

  type CountInt[T, Z] = T match
    case Int => Z + 1 // what is "+"?
    case _ => Z

  // type Fold[T <: Tuple, Z, F[_, _]]
  // Fold a tuple `(T1, ..., Tn)` into `F[T1, F[... F[Tn, Z]...]]]`
  val t6: Fold[t4.type, 0, CountInt] = 2

  summon[Fold[t4.type, 0, CountInt] =:= 2]
