package tlp

import scala.annotation._

object Firsts:

  type First[X] = X match
    case None.type => None.type
    case Some[x] => First[x]
    case String => Option[Char]
    case Array[x] => First[Option[x]]
    case Iterable[x] => First[Option[x]]
    case x *: _ => First[Option[x]]
    case Any => Option[X]

  @tailrec
  def first[X](x: X): First[X] = x match
    case x: None.type => None
    case x: Some[_] => first(x.get)
    case x: String => x.headOption
    case x: Array[_] => first(x.headOption)
    case x: Iterable[_] => first(x.headOption)
    case x: (_ *: _) => first(Option(x.head))
    case x: Any => Option(x)

  @main
  def runFirsts(): Unit = 
    println(first((1, false, 'a'))) // Some(1)
    println(first("hello")) // Some('h')
    println(first("")) // None
    println(first(Array(1, 2, 3))) // Some(1)
    println(first(Array.empty[Int])) // None
    println(first(1 :: 2 :: 3 :: Nil)) // Some(1)
    println(first(Nil)) // None
    println(first(false)) // Some(false)

    println(first(List(Array(("world", false))))) // Some('w')