package main.scala

import scala.io.Source
// import main.java.CSSInliner

object Main extends App { //with CSSInliner {
  
  val pathResources = "c:/scala/proyectos/jimmy/resources/"

  override def main(args: Array[String]) {
    //pruebaScript(args(0))
    //pruebaPlantillas()
    //pruebaConf()
    //pruebaConfEntornos()
    pruebaReglas()
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
    
    val p = new Parser
    val query = p.parseUno(script)
  }

  def pruebaConf() = {
    import java.io._
    import com.typesafe.config.ConfigFactory
     
    val path = """c:/scala/proyectos/jimmy/resources/conf/app.conf"""
    val config = ConfigFactory.parseFile(new File(path))
    println(config)
    println(config.getString("app.path.plantillas"))


    // val sysConfig = ConfigFactory.load()
    // println(sysConfig)
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
