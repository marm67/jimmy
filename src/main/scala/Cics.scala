package jimmy

import grizzled.slf4j.Logging

object Cics {
	def apply(entorno: String, applid: String): Cics = new Cics(entorno, applid)
	def apply(applid: String): Cics = {
		Target(applid) match {
			case Some(s) => s(0)
			case None => {
				println("ERROR")
				null 
			}
		}
	}
}

// class Cics private(val entorno: String, val applid: String) extends Logging  {

// 	def inquire(filtros: (String, String)* ): List[RecursoCics] = {
// 		val criterio = filtros.reverse.foldLeft(List[String]())( (acum, x) => s"""${x._1}=${x._2}""" :: acum ).mkString(" AND ")
// 		val cmci = Cmci(entorno).scope(applid).tabla("CICSLocalTransaction").criteria(criterio)
// 		cmci.doGet.records map ( x => x match {case Record(t,p) => RecursoCics(t,p)} )
// 	}

// 	override def toString() = s"""Cics($entorno, $applid)"""
// }


class Cics private(val entorno: String, val applid: String) extends Logging { self =>

	var tabla: String = ""
	var criterio: String = ""

	def from(str: String): Cics = {
		tabla = str
		self
	}

	def where(str: String): Cics = {
		criterio = str
		self
	}

	def inquire: List[RecursoCics] = {
		val cmci = Cmci(entorno).scope(applid).tabla(tabla).criteria(criterio)
		cmci.doGet.records map ( x => x match {case Record(t,p) => RecursoCics(t,p,self)} )
	}

	// def where(fs: (String, String)* ): Cics = {
	// 	fs map ( x => filtros(x._1) = x._2 )
	// 	self
	// }

	// def inquire: List[RecursoCics] = {
	// 	val criterio = filtros.foldLeft(List[String]())( (acum, x) => s"""${x._1}=${x._2}""" :: acum ).mkString(" AND ")
	// 	val cmci = Cmci(entorno).scope(applid).tabla(tabla).criteria(criterio)
	// 	cmci.doGet.records map ( x => x match {case Record(t,p) => RecursoCics(t,p,self)} )
	// }

	override def toString() = s"""Cics($entorno, $applid)"""
}