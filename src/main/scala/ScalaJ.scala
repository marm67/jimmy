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