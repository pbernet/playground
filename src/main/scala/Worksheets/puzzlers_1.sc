import scala.collection.immutable.{TreeMap, SortedMap}

val list: List[String]  = List( "foo", "bar", "spam" , "a" )
val unsorted = list.groupBy(_.length)
val sortedMap = SortedMap(unsorted.toSeq:_*)
val sortedTreeMap = TreeMap(unsorted.toSeq:_*)


def sumSizes(collections: Iterable[TraversableOnce[_]]): Int = collections.map(_.size).sum

sumSizes(List(Set(1, 2), List(3, 4)))
sumSizes(Set(List(1, 2), Set(3, 4)))


val listOfInt = List(1)
listOfInt.toSet





trait SpyOnEquals {
  override def equals(x: Any) = { println("DEBUG: In equals"); super.equals(x) }
}
case class CC()
case class CCSpy() extends SpyOnEquals
val cc1 = new CC() with SpyOnEquals
val cc2 = new CC() with SpyOnEquals
val ccspy1 = CCSpy()
val ccspy2 = CCSpy()
println(cc1 == cc2)
println(cc1.## == cc2.##) // null-safe hashCode()
println(ccspy1 == ccspy2)
println(ccspy1.## == ccspy2.##)
