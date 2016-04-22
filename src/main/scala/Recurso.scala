package jimmy

//type Filtro = (String, String)
// type Filtros = List[Filtro]

//case class Filtro(tipo: String, fnombre: String, filtros: Filtros)

import scala.language.dynamics

trait ElementoCics extends Dynamic {
	val propsUpdated: scala.collection.mutable.LinkedHashMap[String, String] = scala.collection.mutable.LinkedHashMap()
	val propiedades: Map[String,String]

	def selectDynamic(name: String) = propiedades(name)
	def updateDynamic(name: String)(value: String) = propsUpdated(name) = value
}
case class RecursoCics(tipo: String, props: Map[String,String], cics: Cics) extends { val propiedades = props } with ElementoCics
case class DefinicionCics(tipo: String, grupo: String, props: Map[String,String]) extends { val propiedades = props } with ElementoCics


object ListaElementoCics {
	def apply(rs: List[RecursoCics]): ListaElementoCics = new ListaElementoCics(rs)
}
class ListaElementoCics(rs: List[RecursoCics]) {
	def toList = rs

	def print: Unit = print( (rs(0).propiedades.keys.toList.sorted):_* )
	def print( cab: String* ): Unit = {
		val buffer = scala.collection.mutable.ListBuffer.empty[List[String]]
		val cabeceras = cab.toList
		buffer += cabeceras
		rs foreach { r=> buffer += cabeceras map (r.propiedades(_))}
		println( Tabulador.format(buffer.toList) )
		// Tabulador.format(List(List("eyu_cicsname", "tranid", "program"), List("XALA01CO", "CICSJLI1", "XALA"), List("XALA01CO", "CICSJLI2", "XALA")))
	}

}

