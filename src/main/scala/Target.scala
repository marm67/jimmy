package jimmy

import collection.JavaConversions._
import com.typesafe.config._
import scala.xml._

// Tipos de target:
// 	1 entorno o cicses
// 		cicsjcoa
// 	2 entorno.ambito
// 		prod.comercial
// 	3 entorno.ambito.funcion
// 		prod.comercial.tores
// 	4 entorno.ambito.funcion.cics
// 		prod.comercial.tores.tsectt11	

object Target {
	val target = new Target

	def apply(path: String) = {
		target.get(path)
	}
}

class Target private { self =>
	lazy val mapa = setMapa

	def get(path: String): ListaCics = {
		val patron = path.replaceAll("""\*\*""", """(\*)""").replaceAll("""\.""", """\\.""").replaceAll("""\*""", """(\.\*)""")
		val regexp = patron.r
		val lista = mapa filter { x => 
			regexp.findFirstIn(x) match {
				case Some(_)	=> true
				case None		=> false
			}
		}

		if( lista.size == 0) ListaCics(List())
		else {
			val cs = lista.sorted map { s => 
				val Array(entorno, ambito, funcion, applid) = s.split('.')
				Cics(entorno, applid)
			}
			ListaCics(cs)
		}
	}
	
	def setMapa = {
		val config = Config.getConfig
		config.getConfig("plataforma")
			.entrySet()
			.filter(entry => entry.getKey.count(_ == '.') == 2 )
			.flatMap (entry =>
				config.getStringList("plataforma." + entry.getKey).map( 
					cics => entry.getKey + "." + cics
				) 
			)
			.toList
	}

	// val mapa = setMapaXML
	// 
	// def setMapaXML = {
	// 	val config = Config.getConfig
	// 	val entornos = config.getConfig("plataforma").entrySet().filter(entry => entry.getKey.count(_ == '.') == 2 ).map { entry =>
	// 		(entry.getKey, config.getStringList("plataforma." + entry.getKey ).toList)
	// 	}.toMap

	// 	type Funcion = scala.collection.mutable.Map[String, List[String]]
	// 	type Ambito  = scala.collection.mutable.Map[String, Funcion]
	// 	type Entorno = scala.collection.mutable.Map[String, Ambito]

	// 	val mapa: Entorno = scala.collection.mutable.Map.empty
	// 	entornos foreach { case (clave, cicses) =>
	// 		val Array(entorno, ambito, funcion) = clave.split('.')

	// 		if ( !mapa.contains(entorno) )				
	// 			mapa(entorno) = scala.collection.mutable.Map.empty
	// 		if ( !mapa(entorno).contains(ambito))
	// 			mapa(entorno)(ambito) = scala.collection.mutable.Map.empty

	// 		mapa(entorno)(ambito)(funcion) = cicses
	// 	}

	// 	def createXML               = <entornos>{entornosXML(mapa)}</entornos>
	// 	def entornosXML(mapa: Entorno)       = mapa.map 	 { case (entorno, ambitos)	=> <xml>{ambitosXML(ambitos)}</xml>.copy(label = entorno) }
	// 	def ambitosXML(ambitos: Ambito)      = ambitos.map 	 { case (ambito, funciones)	=> <xml>{funcionesXML(funciones)}</xml>.copy(label = ambito) }
	// 	def funcionesXML(funciones: Funcion) = funciones.map { case (funcion, cicses)	=> <xml>{cicsesXML(cicses)}</xml>.copy(label = funcion) }
	// 	def cicsesXML(cicses: List[String])  = cicses.map 	 { cics => <cics>{cics}</cics> }
		
	// 	createXML
	// }

	// def getFromXML(path: String): Option[List[String]] = {

	// 	path.split('.').size match {
	// 		case 1 => {
	// 			val patron = s""""produccion" \\ "elos""""
	// 			val nodos = mapa \ {patron}
	// 			println(nodos)
	// 			val lista = (nodos \\ "cics").map(_.text).toList
	// 			if( lista.size == 0) None else Some(lista)

	// 		}
	// 		case 2 => {
	// 			None

	// 		}
	// 		case 3 => {
	// 			val Array(entorno, ambito, funcion) = path.split('.')
	// 			val nodos = mapa \ entorno \ ambito \ funcion 
	// 			val lista = (nodos \\ "cics").map(_.text).toList
	// 			Some(lista)
	// 		}
	// 		case 4 => {
	// 			val Array(entorno, ambito, funcion, cics) = path.split('.')

	// 			val patron = s""""prod" \\ "elos""""
	// 			val nodos = mapa \ "prod" \\ "elos"
	// 			val lista = (nodos \\ "cics").map(_.text).filter(_ == cics).toList
	// 			if( lista.size == 0) None else Some(lista)
	// 		}
	// 		case _ => {
	// 			println(s"""Target {$path} erroneo""")
	// 			None
	// 		}
	// 	}

	// 	// println(nodos)
	// 	// val lista = (nodos \\ "cics").map(_.text)
	// 	// println(lista)
	// 	// (mapa \\ "_").filter(_.attribute("entorno").filter(_=="name").isDefined)
	// }

}

