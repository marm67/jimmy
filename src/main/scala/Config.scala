import java.io._
import com.typesafe.config.ConfigFactory
import collection.JavaConversions._

trait Config {

    // private val CONF_PATH = """c:/scala/proyectos/jimmy/resources/conf"""
    private val CONF_PATH = 
    	if ( ConfigFactory.load().getString("os.name") == "Mac OS X" )
    		"""/Users/miguel/programacion/scala/jimmy/resources/conf"""
    	else
    		"""c:/scala/proyectos/jimmy/resources/conf"""

    val AppConfig = ConfigFactory.parseFile(new File(CONF_PATH + """/app.conf""")).resolve()

    def getCicsEntorno(t: String) = AppConfig.getStringList( s"""plataforma.$t""" ).toList

}