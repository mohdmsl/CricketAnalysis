package Extraction.fields

case class Delivery(
                batsman: String,
                bowler: String,
                non_striker: String,
                runs: Runs,
                wicket: Wicket,
                extras: Extras
              )
