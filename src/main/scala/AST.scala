package main.scala

case class Servicio(tipo: String)

trait Comando {
	def check(): Unit
}

case class Define(servicio: Servicio, parametros: Map[String, String]) extends Comando {
	def toComando = {
		"%s %s %s(%s) %s" format (
			 "DEFINE" 
			, servicio.tipo 
			, parametros map { case (k,v) => "%s(%s)" format (k,v) } mkString ("", " ", " ")
		)
	}

	def check = {
		// check plantilla
		val plantilla = servicio.tipo		
	    val path = s"""c:/scala/proyectos/jimmy/resources/plantillas/$plantilla"""
	    println(path)
	}
}

case class Set(variable: String, valor: String) extends Comando {
	def check = {}

}

