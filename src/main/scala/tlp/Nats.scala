package tlp

object Nats:

  sealed trait Nat
  sealed trait Zero extends Nat
  sealed trait Succ[N <: Nat] extends Nat

  type Plus[N1 <: Nat, N2 <: Nat] <: Nat = N1 match
    case Zero => N2
    case Succ[n] => Succ[Plus[n, N2]]

  type Multiply[N1 <: Nat, N2 <: Nat] <: Nat = N1 match
    case Zero => Zero
    case Succ[n] => Plus[N2, Multiply[n, N2]]

  type One = Succ[Zero]
  type Two = Succ[One]
  type Three = Succ[Two]
  type Four = Succ[Three]
  
  summon[Succ[One] =:= Two]
  summon[Plus[One, One] =:= Two]
  summon[Plus[One, Two] =:= Three]
  summon[Succ[Plus[One, One]] =:= Three]
  summon[Succ[Plus[One, Two]] =:= Plus[Succ[One], Two]]
  summon[Multiply[Zero, One] =:= Zero]
  summon[Multiply[One, One] =:= One]
  summon[Multiply[Two, Two] =:= Four]