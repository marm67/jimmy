package jimmy

import jimmy._
import scala.annotation.tailrec
import scala.collection.mutable

import grizzled.slf4j.Logging

object Interpreter extends Logging {

//  private[this] val logger = org.log4s.getLogger

  def execute(script: Script) = new ScriptExecution(script, new Contexto).execute

  class ScriptExecution(script: Script, contexto: Contexto) {
    def execute = for (e <- script.comandos) { 
      logger.info( e.toString ) 
      execComando(e) 
    }

    private def execComando(e: Comando): Unit = (e: @unchecked) match {
      case SET(variable, valor)  => procesaSet(variable, valor)
      case CEDA(op, recursot)  => procesaCeda(op, recursot)
    }

    private def procesaSet(variable: String, valor: String) = {
      contexto.set(variable, valor)
    }

    private def procesaCeda(op: String, rec: RecursoT) ={
      println("procesaCeda")
    }

    private def procesaCemt(op: String, rec: RecursoT) ={
      println("procesaCemt")
    }

  }

  class Contexto(initVars: Map[String, Any] = Map.empty) {
    private val _vars = mutable.Map[String, Any]() ++ initVars.map(e => e._1 -> e._2)
    def vars : Map[String,Any] = _vars.map(x => x._1 -> x._2).toMap
    def apply(n: String) = _vars.getOrElse(n, "")
    def set(v: String, value: Any): Unit = _vars.put(v, value)
  }

}
