package jimmy

object Consola {
  def run: Unit = {
    Console.println("Welcome to the Simple Console, type exit or quit to leave")
    var done = false
    while(!done){
      Console.print("+> ")
      val input = readLine()
      var lowerInput = input.toLowerCase
      if(lowerInput == "quit" || lowerInput == "exit"){
        done = true
      }else if(lowerInput == "help"){ 
    	  Console.print("+> help???")
      }else if(input != ""){
        var programArgs = input split " "
        Console.print("+> " + programArgs)
      }
    }
  }
}