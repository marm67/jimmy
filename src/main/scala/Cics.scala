package jimmy

import grizzled.slf4j.Logging

trait Queryable { 
	var tabla: String = ""
	var criterio: String = ""

	def from(t: String) = {
		tabla = t
		this
	}

	def where(cond: String) = {
		criterio = cond
		this
	}

	def and(cond: String) = {
		val co = criterio
		criterio = s"""(($criterio) AND ($cond))"""
		this
	}

	def or(cond: String) = {
		criterio = s"""(($criterio) OR ($cond))"""
		this
	}

	def inquire: List[RecursoCics]
	// def create: RecursoCics
	// def set: RecursoCics
	// def discard: RecursoCics

	// def expand: List[DefinicionCics]
	// def define: DefinicionCics
	// def alter: DefinicionCics
	// def delete: DefinicionCics

}

object Cics {
	def apply(entorno: String, applid: String): Cics = new Cics(entorno, applid)
	def apply(applid: String): Cics = {
		val cs =Target(applid).toList 
		if (cs.size > 0) cs(0) else { println("ERROR"); null }
	}
}

class Cics private(val entorno: String, val applid: String) extends Logging with Queryable { self =>
	def inquire: List[RecursoCics] = {
		val cmci = Cmci(entorno).scope(applid).tabla(tabla).criteria(criterio)
		cmci.doGet.records map ( x => x match { case Record(t, p) => RecursoCics(t, p, self) } )
	}
	override def toString() = s"""Cics($entorno, $applid)"""
}

object ListaCics {
	def apply(cs: List[Cics]): ListaCics = new ListaCics(cs)
}
class ListaCics private(lista: List[Cics]) extends Queryable { 
	def inquire: List[RecursoCics] = lista flatMap ( _.from(tabla).where(criterio).inquire )
	def toList = lista
}

