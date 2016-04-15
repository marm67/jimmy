/*
    fatJar -> Comando 'assembly' en sbt
 */
package jimmy

import jimmy._
import scala.io.Source
import java.io.File
// import main.java.CSSInliner
import grizzled.slf4j.Logging

case class CmdOpciones(
    config: File = new File("./jimmy.conf")
  , usuario: String = ""
  , password: String = ""
  , script: String = ""
)

object Main extends Logging { //extends App { //with CSSInliner {
  // run -s "f:/scala/proyectos/jimmy/resources/ejemplos/script01" -c "f:/scala/proyectos/jimmy/resources/conf/app.conf" -u u -p p
  // run -s "c:/scala/proyectos/jimmy/resources/ejemplos/script01" -c "c:/scala/proyectos/jimmy/resources/conf/app.conf" -u u -p p
  def main(args: Array[String]) {
    if (loadConfig(args)) {
      //pruebaCmciGet
      pruebaCics
    }
    //if (loadConfig(args)) runScript()
    //pruebaScript(args(0))
    //pruebaPlantillas()
    //pruebaAssembly()
    //pruebaConfEntornos()
    //pruebaReglas()
    //prueba
  }

  def pruebaCics = {
    Cics("CICSJCOA").inquire( "TRAN"->"X*", "PROGRAM"->"XABENDCO")
    // Cics("CICSJCOA").inquire.TRAN("X*").PROGRAM("XABENDCO")
  }

  def pruebaCmciGet = {
    val cmci = Cmci("SIST").scope("CICSJCOA").tabla("CICSLocalTransaction").limit(10).criteria("TRANID=XA*")
    val response = cmci.doGet.get

    val body = response.body
    println(response.records)
  }

  def pruebaCmciPost = {
    val cmci = Cmci("SIST").scope("CICSJCOA").tabla("CICSDefinitionTransaction")
    val xml = """<request><create><parameter name="CSD"/><attributes name="MIKE" program="POST" remotesystem="JCOB" csdgroup="JIMMY"/></create></request>"""

    println(cmci.uri)
    val response = cmci.doPost(xml).get
    println(response.response)
  }

  def pruebaCmciPut = {
    val cmci = Cmci("SIST").scope("CICSJCOA").tabla("CICSDefinitionTransaction").criteria("NAME=MIKE").parameter("CSDGROUP(JIMMY)")
    val xml = """<request><update><attributes program="PUT"/></update></request>"""

    println(cmci.uri)
    val response = cmci.doPut(xml).get
    println(response.response)
  }
  
  def pruebaCmciDelete() = {
    val cmci = Cmci("SIST").scope("CICSJCOA").tabla("CICSDefinitionTransaction").criteria("NAME=MIKE").parameter("CSDGROUP(JIMMY)")
    println(cmci.uri)

    val response = cmci.doDelete.get
    println(response.response)
  }
  
  def pruebaConfig() = {
    val config = Config.getConfig
    println(config)
    println(config.getString("app.path.plantillas"))
  }

  private def loadConfig(args: Array[String]): Boolean = {
    val parser = new scopt.OptionParser[CmdOpciones]("jimmy") {
      head("jimmy", "1.x")
      opt[String]('s', "script")  valueName("<script>") required() action { (x, c) => c.copy(script = x) } text("Fichero de script requerido") 
      opt[String]('u', "usuario")   valueName("<usuario>")  required() action { (x, c) => c.copy(usuario = x) } text("usuario requerido")
      opt[String]('p', "password")  valueName("<password>") required() action { (x, c) => c.copy(password = x) } text("password requerida") 
      opt[File]('c', "config")      valueName("<fichero>") action { (x, c) => c.copy(config = x) } text("Fichero de configuracion, por defecto ./jimmy.conf")
    }

    parser.parse(args, CmdOpciones()) match {
      case Some(opciones) => {
        Config.load(opciones)
        true
      }
      case None =>
        println("chungo")
        false
    }
  }

  private def runScript() = {
    val fscript = Config("script").get

    if ( !new java.io.File(fscript).exists ) {
      val msg = s"""ERROR. No existe el fichero '$fscript'""" 
      logger.error(msg)
      System.exit(0)
    }

    /* test file readable */
    /* return exit code error (non zero) if a file is not readable */
    if ( !new java.io.File(fscript).canRead) {
      logger.error( s"""El fichero no es legible '$fscript'""")
      System.exit(0)
    }

    val script = Source.fromFile(fscript).mkString
    logger.info("Script:\n" + script + "**\n")
    
//    val p = new Parser
    val query = Parser(script)
  }

  def prueba1 = {
    import com.typesafe.config.ConfigFactory
    val osName = ConfigFactory.load().getString("os.name")
    println(osName)
  }
  
  def pruebaReglas() = {
    // import java.io._

    // val xml = """<cicslocaltransaction tranclass="DFHTCL00" tranid="XALA" trprof="" twasize="0" usecount="57"/>"""
    // val pathReglas = """c:/scala/proyectos/jimmy/resources/reglas/rules.css"""

    // val xml1 = inlineStyles(xml, new File(pathReglas), false)

    // println(xml)
    // println(xml1)

  }

  def pruebaScript(s: String) = {
    val path = s"""c:/scala/proyectos/jimmy/resources/ejemplos/$s"""
    val script = Source.fromFile(path).mkString.toUpperCase
    println(script.mkString)
    
    val query = Parser(script)
  }

  def pruebaAssembly() = {
    import java.io._
    import com.typesafe.config.ConfigFactory
    
    val config = ConfigFactory.parseFile(new File("application.conf")).resolve() 
//    val config = ConfigFactory.load()
    println(config)
    val kk = config.getString("cmci.produccion.port")
    println(s"""cmci.produccion.port = $kk""")


  }

  def pruebaConf() = {
    import java.io._
    import com.typesafe.config.ConfigFactory
     
    val path = """c:/scala/proyectos/jimmy/resources/conf/app.conf"""
    val config = ConfigFactory.parseFile(new File(path))
    println(config)
    println(config.getString("app.path.plantillas"))


  }

  def pruebaConfEntornos() = {
    import java.io._
    import collection.JavaConversions._
    import com.typesafe.config.ConfigFactory
     
    val path = """c:/scala/proyectos/jimmy/resources/conf/entornos.conf"""
    val config = ConfigFactory.parseFile(new File(path)).resolve()
//    println(config)
    val tores = config.getStringList("produccion.comercial.elis").toList
    println(tores)
  }

  def pruebaPlantillas {
    import java.io._
    import java.util._
    import freemarker.template._
    import scala.collection.JavaConversions._
    import scala.collection.JavaConverters._
    import scala.collection.mutable.ArrayBuffer

    println("...inicio...")

    val pathPlantillas = """c:\scala\proyectos\jimmy\resources\plantillas"""

    val cfg = new Configuration
    cfg.setDirectoryForTemplateLoading(new File(pathPlantillas))
    try {
        val template = cfg.getTemplate( "sda.ftl" )
         
        val data = scala.collection.mutable.Map[String, String]()
        data += ("transaccion" -> "A5MI")
        data += ("programa" -> "A500CPCO")
 
        // write to string
        val output = new StringWriter
        template.process(data.asJava, output)
        
        val stringResult = output.toString
        // do whatever you want/need to do with the string here ...
        println(stringResult) 
        println("...fin...")
    } catch {
        case t: Throwable => println(t) //t.printStaceTrace
    }
  }

}
