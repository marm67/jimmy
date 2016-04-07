package main.scala

case class Script(items: Seq[Comando])

sealed trait Comando
case class Set(variable: String, valor: String) extends Comando 
case class Ceda(op: CedaOp, rec: RecursoT) extends Comando 
case class Cemt(op: CemtOp, rec: RecursoT)) extends Comando 

sealed trait RecursoT
case class Recurso(tipo: String, nombre: String, parametros: Map[String, String]) extends RecursoT
case class Servicio(servicio: String, parametros: Map[String, String]) extends RecursoT

sealed trait Operacion

sealed trait CedaOp extends Operacion
case class Expand extends CedaOp
case class Define extends CedaOp
case class Alter extends CedaOp
case class Delete extends CedaOp

sealed trait CemtOp extends Operacion
case class Inquire extends CemtOp
case class Create extends CemtOp
case class Set extends CemtOp
case class Discard extends CemtOp
