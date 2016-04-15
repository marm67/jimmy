package jimmy

//type Filtro = (String, String)
// type Filtros = List[Filtro]

//case class Filtro(tipo: String, fnombre: String, filtros: Filtros)

trait Elemento {
	val cics: Region
	val tipo: String
	val props: Map[String, String]
}

trait RecursoCics extends Elemento {
	
}

trait Definicion extends Elemento {
	
}

trait Region {
	def inquire(filtros: (String, String)* ): List[RecursoCics]
}


class Cics(val applid: String) extends Region {
	def inquire(filtros: (String, String)* ): List[RecursoCics] = {
		val criterio = s"""${filtros(0)._1}=${filtros(0)._2}"""
		println( criterio )
		// filtros.foreach(println) 
	 //    val cmci = Cmci("SIST").scope(applid).tabla("CICSLocalTransaction").criteria("TRANID=XA*")
	 //    val response = cmci.doGet.get
	 //    val body = response.body
	 //    println(response.records)

		List()
	}
}

object Cics {
	def apply(applid: String): Cics = new Cics(applid)
}
