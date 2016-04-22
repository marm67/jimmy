package jimmy

import grizzled.slf4j.Logging
import scalaj.http._
import scalaj.http.Base64
import java.net.Proxy


//------------------------------------------------------------------------------------
// Ejemplos de url
//------------------------------------------------------------------------------------
// http://10.145.254.228:8632/CICSSystemManagement/CICSLocalTransaction/JPLX1/CICSJCOA//10?CRITERIA=(TRANID=XA*)
// http://10.145.254.228:8632/CICSSystemManagement/CICSLocalTransaction/JPLX1/CICSJCOA?CRITERIA=(TRANID=X*)&SUMMONLY
//
// http://10.145.254.228:8632/CICSSystemManagement/CICSLocalFile/JPLX1/JPLX1/?CRITERIA=(FILE=EQ* AND ENABLESTATUS=UNENABLED)
// http://10.145.254.228:8632/CICSSystemManagement/CICSLocalTransaction/JPLX1/CICSJCOA?CRITERIA=(TRANID=X* AND PROGRAM=XABENDCO)
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

class Cmci private(entorno: String, usuario: String, password: String) extends Logging {

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

	def context(c: String): Cmci   = { vars("context")	 = Some(c.toUpperCase); this }
	def scope(c: String): Cmci     = { vars("scope")	 = Some(c.toUpperCase); this }
	def tabla(c: String): Cmci     = { vars("tabla")	 = Some(c); this }
	def limit(c: String): Cmci     = { vars("_limit")	 = Some(c); this }
	def limit(c: Int): Cmci    	   = { vars("_limit")	 = Some(c.toString); this }
	def groupBy(c: String): Cmci   = { vars("_groupBy")	 = Some(c.toUpperCase); this }
	def criteria(c: String): Cmci  = { vars("_criteria") = Some(c.toUpperCase); this }
	def parameter(c: String): Cmci = { vars("_parameter") = Some(c.toUpperCase); this }

	def context(): Option[String]   = vars("context")
	def scope(): Option[String]     = vars("scope")
	def tabla(): Option[String]     = vars("tabla")
	def limit(): Option[String]     = vars("_limit")
	def groupBy(): Option[String]   = vars("_groupBy")
	def criteria(): Option[String]  = vars("_criteria")
	def parameter(): Option[String] = vars("_parameter")

	def doGet: TCmciResponse = {
		// val cmci = Cmci("SIST").scope("CICSJCOA").tabla("CICSLocalTransaction").limit(10).criteria("TRANID=XA*")
	    val _uri = uri
	    if ( _uri.isDefined ) {
	    	val request  = MiHttp(_uri.get).auth(usuario, password)
			new CmciResponse(request)
	    }
	    else {
	    	logger.error( s"URL invalida: ${_uri}" )
	    	new CmciResponseErrorUri()
	    }
	}

	def doPost(xml: String): Option[CmciResponse] = {
		// val cmci = Cmci("SIST").scope("CICSJCOA").tabla("CICSDefinitionTransaction")
	    val _uri = uri
	    if ( _uri.isDefined ) {
	    	val request  = MiHttp(_uri.get).auth(usuario, password).postData(xml).method("post").header("content-type", "application/xml")
			val response = new CmciResponse(request)
	    	Some(response)
	    }
	    else None
	}

	def doPut(xml: String): Option[CmciResponse] = {
		// val cmci = Cmci("SIST").scope("CICSJCOA").tabla("CICSDefinitionTransaction").criteria("NAME=MIKE").parameter("CSDGROUP(JIMMY)")
	    val _uri = uri
	    if ( _uri.isDefined ) {
	    	val request  = MiHttp(_uri.get).auth(usuario, password).postData(xml).method("put").header("content-type", "application/xml")
			val response = new CmciResponse(request)
	    	Some(response)
	    }
	    else None
	}

	def doDelete: Option[CmciResponse] = {
		// val cmci = Cmci("SIST").scope("CICSJCOA").tabla("CICSDefinitionTransaction").criteria("NAME=MIKE").parameter("CSDGROUP(JIMMY)")
	    val _uri = uri
	    if ( _uri.isDefined ) {
	    	val request  = MiHttp(_uri.get).auth(usuario, password).method("delete")
			val response = new CmciResponse(request)
	    	Some(response)
	    }
	    else None
	}

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

