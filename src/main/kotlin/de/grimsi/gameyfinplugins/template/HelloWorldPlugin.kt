package de.grimsi.gameyfinplugins.template

import de.grimsi.gameyfin.pluginapi.core.ConfigurableGameyfinPlugin
import de.grimsi.gameyfin.pluginapi.core.PluginConfigElement
import de.grimsi.gameyfin.pluginapi.core.PluginConfigValidationResult
import de.grimsi.gameyfin.pluginapi.gamemetadata.GameMetadata
import de.grimsi.gameyfin.pluginapi.gamemetadata.GameMetadataProvider
import org.pf4j.Extension
import org.pf4j.PluginWrapper
import org.slf4j.LoggerFactory

class HelloWorldPlugin(wrapper: PluginWrapper) : ConfigurableGameyfinPlugin(wrapper) {

    /**
     * This is the configuration metadata for the plugin.
     * It defines the configuration keys, names, descriptions, and whether they are secret (which only changes how they are displayed in the UI).
     * By default, all configuration options are stored encrypted in the Gameyfin database.
     */
    override val configMetadata: List<PluginConfigElement> = listOf(
        PluginConfigElement(
            key = "exampleKey",
            name = "Example Key",
            description = "Example configuration key for HelloWorldPlugin"
        )
    )

    /**
     * This is an example of how to validate the plugin configuration.
     * You can implement this method to check if the provided configuration is valid.
     * In this example, we only check if the "exampleKey" is not empty.
     */
    override fun validateConfig(config: Map<String, String?>): PluginConfigValidationResult {
        return config["exampleKey"]?.let {
            if (it.isNotBlank()) {
                PluginConfigValidationResult.VALID
            } else {
                PluginConfigValidationResult.INVALID(mapOf("exampleKey" to "This field cannot be empty"))
            }
        } ?: PluginConfigValidationResult.INVALID(mapOf("exampleKey" to "This field is required"))
    }

    /**
     * OPTIONAL:
     * This method is called when the plugin is started.
     * You can use it to initialize resources, register listeners, etc.
     */
    override fun start() {
        log.info("HelloWorldPlugin started")
    }

    /**
     * OPTIONAL:
     * This method is called when the plugin is stopped.
     * You can use it to clean up resources, unregister listeners, etc.
     */
    override fun stop() {
        log.debug("HelloWorldPlugin stopped")
    }

    /**
     * OPTIONAL:
     * This method is called when the plugin is deleted.
     * You can use it to clean up resources, unregister listeners, etc.
     */
    override fun delete() {
        log.debug("HelloWorldPlugin deleted")
    }

    /**
     * This class implements the core functionality of the plugin.
     * It implements the GameMetadataProvider interface from the Gameyfin plugin API.
     */
    @Extension
    class HelloWorldMetadataProvider : GameMetadataProvider {

        companion object {
            private val log = LoggerFactory.getLogger(this::class.java)
        }

        /**
         * Implement this method to provide game metadata from your plugin.
         *
         * The method receives a "gameId" which is usually the title of a video game.
         * The task of your plugin is to match this gameId to one or more video games and return metadata about it.
         * The metadata should be returned as a list of GameMetadata objects with the best match first and the worst match last.
         * If you could not find any matches, return an empty list.
         *
         * The required metadata fields are:
         * - title: The title of the game.
         * - originalId: The unique identifier of the game from your data source (e.g. "slug" from IGDB, "AppID" from Steam, ...).
         *
         * The remaining fields are optional, but you should try to fill them if possible:
         * - description: A short description of the game.
         * - coverUrl: A URL to the cover image of the game.
         * - screenshotUrls: A list of URLs to screenshots of the game.
         * - videoUrls: A list of URLs to videos of the game.
         * - releaseDate: The release date of the game.
         * - userRating: The user rating of the game (0-100).
         * - criticsRating: The critics rating of the game (0-100).
         * - developedBy: The developer(s) of the game.
         * - publishedBy: The publisher(s) of the game.
         * - genres: A list of genres the game belongs to.
         * - themes: A list of themes the game belongs to.
         * - keywords: A list of keywords associated with the game.
         * - features: A list of features the game has.
         * - perspectives: A list of perspectives the game can be played from.
         *
         * @param gameId This is the name of a file or folder that Gameyfin found in a game library.
         * @param maxResults The maximum number of results to return. You can return fewer results if you want.
         */
        override fun fetchMetadata(gameId: String, maxResults: Int): List<GameMetadata> {

            // Log the gameId and maxResults for debugging purposes.
            log.debug("HelloWorldMetadataProvider: Fetching metadata for gameId: {} with maxResults: {}", gameId, maxResults)

            // For demonstration purposes, we will return a hardcoded example result.
            val exampleResult = GameMetadata(
                title = "Hello World Game",
                originalId = "hello-world-game"
            )

            // Return a list containing the example result.
            return listOf(exampleResult)
        }
    }
}