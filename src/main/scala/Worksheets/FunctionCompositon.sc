def mapmake[T](g:T=>T)(seq: List[T]): Seq[Any] =  seq.map(g)

val myIntList = List(1,2)

val addFunction:Int => Int = {x => x + 1}
val multiplyFunction:Int => Int = {x => x * 2}

val composedFunction:Int => Int =  {addFunction andThen multiplyFunction}

val directResultList = myIntList.map(composedFunction)
val resultListThroughMapmake = mapmake(composedFunction)(myIntList)



//compose functions with "f andThen g", which is the same as def newFunc[T](x: T) = g(f(x))

def plus1(in: Int) = in + 1
def twice(in: Int) = in * 2
def addDouble() = plus1 _ andThen twice  //plus1 needs to be partially applied so it becomes a function

List(1,2).map(addDouble())