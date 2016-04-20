package jimmy

//type Filtro = (String, String)
// type Filtros = List[Filtro]

//case class Filtro(tipo: String, fnombre: String, filtros: Filtros)

import scala.language.dynamics

trait ElementoCics extends Dynamic

case class RecursoCics(tipo: String, props: Map[String,String], cics: Cics) extends ElementoCics { self =>

	val propsUpdated: scala.collection.mutable.LinkedHashMap[String, String]  = scala.collection.mutable.LinkedHashMap()

	def set(ps: (String, String)*): RecursoCics = {
		self
	}

	def selectDynamic(name: String) = props(name)

	def updateDynamic(name: String)(value: String) {
		propsUpdated(name) = value
	}

}
case class DefinicionCics(tipo: String, grupo: String, props: Map[String,String]) extends ElementoCics

trait Region {
	def inquire(filtros: (String, String)* ): List[RecursoCics]
}

