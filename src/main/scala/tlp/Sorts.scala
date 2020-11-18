package tlp

import scala.Tuple._
import scala.compiletime.ops.int._

object Sorts:

  type IntLT[X, Y] <: Boolean = X < Y match
    case true => true
    case _ => false

  type SizeLT[X, Y] <: Boolean = (X, Y) match
    case (Tuple, Tuple) => Size[X] < Size[Y]
    case _ => false

  type Insert[A, T <: Tuple, LT[_, _] <: Boolean] <: Tuple = T match
    case EmptyTuple => A *: EmptyTuple
    case h *: t => LT[A, h] match
      case true => A *: T
      case false => h *: Insert[A, t, LT]

  summon[Insert[1, (2, 3), IntLT] =:= (1, 2, 3)]
  summon[Insert[2, (1, 3), IntLT] =:= (1, 2, 3)]
  summon[Insert[3, (1, 2), IntLT] =:= (1, 2, 3)]
  summon[Insert[1 *: EmptyTuple, ((2, 2), (3, 3, 3)), SizeLT] =:= (1 *: EmptyTuple, (2, 2), (3, 3, 3))]
  summon[Insert[(2, 2), (1 *: EmptyTuple, (3, 3, 3)), SizeLT] =:= (1 *: EmptyTuple, (2, 2), (3, 3, 3))]
  summon[Insert[(3, 3, 3), (1 *: EmptyTuple, (2, 2)), SizeLT] =:= (1 *: EmptyTuple, (2, 2), (3, 3, 3))]

  type Sort[T <: Tuple, LT[_, _] <: Boolean] <: Tuple = T match
    case EmptyTuple => EmptyTuple
    case h *: t => Insert[h, Sort[t, LT], LT]

  summon[Sort[(2, 1, 3), IntLT] =:= (1, 2, 3)]
  summon[Sort[(3, 2, 1), IntLT] =:= (1, 2, 3)]
  summon[Sort[((2, 2), 1 *: EmptyTuple, (3, 3, 3)), SizeLT] =:= (1 *: EmptyTuple, (2, 2), (3, 3, 3))]
  summon[Sort[((3, 3, 3), (2, 2), 1 *: EmptyTuple), SizeLT] =:= (1 *: EmptyTuple, (2, 2), (3, 3, 3))]