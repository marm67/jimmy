// package jimmy

// //------------------------------------------------------------------------------------
// // Ejemplos de url
// //------------------------------------------------------------------------------------
// // http://10.145.254.228:8632/CICSSystemManagement/CICSLocalTransaction/JPLX1/CICSJCOA//10?CRITERIA=(TRANID=XA*)
// // http://10.145.254.228:8632/CICSSystemManagement/CICSLocalTransaction/JPLX1/CICSJCOA?CRITERIA=(TRANID=X*)&SUMMONLY
// //
// // http://10.145.254.228:8632/CICSSystemManagement/CICSLocalFile/JPLX1/JPLX1/?CRITERIA=(FILE=EQ* AND ENABLESTATUS=UNENABLED)
// //
// // http://10.145.254.228:8632/CICSSystemManagement/CICSDefinitionTransaction/JPLX1/CICSJCOA?CRITERIA=(NAME=XALA)&PARAMETER=CSDGROUP(XXTR00L)
// // http://10.145.254.228:8632/CICSSystemManagement/CICSDefinitionTransaction/JPLX1/CICSJCOA?CRITERIA=(NAME=X*)&PARAMETER=CSDGROUP(XXTR00L)
// // http://10.145.254.228:8632/CICSSystemManagement/CICSDefinitionTransaction/JPLX1/CICSJCOA?PARAMETER=CSDGROUP(DFHEDF)
// //
// // http://10.145.254.228:8632/CICSSystemManagement/CICSDefinitionFile/JPLX1/CICSJCOA?CRITERIA=(NAME=NAGRBATK)&PARAMETER=CSDGROUP(NAGRUPOA)

// //------------------------------------------------------------------------------------
// // Forma de invocacion
// //------------------------------------------------------------------------------------
// // 	$cmci->entorno('sistemas')
// // 		 ->tabla('CICSLocalFile')
// // 		 ->scope('CICSJCOA')
// // 		 ->criteria('NAME=XALA')
// // 		 ->parameter('CSDGROUP(XXTR00L)')
// // 	;
// //  $recursos = $cmci->doGet();

// object Cmci {
// 	def apply(entorno: String): Cmci = {
// 		val usuario: String  = Config("usuario").get
// 		val password: String = Config("password").get
// 		new Cmci(entorno, usuario, password)
// 	}
// }

// case class UriMap(
// 	  host: Option[String]
// 	, port: Option[String]
// 	, context: Option[String]
// 	, scope: Option[String]
// 	, tabla: Option[String]
// 	, limit: Option[String]
// 	, groupBy: Option[String]
// 	, criteria: Option[String]
// 	, parameter: Option[String]
// )

// class Cmci(entorno: String, usuario: String, password: String) {

// 	val vars: scala.collection.mutable.LinkedHashMap[String, Option[String]]  = scala.collection.mutable.LinkedHashMap(
// 		  "protocolo" 	-> Some("http")
// 		, "host" 		-> Config(s"cmci.$entorno.host")
// 		, "port" 		-> Config(s"cmci.$entorno.port")
// 		, "csm" 		-> Some("CICSSystemManagement")
// 		, "tabla" 		-> None
// 		, "context" 	-> Config(s"cmci.$entorno.context")
// 		, "scope" 		-> Config(s"cmci.$entorno.scope")
// 		, "_limit" 		-> None
// 		, "_groupBy" 	-> None
// 		, "_criteria" 	-> None
// 		, "_parameter" 	-> None
// 	)

// 	def context(c: String): Cmci   = { vars("context")	 = Some(c); this }
// 	def scope(c: String): Cmci     = { vars("scope")	 = Some(c); this }
// 	def tabla(c: String): Cmci     = { vars("tabla")	 = Some(c); this }
// 	def limit(c: String): Cmci     = { vars("limit")	 = Some(c); this }
// 	def groupBy(c: String): Cmci   = { vars("groupBy")	 = Some(c); this }
// 	def criteria(c: String): Cmci  = { vars("criteria")	 = Some(c); this }
// 	def parameter(c: String): Cmci = { vars("parameter") = Some(c); this }

