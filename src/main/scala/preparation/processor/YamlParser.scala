package preparation.processor

import java.io.FileReader

import Extraction.fields.CricketMatch
import org.yaml.snakeyaml.Yaml
import org.yaml.snakeyaml.constructor.Constructor
import org.yaml.snakeyaml.representer.Representer

object YamlParser {

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
      case e: Exception => None
    }
    finally {
      yamlFile match {
        case Some(x) =>  yamlFile.get.close()
        case None => None
      }

    }


  }
}
