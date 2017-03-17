"asdfg: incorrect IP format".contains("incorrect IP formatx")


//Sorting String in different ways
"bca".sortBy(_.hashCode())
"bca".sortWith((x, y) => x < y)

List(1,2,3) :+ 4
List(1,2,3).reduce((first, second) => first + second)
List(1,2,3).foldLeft(0)((first, second) => first + second)
List(1,2,3).sum
List('a',"b","c").collect{case i:String => i.hashCode(); case i:Char => i.hashCode()}
"tesaFilm".filter(char => char >= 'e')     //weird Behaviour...
"tesaFilmm".distinct
//a String is a list
//permutations answers with an iterator
"0123456789".permutations.size
"0123456789".permutations.drop(999999).next
"012".permutations.mkString(";")
"012".drop(1).iterator.next()

//anoter change for rebase
