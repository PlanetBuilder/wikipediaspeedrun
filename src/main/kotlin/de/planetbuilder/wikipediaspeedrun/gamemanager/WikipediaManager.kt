package de.planetbuilder.wikipediaspeedrun.gamemanager

import org.http4k.client.ApacheClient
import org.http4k.core.Method
import org.http4k.core.Request

class WikipediaManager {

    companion object {
        val client = ApacheClient()
        fun getRandomPage(lang: String): WikipediaPage? {
            println(client(Request(Method.GET, client(Request(Method.GET, client(Request(Method.GET, "https://$lang.wikipedia.org/wiki/Special:Random")).headers[5].second!!)).headers[5].second!!)).body)
//            val page = client(Request(Method.GET, client(Request(Method.GET, "https://$lang.wikipedia.org/wiki/Special:Random"))))
            return null
        }

        fun getPageFromURI(uri: String) : WikipediaPage? {
            val page = client(Request(Method.GET, uri))
            println(page)
            println("hello")
            return WikipediaPage("", "")
//            return getWikipediaPage(page)

        }

        fun getWikipediaPage(page: String) : WikipediaPage? {
            return WikipediaPage("", "")
        }
    }
}