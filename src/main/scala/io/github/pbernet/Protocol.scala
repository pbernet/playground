package io.github.pbernet

import spray.json.DefaultJsonProtocol

trait Protocol extends DefaultJsonProtocol {

  implicit val ipInfoFormat = jsonFormat5(IpInfo.apply)
  implicit val ipPairSummaryRequestFormat = jsonFormat2(IpPairSummaryRequest.apply)
  implicit val ipPairSummaryFormat = jsonFormat3(IpPairSummary.apply)

}
