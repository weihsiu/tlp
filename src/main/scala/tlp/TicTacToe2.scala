package tlp

import scala.Tuple._

object TicTacToe2:

  sealed trait Pos
  case object TL extends Pos
  case object TC extends Pos
  case object TR extends Pos
  case object ML extends Pos
  case object MC extends Pos
  case object MR extends Pos
  case object BL extends Pos
  case object BC extends Pos
  case object BR extends Pos

  sealed trait Player
  case object O extends Player
  case object X extends Player

  type OtherPlayer[P <: Player] <: Player = P match
    case O.type => X.type
    case X.type => O.type

  type ContainsPos[P <: Pos] = [M] =>> ContainsPosAux[P, M]
  type ContainsPosAux[P <: Pos, M] <: Boolean = M match
    case Move[_, P] => true
    case _ => false

  case class Move[+PL <: Player, +PS <: Pos](player: PL, pos: PS)
  case class Board[P <: Player, MS <: Tuple](moves: List[Move[Player, Pos]])
  val emptyBoard = Board[O.type, EmptyTuple](Nil)

  def playMove[P <: Player, MS <: Tuple, PL <: Player, PS <: Pos]
      (board: Board[P, MS], move: Move[PL, PS])
      (using P =:= PL, Filter[MS, ContainsPos[PS]] =:= EmptyTuple)
      : Board[OtherPlayer[P], Move[PL, PS] *: MS] =
    Board[OtherPlayer[P], Move[PL, PS] *: MS](move :: board.moves)

  val b1 = playMove(emptyBoard, Move(O, MC))
  val b2 = playMove(b1, Move(X, TC))
  val b3 = playMove(b2, Move(O, BC))
