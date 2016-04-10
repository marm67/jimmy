package jimmy

import jimmy._

case class Script(comandos: Seq[Comando])

sealed trait Comando
case class SET(variable: String, valor: String) extends Comando 
case class CEMT(op: String, rec: RecursoT) extends Comando 
case class CEDA(op: String, rec: RecursoT) extends Comando 

sealed trait RecursoT
case class Recurso(tipo: String, nombre: String, parametros: Map[String, String]) extends RecursoT
case class Servicio(tipo: String, parametros: Map[String, String]) extends RecursoT
