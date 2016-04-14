package jimmy

import scalaj.http._
import java.net.Proxy


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

object MyHttp extends BaseHttp (
  proxyConfig = Some(java.net.Proxy.NO_PROXY),
  charset = HttpConstants.utf8
)	

class CmciResponse(request: HttpRequest) {
	val response      = request.asString
	
	println(response)

	val code          = response.code
	def headers       = response.headers	
	def cookies       = response.cookies

	// http://alvinalexander.com/scala/how-to-extract-data-from-xml-nodes-in-scala
	// http://www.scala-lang.org/api/current/scala-xml/
	val body          = scala.xml.XML.loadString(response.body)
	val resultsummary = (body \ "resultsummary").head.attributes.asAttrMap
	val records       = _records()

	private def _records() = {
		val _bodyrecords  = (body \ "records").head.child
		val _records      = _bodyrecords.map(_.attributes.asAttrMap).filter(_.size > 0)
		val _label        = _bodyrecords.map(_.label).filterNot(_.contains("#PCDATA")).head

		for { r <- _records } yield Record(_label, r)
	}
}

case class Record(tipo: String, props: Map[String,String])

class Cmci(entorno: String, usuario: String, password: String) {

	def doGet: Option[CmciResponse] = {
		// val cmci = Cmci("SIST").scope("CICSJCOA").tabla("CICSLocalTransaction").limit(10).criteria("TRANID=XA*")
	    val _uri = uri
	    if ( _uri.isDefined ) {
	    	val request  = MyHttp(_uri.get).auth(usuario, password)
			val response = new CmciResponse(request)
	    	Some(response)
	    }
	    else None
	}

	def doPost(xml: String): Option[CmciResponse] = {
		// val cmci = Cmci("SIST").scope("CICSJCOA").tabla("CICSDefinitionTransaction")
	    val _uri = uri
	    if ( _uri.isDefined ) {
	    	val request  = MyHttp(_uri.get).auth(usuario, password).postData(xml).method("post").header("content-type", "application/xml")
			val response = new CmciResponse(request)
	    	Some(response)
	    }
	    else None
	}

	def doPut(xml: String): Option[CmciResponse] = {
		// val cmci = Cmci("SIST").scope("CICSJCOA").tabla("CICSDefinitionTransaction").criteria("NAME=MIKE").parameter("CSDGROUP(JIMMY)")
	    val _uri = uri
	    if ( _uri.isDefined ) {
	    	val request  = MyHttp(_uri.get).auth(usuario, password).postData(xml).method("put").header("content-type", "application/xml")
			val response = new CmciResponse(request)
	    	Some(response)
	    }
	    else None
	}

	def doDelete: Option[CmciResponse] = {
		// val cmci = Cmci("SIST").scope("CICSJCOA").tabla("CICSDefinitionTransaction").criteria("NAME=MIKE").parameter("CSDGROUP(JIMMY)")
	    val _uri = uri
	    if ( _uri.isDefined ) {
	    	val request  = MyHttp(_uri.get).auth(usuario, password).method("delete")
			val response = new CmciResponse(request)
	    	Some(response)
	    }
	    else None
	}

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

	def uri: Option[String] = {
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
				if ( criteria().isDefined ) _uri += s"""&PARAMETER=${parameter().get}"""
				else 						_uri += s"""?PARAMETER=${parameter().get}"""	
			}	
			Some(_uri)
		}

	}
}

object Cmci {
	def apply(entorno: String): Cmci = {
		val usuario: String  = Config("usuario").get
		val password: String = Config("password").get
		new Cmci(entorno, usuario, password)
	}
}
