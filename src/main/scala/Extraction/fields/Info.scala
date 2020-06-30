package Extraction.fields

case class Info(
                 city: String,
                 dates: List[String],
                 gender: String,
                 match_type: String,
                 outcome: Outcome,
                 overs: Int,
                 player_of_match: List[String],
                 teams: List[String],
                 toss: Toss,
                 umpires: List[String],
                 venue: String
               )
