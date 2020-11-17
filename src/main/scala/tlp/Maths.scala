package tlp

import scala.compiletime.ops.int._

object Maths:

  type GCD[A <: Int, B <: Int] <: Int =
    B match
      case 0 => A
      case _ => GCD[B, A % B]

  val gcd: GCD[252, 105] = 21

  type Fib[N <: Int] <: Int =
    N match
      case 0 => 0
      case 1 => 1
      case _ => Fib[N - 1] + Fib[N - 2]

  val fib0: Fib[0] = 0
  val fib1: Fib[1] = 1
  val fib2: Fib[2] = 1
  val fib3: Fib[3] = 2
  val fib4: Fib[4] = 3

  type Odd[X <: Int] =
    X match
      case 0 => false
      case _ => Even[X - 1]
  
  type Even[X <: Int] =
    X match
      case 0 => true
      case _ => Odd[X - 1]

  val odd: Odd[9] = true
  val even: Even[10] = true
  // val dfd: Odd[-1] = true // compiler recursion limit exceeded