			var _no_escaped = ""
			var _escaped = ""
			if ( criteria().isDefined ) { 
				val s = s"""(${criteria().get})"""
				_no_escaped += s"""?CRITERIA=${s}""" 
				_escaped 	+= s"""?CRITERIA=${escape(s)}""" 
			}	
			if ( parameter().isDefined ) 
			{
				val s = s"""${parameter().get}"""
				if ( criteria().isDefined ) { 
					_no_escaped += s"""&PARAMETER=${s}""" 
					_escaped    += s"""&PARAMETER=${escape(s)}""" 
				}
				else {
					_no_escaped += s"""?PARAMETER=${s}"""
					_escaped    += s"""&PARAMETER=${escape(s)}""" 
				} 
			}

			_no_escaped = _uri + _no_escaped
			_escaped = _uri + _escaped

	    	logger.info( s"""GET ${_no_escaped}""" )
	    	
			Some(_escaped)
		}

	}

//	private def escape(s: String): String = HttpConstants.urlEncode(s, HttpConstants.utf8 )
	private def escape(s: String): String = CmciUtils.escape(s)

	object MiHttp extends BaseHttp (
	  proxyConfig = Some(java.net.Proxy.NO_PROXY),
	  charset = HttpConstants.utf8
	)	

	object CmciUtils {
		val tconv: Map[Char,String] = Map(
			  ' ' -> "%20"
			, '!' -> "%21" 
			, '#' -> "%23" 
			, '$' -> "%24" 
			, '%' -> "%25" 
			, '&' -> "%26" 
			, ''' -> "%27" 
			, '(' -> "%28" 
			, ')' -> "%29" 
			, '*' -> "%2A" 
			, '+' -> "%2B" 
			, ',' -> "%2C" 
			, '-' -> "%2D"     
			, '.' -> "%2E" 
			, '/' -> "%2F" 
			, ':' -> "%3A" 
			, ';' -> "%3B" 
			, '<' -> "%3C" 
			, '=' -> "%3D" 
			, '>' -> "%3E" 
			, '?' -> "%3F" 
			, '@' -> "%40" 
			, '[' -> "%5B" 
			, ']' -> "%5D" 
			, '¬' -> "%AC" 
		)

		def escape(cadena: String): String = {
			val x = scala.collection.mutable.ArrayBuffer[String]()
			for ( ch1 <- cadena )  x += tconv.getOrElse(ch1, ch1.toString)
			x.mkString
		}

	}
}

case class Record(tipo: String, props: Map[String,String])

trait CmciResult
case class CmciResultOK(resultsummary: Map[String, String], records: List[Record]) {
	def isSuccess  = true
}
case class CmciResultError(resultsummary: Map[String, String], records: List[Record]) {
	def isSuccess  = false
}

trait TCmciResponse {
	def isSuccess: Boolean
	def error: String
	def records: List[Record]
}

class CmciResponseErrorUri extends TCmciResponse {
	def isSuccess: Boolean    = false
	def error: String         = "ERROR. URL no es valida"
	def records: List[Record] = List[Record]()
}

class CmciResponse(request: HttpRequest) extends Logging with TCmciResponse {

	def isSuccess: Boolean    = response.isSuccess && resultsummary("api_response1_alt") == "OK"
	def error: String         = if (response.isSuccess) resultsummary("api_response1_alt") else response.code.toString
	def records: List[Record] = _records
	
	private val response                          = request.asString
	private var body: scala.xml.Elem              = null
	private var resultsummary: Map[String,String] = null
	private var _records                          = List[Record]()

	if ( response.isSuccess ) {
		// http://alvinalexander.com/scala/how-to-extract-data-from-xml-nodes-in-scala
		// http://www.scala-lang.org/api/current/scala-xml/
		body             = scala.xml.XML.loadString(response.body)
		resultsummary    = (body \ "resultsummary").head.attributes.asAttrMap
		val _bodyrecords = (body \ "records").head.child
		val __records    = _bodyrecords.map(_.attributes.asAttrMap).filter(_.size > 0)
		val _label       = _bodyrecords.map(_.label).filterNot(_.contains("#PCDATA")).head
		_records         = ( for { r <- __records } yield Record(_label, r) ).toList
	} 
	else {
		logger.error( s"Response Code ${response.code}" )
	}
}

