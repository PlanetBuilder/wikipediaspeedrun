package de.planetbuilder.wikipediaspeedrun.gamemanager

import java.util.NoSuchElementException
import java.util.UUID

class Game(
    private val gameSettings: GameSettings
) {
    val uuid = UUID.randomUUID()
    var language: String = if (WikipediaLanguage.languages.contains(gameSettings.language)) gameSettings.language else WikipediaLanguage.ENGLISH
    private val players = ArrayList<Player>();
    var startTime: Long = -1;
    var stopTime: Long = -1;
    var running = false
    lateinit var startPage: WikipediaPage
    lateinit var endPage: WikipediaPage
    var winner: Player? = null


    fun start() : Boolean {
        if (running || players.size < 2) return false
        startTime = System.currentTimeMillis()
        running = true
        startPage = WikipediaManager.getRandomPage(language)!!
        endPage = WikipediaManager.getRandomPage(language)!!
        while (endPage == startPage) endPage = WikipediaManager.getRandomPage(language)!!
        players.forEach { player -> player.currentPage = startPage; player.pagesVisited.add(startPage) }
        return true
    }

    fun stop() : Player? {
        running = false
        stopTime = System.currentTimeMillis()
        return winner
    }

    fun movePlayerToPage(uuid: UUID, pageUri: String): WikipediaPage? {
        if (!running) return null
        try {
            val player: Player = players.stream().filter { player: Player -> player.uuid == uuid }.findFirst().get()
            val page = WikipediaManager.getPageFromURI(pageUri) ?: return null
            player.moveToPage(page)
            if (page == endPage) {
                winner = player
                stop()
                //TODO: give information about winning
            }
            return page
        } catch (e: NoSuchElementException) {
            return null
        }
    }

    fun addPlayer(player: Player) : Boolean {
        if (running) return false
        players.add(player)
        return true
    }

    fun removePlayer(player: Player) : Boolean {
        if (running) return false
        players.remove(player)
        return true
    }
}