// 	def context(): Option[String]   = vars("context")
// 	def scope(): Option[String]     = vars("scope")
// 	def tabla(): Option[String]     = vars("tabla")
// 	def limit(): Option[String]     = vars("limit")
// 	def groupBy(): Option[String]   = vars("groupBy")
// 	def criteria(): Option[String]  = vars("criteria")
// 	def parameter(): Option[String] = vars("parameter")

// 	//def uriBase: String = s"""$protocolo://$host:$port/$csm"""
// 	def uri = {
// 		//s"""$protocolo://$host:$port/$csm"""
// 		vars.values map { k => k match {
// 				case Some(v) => v
// 				case _ 		 => None
// 			}
// 		}

// 		// val requeridos = vars.filterKeys(! _.startsWith("_") )
// 		// val opcionales = vars.filterKeys(  _.startsWith("_") )

// 		val (opcionales, requeridos) = vars partition ( _._1.startsWith("_") )
// 		val lista = requeridos.values.toList
// 		println(lista)
// 		val kk: List[String] = lista.foldLeft (List[String]()) { (acum: List[String], v: Option[String]) =>
// 			v match {
// 				case None		=> Nil
// 				case Some(x)	=> x :: acum
// 			}
// 		}
// 		kk
// 		val kk1: Option[String] = lista.foldLeft (Some("")) { (acum: Option[String], v: Option[String]) =>
// 			acum match {
// 				case None		=> None
// 				case Some(x)	=> v match {
// 					case Some(y) => Some(x + y)
// 					case None => None
// 				}
				
// 			}
// 		}
// 		kk1
// 		// .map{ _ match { 
// 		//   case Some(x) => x
// 		//   case None => None
// 		// }}
// 		//.mkString("/")

// 		// http://10.145.254.228:8632/CICSSystemManagement/CICSLocalTransaction/JPLX1/CICSJCOA//10?CRITERIA=(TRANID=XA*)
// 		// http://10.145.254.228:8632/CICSSystemManagement/CICSLocalTransaction/JPLX1/CICSJCOA
// 		// s"""${uriBase}/${tabla}/${context}/${scope}?${criteria}&${parameter}"""
// 		// var kk = s"""$protocolo://$host:$port/$csm"""
// 		// tabla match {
// 		// 	case Some(t)	=>  kk += s"/$t"
// 		// 	case None		=>  kk
// 		// }
// 	}

// 	def scalajCmci() {
// 		var url = "http://10.145.254.228:8632/CICSSystemManagement/CICSLocalTransaction/JPLX1/CICSJCOA//10?CRITERIA=(TRANID=XA*)"
//     	val response = ScalaJ(url, "_usuario", "_password")
//       	//println(response.asParamMap)

//   //   	println("---> response.body")
//   //   	println(response.body)
//   //   	println(" ")
// 		// println("---> response.code")
// 		// println(response.code)
// 		// println(" ")
// 		// println("---> response.headers")
// 		// println(response.headers)
// 		// println(" ")
// 		// println("---> response.cookies")
// 		// println(response.cookies)
// 		// println(" ")

//       	// http://alvinalexander.com/scala/how-to-extract-data-from-xml-nodes-in-scala
//       	// http://www.scala-lang.org/api/current/scala-xml/
      	
//       	val body = scala.xml.XML.loadString(response.asString.body)

//       	val resultsummary = ( (body \ "resultsummary").head ).attributes.asAttrMap
//       	//println( resultsummary("api_response1_alt") )

//       	val records = (body \ "records").head.child.map(_.attributes.asAttrMap).filter(_.size > 0)
//       	//println(records)

//       	// val kk = for ( r <- (body \ "records").head.child if r.size > 0 ) {
//       	// 	r.attributes.asAttrMap ++ ("label", r.label)
//       	// } 
//       	// println(kk)


// 	}

// }