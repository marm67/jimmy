package jimmy

import jimmy._
import scala.annotation.tailrec
import scala.collection.mutable

import grizzled.slf4j.Logging

object Interprete extends Logging {

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
      case CEMT(op, recursot)  => procesaCemt(op, recursot)
    }

    private def procesaSet(variable: String, valor: String) = {
      contexto.set(variable, valor)
    }

    private def procesaCemt(op: String, rec: RecursoT) = rec match {
      case Servicio(_,_)  => procesaCemtServicio(op, rec.asInstanceOf[Servicio])
      case Recurso(_,_,_) => procesaCemtRecurso(op, rec)  
    }

    private def procesaCemtServicio(op: String, servicio: Servicio) = {
      val ctx = contexto.vars map { case (k,v) => s"""set $k($v)""" }
      val cmds = Plantilla(servicio.tipo, servicio.parametros)
        .split("[\r\n]+")
        .filter(!_.isEmpty())
        .map( linea => s"""cemt $op $linea""" )
        .toList

      val script = ctx ++ cmds
      println( script )
      println( script.mkString("\n") )

    }

    private def procesaCemtRecurso(op: String, rec: RecursoT) = {
      val target = contexto("target")
      val rs = Target(target).from("CICSLocalTransaction").where("TRANID=X* AND PROGRAM=XALA01CO").inquire
      rs.print("eyu_cicsname", "tranid", "program", "definesource")
    }

    private def procesaCeda(op: String, rec: RecursoT) ={
      println("procesaCeda")
    }

  }

  class Contexto(initVars: Map[String, String] = Map.empty) {
    private val _vars = mutable.Map[String, String]() ++ initVars.map(e => e._1 -> e._2)
    def vars : Map[String, String] = _vars.map(x => x._1 -> x._2).toMap
    def set(v: String, value: String): Unit = _vars.put(v, value)
    def apply(n: String) = _vars.getOrElse(n, "")

    def print = {
    }
  }

}

object ComandoCEMT {
  val rs = Target("sistemas").from("CICSLocalTransaction").where("TRANID=X* AND PROGRAM=XALA01CO").inquire
}
