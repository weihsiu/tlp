package tlp

import scala.Tuple._
import scala.compiletime._
import scala.compiletime.ops.any._
import scala.compiletime.ops.boolean._
import scala.compiletime.ops.int._
import scala.language.experimental.namedTypeArguments
import scala.util._

object TicTacToe:

  type TL = 256
  type TC = 128
  type TR = 64
  type ML = 32
  type MC = 16
  type MR = 8
  type BL = 4
  type BC = 2
  type BR = 1

  type O = "O"
  type X = "X"
  type OtherPlayer[P] = P match
    case O => X
    case X => O

  case class Board[NP, OS <: Int, XS <: Int]()
  val emptyBoard = Board[O, 0, 0]()

  type SamePlayer[P1, P2] = P1 =:= P2

  type LegalMove[M <: Int, OS <: Int, XS <: Int] = Not[BitwiseAnd[M, BitwiseOr[OS, XS]] =:= M]

  type AddMove[P, M <: Int, OS <: Int, XS <: Int] <: Int = P match
    case O => BitwiseOr[M, OS]
    case X => BitwiseOr[M, XS]

  def playMove[P, M <: Int, NP, OS <: Int, XS <: Int]
      (board: Board[NP, OS, XS])
      (using SamePlayer[P, NP], LegalMove[M, OS, XS]) = 
    Board[
      OtherPlayer[P],
      AddMove[P, M, OS, XS],
      AddMove[OtherPlayer[P], M, OS, XS]]()

  val b1 = playMove[P = O, M = MC](emptyBoard)
  val b2 = playMove[P = X, M = TL](b1)
  val b3 = playMove[P = O, M = TR](b2)
  val b4 = playMove[P = X, M = ML](b3)
  val b5 = playMove[P = O, M = BL](b4)

  type Wins = 448 *: 56 *: 7 *: 292 *: 146 *: 73 *: 273 *: 84 *: EmptyTuple

  type ContainsAux[MS <: Int, WS] <: Boolean = BitwiseAnd[MS, WS] match
    case WS => true
    case _ => false
  type Contains[MS <: Int] = [WS] =>> ContainsAux[MS, WS]

  type Win[MS <: Int] = Not[Filter[Wins, Contains[MS]] =:= EmptyTuple]

  case class Winner[P]()

  inline def winner[NP, OS <: Int, XS <: Int](board: Board[NP, OS, XS]): Option[Winner[OtherPlayer[NP]]] =
    summonFrom:
      case given Win[OS] => Some(Winner[OtherPlayer[NP]]())
      case given Win[XS] => Some(Winner[OtherPlayer[NP]]())
      case _ => None

  val w1 = winner(b1)
  val w5 = winner(b5)