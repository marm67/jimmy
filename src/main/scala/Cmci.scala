package jimmy

//------------------------------------------------------------------------------------
// Ejemplos de url
//------------------------------------------------------------------------------------
// http://10.145.254.228:8632/CICSSystemManagement/CICSLocalTransaction/JPLX1/CICSJCOA//10?CRITERIA=(TRANID=XA*)
// http://10.145.254.228:8632/CICSSystemManagement/CICSLocalTransaction/JPLX1/CICSJCOA?CRITERIA=(TRANID=X*)&SUMMONLY
//
// http://10.145.254.228:8632/CICSSystemManagement/CICSLocalFile/JPLX1/JPLX1/?CRITERIA=(FILE=EQ* AND ENABLESTATUS=UNENABLED)
//
// http://10.145.254.228:8632/CICSSystemManagement/CICSDefinitionTransaction/JPLX1/CICSJCOA?CRITERIA=(NAME=XALA)&PARAMETER=CSDGROUP(XXTR00L)
// http://10.145.254.228:8632/CICSSystemManagement/CICSDefinitionTransaction/JPLX1/CICSJCOA?CRITERIA=(NAME=X*)&PARAMETER=CSDGROUP(XXTR00L)
// http://10.145.254.228:8632/CICSSystemManagement/CICSDefinitionTransaction/JPLX1/CICSJCOA?PARAMETER=CSDGROUP(DFHEDF)
//
// http://10.145.254.228:8632/CICSSystemManagement/CICSDefinitionFile/JPLX1/CICSJCOA?CRITERIA=(NAME=NAGRBATK)&PARAMETER=CSDGROUP(NAGRUPOA)

//------------------------------------------------------------------------------------
// Forma de invocacion
//------------------------------------------------------------------------------------
// 	val cmci = Cmci("SIST")
// 				.scope("CICSJCOA")
// 				.tabla("CICSLocalTransaction")
// 				.limit(10)
// 				.criteria("TRANID=XA*")
// 				
//  val recursos = cmci.doGet()

object Cmci {
	def apply(entorno: String): Cmci = {
		val usuario: String  = Config("usuario").get
		val password: String = Config("password").get
		new Cmci(entorno, usuario, password)
	}
}

class Cmci(entorno: String, usuario: String, password: String) {

	val vars: scala.collection.mutable.LinkedHashMap[String, Option[String]]  = scala.collection.mutable.LinkedHashMap(
		  "protocolo" 	-> Some("http")
		, "host" 		-> Config(s"cmci.$entorno.host")
		, "port" 		-> Config(s"cmci.$entorno.port")
		, "csm" 		-> Some("CICSSystemManagement")
		, "tabla" 		-> None
		, "context" 	-> Config(s"cmci.$entorno.context")
		, "scope" 		-> Config(s"cmci.$entorno.scope")
		, "_limit" 		-> None
		, "_groupBy" 	-> None
		, "_criteria" 	-> None
		, "_parameter" 	-> None
	)

	def context(c: String): Cmci   = { vars("context")	 = Some(c); this }
	def scope(c: String): Cmci     = { vars("scope")	 = Some(c); this }
	def tabla(c: String): Cmci     = { vars("tabla")	 = Some(c); this }
	def limit(c: String): Cmci     = { vars("_limit")	 = Some(c); this }
	def limit(c: Int): Cmci    	   = { vars("_limit")	 = Some(c.toString); this }
	def groupBy(c: String): Cmci   = { vars("_groupBy")	 = Some(c); this }
	def criteria(c: String): Cmci  = { vars("_criteria") = Some(c); this }
	def parameter(c: String): Cmci = { vars("_parameter") = Some(c); this }

	def context(): Option[String]   = vars("context")
	def scope(): Option[String]     = vars("scope")
	def tabla(): Option[String]     = vars("tabla")
	def limit(): Option[String]     = vars("_limit")
	def groupBy(): Option[String]   = vars("_groupBy")
	def criteria(): Option[String]  = vars("_criteria")
	def parameter(): Option[String] = vars("_parameter")

	def uri(): Option[String] = {
		val (opcionales, requeridos) = vars partition ( _._1.startsWith("_") )	

		//comprobar que todos los valores requeridos estan resueltos
		val valores = requeridos.values
		val total = valores.size
		val resueltos = valores.flatten.size
		if ( total > resueltos) {
			//logger.error("...")
			None
		}
		else {
			val List(_protocolo, _host, _port, _csm, _tabla, _context, _scope) = valores.flatten
			var _uri = s"""${_protocolo}://${_host}:${_port}/${_csm}/${_tabla}/${_context}/${_scope}"""
			if ( limit().isDefined ) 		_uri += s"""//${limit().get}"""	
			if ( criteria().isDefined ) 	_uri += s"""?CRITERIA=(${criteria().get})"""	
			if ( parameter().isDefined ) 
			{
				if ( criteria().isDefined ) _uri += s"""&PARAMETER=(${parameter().get})"""
				else 						_uri += s"""?PARAMETER=(${parameter().get})"""	
			}	
			Some(_uri)
		}

	}

	def doGet() {
		uri() match {
			case Some(x) => {
    			val response = ScalaJ(x, usuario, password)
    			response
			}
			case None	=> {
				//logger.error("...")
				//None
			}
		}
      	//println(response.asParamMap)

  //   	println("---> response.body")
  //   	println(response.body)
  //   	println(" ")
		// println("---> response.code")
		// println(response.code)
		// println(" ")
		// println("---> response.headers")
		// println(response.headers)
		// println(" ")
		// println("---> response.cookies")
		// println(response.cookies)
		// println(" ")

      	// http://alvinalexander.com/scala/how-to-extract-data-from-xml-nodes-in-scala
      	// http://www.scala-lang.org/api/current/scala-xml/
      	
      	val body = scala.xml.XML.loadString(response.asString.body)

      	val resultsummary = ( (body \ "resultsummary").head ).attributes.asAttrMap
      	//println( resultsummary("api_response1_alt") )

      	val records = (body \ "records").head.child.map(_.attributes.asAttrMap).filter(_.size > 0)
      	//println(records)

      	// val kk = for ( r <- (body \ "records").head.child if r.size > 0 ) {
      	// 	r.attributes.asAttrMap ++ ("label", r.label)
      	// } 
      	// println(kk)


	}

}