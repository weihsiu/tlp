package tlp

import scala.reflect._

// java 14 pattern matching for instanceof (jep 305)
object IfInstanceOf:

  opaque type WrappedInstance[A] = A

  def it[A](using ri: WrappedInstance[A]): A = ri

  extension [A](x: Any)(using ClassTag[A])
    def ifInstanceOf(condition: WrappedInstance[A] ?=> Boolean):
        [r] => (WrappedInstance[A] ?=> r) => (WrappedInstance[Any] ?=> r) => r =
      [r] => (ifTrue: WrappedInstance[A] ?=> r) => (ifFalse: WrappedInstance[Any] ?=> r) =>
        x match
          case y: A =>
            if condition(using y) then ifTrue(using y) else ifFalse(using x)
          case _ => ifFalse(using x)

  @main
  def runIfInstanceOf() =
    val x: Any = "hello"
    x.ifInstanceOf[String](it.length > 4)(println(s"string with length greater than 4: $it"))(println(s"not a string or length is less or equal to 4: $it"))
    val y: Any = 11
    println(y.ifInstanceOf[Int](it <= 10)("*" * it)(":("))
    