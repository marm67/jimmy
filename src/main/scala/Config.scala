package main.scala

import java.io._
import com.typesafe.config.ConfigFactory
import collection.JavaConversions._

object Config {

	private var user: String                       = null
	private var pass: String                       = null
	private var config: com.typesafe.config.Config = null

	def load(opciones: CmdOpciones) = {
		user   = opciones.usuario
		pass   = opciones.password
		config = ConfigFactory.parseFile(opciones.config).resolve()		
	}

	def usuario  = user
	def password = pass
	def apply(s: String) = config.getString(s)
    def getCicsEntorno(t: String) = config.getStringList( s"""plataforma.$t""" ).toList
    
}
