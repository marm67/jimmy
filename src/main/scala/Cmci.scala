package jimmy

object Cmci {
	def apply(entorno: String): Cmci = {
		val usuario: String  = Config("usuario")
		val password: String = Config("password")
		new Cmci(entorno, usuario, password)
	}
}

class Cmci(entorno: String, usuario: String, password: String) {

	private val protocolo                 = "http"
	private val csm                       = "CICSSystemManagement"
	
	private val host: String              = Config(s"cmci.$entorno.host")
	private val port: String              = Config(s"cmci.$entorno.port")
	private val defaultContext: String    = Config(s"cmci.$entorno.context")
	private val defaultScope: String      = Config(s"cmci.$entorno.scope")
	private val defaultLimit: String      = ???
	
	private var context: Option[String]   = None
	private var scope: Option[String]     = None
	private var tabla: Option[String]     = None
	private var limit: Option[String]     = None
	private var groupBy: Option[String]   = None
	private var criteria: Option[String]  = None
	private var parameter: Option[String] = None

	def uriBase: String = s"""$protocolo://$host:$port/$csm"""

	def setContext(c: String): Unit   = context = Some(c)
	def getContext(): String          = context.getOrElse(defaultContext)
	def setScope(c: String): Unit     = scope = Some(c)
	def getScope(): String            = scope.getOrElse(defaultScope)
	def setTabla(c: String): Unit     = tabla = Some(c)
	def getTabla(): String            = tabla.getOrElse(defaultTabla)
	def setLimit(c: String): Unit     = limit = Some(c)
	def getLimit(): String            = limit.getOrElse(defaultLimit)
	def setGroupBy(c: String): Unit   = groupBy = Some(c)
	def getGroupBy(): String          = groupBy.getOrElse(defaultGroupBy)
	def setCriteria(c: String): Unit  = criteria = Some(c)
	def getCriteria(): String         = criteria.getOrElse(defaultCriteria)
	def setParameter(c: String): Unit = parameter = Some(c)
	def getParameter(): String        = parameter.getOrElse(defaultParameter)

	def scalajCmci() {
		var url = "http://10.145.254.228:8632/CICSSystemManagement/CICSLocalTransaction/JPLX1/CICSJCOA//10?CRITERIA=(TRANID=XA*)"
    	val response = ScalaJ(url, "_usuario", "_password")
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