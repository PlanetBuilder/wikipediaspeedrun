package de.planetbuilder.wikipediaspeedrun

import de.planetbuilder.wikipediaspeedrun.gamemanager.GamesManager
import de.planetbuilder.wikipediaspeedrun.gamemanager.WikipediaManager

fun main() {
    WikipediaManager.getPageFromURI("https://en.wikipedia.org/wiki/Main_Page")
    val gamesManager = GamesManager()
    gamesManager.start(9001)
}