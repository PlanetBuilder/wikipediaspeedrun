package de.planetbuilder.wikipediaspeedrun.gamemanager

import org.http4k.core.Method
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.routing.routes
import org.http4k.routing.bind
import org.http4k.routing.path
import org.http4k.server.Jetty
import org.http4k.server.asServer
import java.util.UUID

class GamesManager {
    val games = ArrayList<Game>()

    val app = routes(
        "/create" bind Method.GET to {request -> createGame(request) },
        "/{uuid}/start" bind Method.GET to {request -> startGame(request) },
        "/{uuid}/stop" bind Method.GET to {request -> stopGame(request) },
        "/{uuid}/join" bind Method.GET to {request -> joinGame(request) },
        "/{uuid}/klick" bind Method.GET to {request -> movePlayer(request) },
    )

    fun start(port: Int) {
        app.asServer(Jetty(port)).start()
    }

    private fun createGame(request: Request) : Response {
        return try {
            val language = request.query("language") ?: throw Exception("language not specified")
            if (!WikipediaLanguage.languages.contains(language)) throw Exception("language not valid")
            val settings: GameSettings = GameSettings(language)
            val game = Game(settings)
            val gameUUID = game.uuid

            Response(Status.OK).body("{success:false,game_uuid:\"$gameUUID\"")

        } catch (e: Exception) {
            Response(Status.BAD_REQUEST).body("{success:false,reason:\"${e.message.toString()}\"")
        }
    }
    private fun startGame(request: Request) : Response {
        return try {
            val uuid = request.path("uuid") ?: throw Exception("This should not happen!")
            val game = getGame(UUID.fromString(uuid)) ?: throw Exception("Game does not exist")
            val success = game.start()
            if (!success) throw Exception("Game start not successful")

            Response(Status.OK).body("{success:true, start_page:\"${game.startPage.content}\"")
        } catch (e: Exception) {
            Response(Status.BAD_REQUEST).body("{success:false,reason:\"${e.message.toString()}\"")
        }
    }
    private fun stopGame(request: Request) : Response {
        return try {
            val uuid = request.path("uuid") ?: throw Exception("This should not happen!")
            val game = getGame(UUID.fromString(uuid)) ?: throw Exception("Game does not exist")
            game.stop()
            Response(Status.OK).body("{success:true}")
        } catch (e: Exception) {
            Response(Status.BAD_REQUEST).body("{success:false,reason:\"${e.message.toString()}\"")
        }
    }
    private fun joinGame(request: Request) : Response {
        return try {
            val uuid = request.path("uuid") ?: throw Exception("This should not happen!")
            val game = getGame(UUID.fromString(uuid)) ?: throw Exception("Game does not exist")
            val username = request.query("username") ?: throw Exception("Username not specified")
            val player = Player(username)
            game.addPlayer(player)

            Response(Status.OK).body("{success:true, player_uuid:\"${player.uuid}\"}")
        } catch (e: Exception) {
            Response(Status.BAD_REQUEST).body("{success:false,reason:\"${e.message.toString()}\"")
        }
    }
    fun movePlayer(request: Request) : Response {
        return try {
            val uuid = request.path("uuid") ?: throw Exception("This should not happen!")
            val game = getGame(UUID.fromString(uuid)) ?: throw Exception("Game does not exist")
            val playeruuid = request.query("playeruuid") ?: throw Exception("Player uuid not specified")
            val pageuri = request.query("pageuri") ?: throw Exception("Page Uri not specified")
            val newPage = game.movePlayerToPage(UUID.fromString(playeruuid), pageuri) ?: throw Exception("Moving not successful")
            Response(Status.OK).body("{success:true, new_page:\"${newPage.content}\"")
        } catch (e: Exception) {
            Response(Status.BAD_REQUEST).body("{success:false,reason:\"${e.message.toString()}\"")
        }
    }


    fun newGame(gameSettings: GameSettings) = games.add(Game(gameSettings))
    fun removeGame(uuid: UUID) = games.removeIf { game -> game.uuid == uuid}
    fun getGame(uuid: UUID) = games.find { game: Game -> game.uuid == uuid }
}