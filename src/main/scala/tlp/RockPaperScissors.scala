package tlp

object RockPaperScissors:

  case object Rock
  case object Paper
  case object Scissors

  case object Tie
  case object OneWins
  case object TwoWins

  type Play[PS <: (_, _)] = PS match
    case (Rock.type, Rock.type) => Tie.type
    case (Rock.type, Scissors.type) => OneWins.type
    case (Rock.type, Paper.type) => TwoWins.type
    case (Paper.type, Paper.type) => Tie.type
    case (Paper.type, Rock.type) => OneWins.type
    case (Paper.type, Scissors.type) => TwoWins.type
    case (Scissors.type, Scissors.type) => Tie.type
    case (Scissors.type, Paper.type) => OneWins.type
    case (Scissors.type, Rock.type) => TwoWins.type

  def play[PS <: (_, _)](ps: PS): Play[PS] = ps match
    case _: (Rock.type, Rock.type) => Tie
    case _: (Rock.type, Scissors.type) => OneWins
    case _: (Rock.type, Paper.type) => TwoWins
    case _: (Paper.type, Paper.type) => Tie
    case _: (Paper.type, Rock.type) => OneWins
    case _: (Paper.type, Scissors.type) => TwoWins
    case _: (Scissors.type, Scissors.type) => Tie
    case _: (Scissors.type, Paper.type) => OneWins
    case _: (Scissors.type, Rock.type) => TwoWins
  
  @main
  def runRockPaperScissors(): Unit =
    assert(play(Rock, Rock) == Tie)
    assert(play(Rock, Paper) == TwoWins)
    assert(play(Scissors, Paper) == OneWins)
