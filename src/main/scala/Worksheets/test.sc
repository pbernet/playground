val isOddFuncLit = (x:Int) => x % 2 == 1
def isOddFunc(x:Int) = x % 2 == 1

object Calculator {
  def isOddMethod(x:Int) = x % 2 == 1
}


val aIntList = List(1,3,2)

aIntList.filter(isOddFuncLit)
aIntList.filter(isOddFunc)
aIntList.filter(Calculator.isOddMethod)


//change for rebase