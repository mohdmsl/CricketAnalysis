package Extraction.fields

case class CricketMatch(
                    meta: Meta,
                    info: Info,
                    innings: List[Map[String, Batting]]

                  )
