package jimmy

import jimmy._
import scala.annotation.tailrec
import scala.collection.mutable

object Interpreter {

  class ScriptExecution(script: Script, globalContext: GlobalContext) {
    def procesa = for (e <- script.comandos) procesaElemento(e)

    private def procesaElemento(e: Comando): Unit = (e: @unchecked) match {
      case SET(variable, valor)  => procesaSet(variable, valor)
//      case CEDA(servicio, parametros)  => procesaDefine(servicio, parametros)
    }

    private def procesaSet(variable: String, valor: String) ={
      globalContext.set(variable, valor)
    }

    private def procesaDefine(servicio: String, parametros: Map[String, String]) ={
      println("procesaDefine")
    }

    def execute(script: Script, globalContext: GlobalContext) = new ScriptExecution(script, globalContext).procesa
  }

  class GlobalContext(initVars: Map[String, Any] = Map.empty) {
    private val _vars = mutable.Map[String, Any]() ++ initVars.map(e => e._1 -> e._2)
    def vars : Map[String,Any] = _vars.map(x => x._1 -> x._2).toMap
    def apply(n: String) = _vars.getOrElse(n, "")
    def set(v: String, value: Any):Unit = _vars.put(v, value)
  }

}
