//one argument method f1 that takes an an Int and returns an Int
def f1(x: Int): Int = {x * x}
f1(2)

//no-argument method f1 that returns a Function1 (Int) => Int
def f2: (Int) => Int = {x => x*x}
f2(2)

//function which takes an Int and returns a Function1 (Ohne Klammern)
def bf: Int => Int => Int = i => v => i+ v
bf(1).apply(3)

//same mit Klammern
def bf2: (Int) => (Int => Int) = {i => v => i + v}
bf2(1)(5)

//Input a Function1 (= Double => Double) as Param1, Returning a Function (=  Double => Double) using Type Inference
import scala.math._

def deriv(f:Double => Double, dx:Double) = {x:Double => f(x + dx) - f(x)}

val cos = deriv(sin, 0.00000001)

println(sin(Pi / 2))
println(cos(Pi / 2))


//dpp Sample 1: Function as Param
def answer(f: Int => String) = f(42)
val convertToStringFunction = (in: Int) => in.toString

answer(convertToStringFunction)

//dpp Sample2: Function1 as Param and returning Function1
def with42(f: Int => Int) = f(42)
def power2(x: Int) = x * x
val with33: Int => Int = 33 +

with42(with33)
with42(power2)