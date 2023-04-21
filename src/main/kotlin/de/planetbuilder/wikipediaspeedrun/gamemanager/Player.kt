package de.planetbuilder.wikipediaspeedrun.gamemanager

import java.util.UUID

class Player(
    val username: String
) {
    val uuid: UUID = UUID.randomUUID()
    var timesClicked: Int = 0
    lateinit var currentPage: WikipediaPage
    val pagesVisited = ArrayList<WikipediaPage>()


    fun moveToPage(page: WikipediaPage) {
        currentPage = page
        pagesVisited.add(page)
        timesClicked++
    }
}