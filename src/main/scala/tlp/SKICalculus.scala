package tlp

object SKICalculus:

  type S = 'S'
  type K = 'K'
  type I = 'I'

  type R[T] = T match
    case (((S, x), y), z) => R[((x, z), (y, z))]
    case ((K, x), y) => R[x]
    case (I, x) => R[x]
    case (x, y) => R[x] match
      case `x` => (x, R[y])
      case _ => R[(R[x], R[y])]
    case T => T

  summon[R[(K, K)] =:= (K, K)]
  summon[R[(((S, I), I), K)] =:= (K, K)]