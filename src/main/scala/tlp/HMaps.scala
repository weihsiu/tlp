package tlp

import scala.Tuple.{Filter, Size}

object HMaps:
  trait HMap[K, ES <: Tuple]:
    type Entry[K, V] = (K, V)
    type Not[B <: Boolean] <: Boolean = B match
      case true => false
      case _ => true
    type Is[T1, T2] <: Boolean = T2 match
      case T1 => true
      case _ => false
    type IsC[T1] = [T2] =>> Is[T1, T2]
    type IsNotC[T1] = [T2] =>> Not[Is[T1, T2]]
    type IsKey[K, E] <: Boolean = E match
      case Entry[K, _] => true
      case _ => false
    type IsKeyC[K] = [E] =>> IsKey[K, E]
    type IsNotKeyC[K] = [E] =>> Not[IsKey[K, E]]

    def apply[V](key: K)(using 1 =:= Size[Filter[ES, IsC[Entry[key.type, V]]]]): V
    def put[V](key: K, value: V): HMap[K, Entry[key.type, V] *: Filter[ES, IsNotKeyC[key.type]]]
    def remove(key: K)(using 1 =:= Size[Filter[ES, IsKeyC[key.type]]]): HMap[K, Filter[ES, IsNotKeyC[key.type]]]

  object HMap:
    def apply[K]: HMap[K, EmptyTuple] = MapHMap[K, EmptyTuple](Map.empty[K, Any])
    private class MapHMap[K, ES <: Tuple](map: Map[K, Any]) extends HMap[K, ES]:
      def apply[V](key: K)(using 1 =:= Size[Filter[ES, IsC[Entry[key.type, V]]]]): V =
        map(key).asInstanceOf[V]
      def put[V](key: K, value: V): HMap[K, Entry[key.type, V] *: Filter[ES, IsNotKeyC[key.type]]] =
        MapHMap[K, Entry[key.type, V] *: Filter[ES, IsNotKeyC[key.type]]](map + (key -> value))
      def remove(key: K)(using 1 =:= Size[Filter[ES, IsKeyC[key.type]]]): HMap[K, Filter[ES, IsNotKeyC[key.type]]] =
        MapHMap[K, Filter[ES, IsNotKeyC[key.type]]](map - key)

  import HMap._
  val hm = HMap[String].put[String]("name", "walter").put[Int]("age", 18)
  val hm1 = hm.put[Int]("age", 20).put[String]("age", "twenty")
  println(hm1[String]("name"))
  // val age: Int = hm1("age")
  println(hm1[String]("age"))
  val hm2 = hm.remove("age")
  hm2[String]("name")
  // hm2[Int]("age")
    
