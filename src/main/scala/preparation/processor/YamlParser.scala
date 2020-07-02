package preparation.processor

import java.io.FileReader

import preparation.Modal.CricketMatch
import org.slf4j.{Logger, LoggerFactory}
import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.constructor.Constructor
import org.yaml.snakeyaml.representer.Representer

object YamlParser {
@transient lazy val logger = LoggerFactory.getLogger(this.getClass)
  def parseYamlFile(path: String): Option[CricketMatch] = {
    var yamlFile = None: Option[FileReader]

    try {
      val representer = new Representer
      representer.getPropertyUtils.setSkipMissingProperties(true)
      val yaml = new Yaml(new Constructor(classOf[CricketMatch]),representer)
      yamlFile = Some(new FileReader(path))
      Some(yaml.load(yamlFile.get).asInstanceOf[CricketMatch])
    }
    catch {

      case e: Exception =>
        logger.error("unable to load file:" + " exception " + e.getMessage)
        None
    }
    finally {
      yamlFile match {
        case Some(x) =>  yamlFile.get.close()
        case None => None
      }

    }


  }
}
