package qt.network.test

import qt.core.{QByteArray, QByteArrayValue, QCoreApplication, QJsonArray, QJsonDocument, QJsonObject, QJsonValue, QString, QStringList, QUrl}
import qt.network.{QNetworkAccessManager, QNetworkRequest}

import scala.scalanative.cobj.ResultValue
import scalanative._
import unsafe._

object Main {
  def main(args: Array[String]): Unit = Zone{ implicit z =>
    QCoreApplication(args)
    val mgr = QNetworkAccessManager()
    val resp = mgr.get("http://www.omdbapi.com/?apikey=381e81be&s=double")
    resp.onFinished{ () => Zone{ implicit z =>
      val bytes = QByteArray.value
      bytes := resp.readAll()
      val doc = QJsonDocument.value
      doc := QJsonDocument.fromJson(bytes)
      val obj = QJsonObject.value
      obj := doc.rootObject()
      println(obj.getArray("Search").get.getObject(0).get.getString("Title"))
      QCoreApplication.quit()
    } }
    QCoreApplication.exec()
  }
}
