val aFuncAnonym = (x:Int) => x * 2
aFuncAnonym(4)

//no-argument method f that returns a Function1 (Int) => Int
def f2: (Int) => Int = {x => x * 2}
f2(2)

//one-argument method that returns a Function1 (Int) => Int
def aFunc(x:Int): (Int) => Int = {(x:Int) => x * 2}
val func = aFunc(1)
func(4)




def composeManual(g:Int=>Int, h:Int=>Int) = (x:Int) => g(h(x))
val composedFunc = composeManual(aFuncAnonym , (x:Int) => x - 1)
println(composedFunc(10))

def f(s: String) = "f(" + s + ")"
def g(s: String) = "g(" + s + ")"
def h(s: String) = "h(" + s + ")"
val fComposeG = f _ compose g _ compose h _
fComposeG("yay")


val fAndThenG = f _ andThen g _
fAndThenG("yay")


def mapmake[T](g:T=>T)(seq: List[T]): Seq[Any] =  seq.map(g)
mapmake((x:Int) => x * 2)(List(4))





