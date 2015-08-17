package marcin

import scala.util.matching.Regex.Match

/**
 * Created by m on 2015-05-30.
 */

  class Matcher(group: String) {

  class BetterMatch(val m:Match){
    def firstMatch=m.group(0)
  }

  implicit def bla(m:Match)=new BetterMatch(m)

    private var line: Option[String] = None
    private var prefix = ""

    def forText(line: String): Matcher = {
      this.line = Some(line)
      this
    }

    def isMatching: Boolean = {
      !group.r.findFirstMatchIn(line.get).isEmpty
    }

    def forMatching(f:Match=>Any): Matcher ={
      if(isMatching) {
        f(getMatch)
      }
      return this
    }

    def getMatch: Match = {
      group.r.findFirstMatchIn(line.get).get
    }

    def getMatchNr(n: Int): String = {
      (prefix + group).r.findFirstMatchIn(line.get).map(_ group n).getOrElse("")
    }

    def setStartingSpaces(num: Int): Matcher = {
      this.prefix = """^\s{"""+num+"""}"""
      this
    }
  }


