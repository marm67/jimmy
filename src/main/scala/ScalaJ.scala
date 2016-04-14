package jimmy

import scalaj.http._
import java.net.Proxy

object ScalaJ {

	// object MyHttp extends BaseHttp (
	//   proxyConfig: Option[Proxy] = None,
	//   options: Seq[HttpOptions.HttpOption] = HttpConstants.defaultOptions,
	//   charset: String = HttpConstants.utf8,
	//   sendBufferSize: Int = 4096,
	//   userAgent: String = "scalaj-http/1.0",
	//   compress: Boolean = true
	// )	

	object MyHttp extends BaseHttp (
	  proxyConfig = Some(java.net.Proxy.NO_PROXY),
	  charset = HttpConstants.utf8
	)	

	def apply(url: String) = {
		//Http("http://foo.com").{responseCode, asString, asXml, asBytes, asParams} 
    	MyHttp(url)
	}

	def apply(url: String, username: String, password: String) = {
		MyHttp(url).auth(username, password)
	}
}


//println(response.asParamMap)

//println("---> response.body")
//println(response.body)
//println(" ")
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

// val body = scala.xml.XML.loadString(response.asString.body)

// val resultsummary = ( (body \ "resultsummary").head ).attributes.asAttrMap
// //println( resultsummary("api_response1_alt") )

// val records = (body \ "records").head.child.map(_.attributes.asAttrMap).filter(_.size > 0)
//println(records)

// val kk = for ( r <- (body \ "records").head.child if r.size > 0 ) {
// 	r.attributes.asAttrMap ++ ("label", r.label)
// } 
// println(kk)

