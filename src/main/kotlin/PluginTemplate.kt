package org.gameyfin.plugins

import org.gameyfin.pluginapi.core.config.ConfigMetadata
import org.gameyfin.pluginapi.core.config.PluginConfigMetadata
import org.gameyfin.pluginapi.core.config.PluginConfigValidationResult
import org.gameyfin.pluginapi.core.wrapper.ConfigurableGameyfinPlugin
import org.gameyfin.pluginapi.gamemetadata.GameMetadata
import org.gameyfin.pluginapi.gamemetadata.GameMetadataProvider
import org.pf4j.Extension
import org.pf4j.PluginWrapper
import org.slf4j.LoggerFactory

enum class ExampleEnum {
    OPTION_ONE,
    OPTION_TWO,
    OPTION_THREE
}

class PluginTemplate(wrapper: PluginWrapper) : ConfigurableGameyfinPlugin(wrapper) {

    /**
     * This is the configuration metadata for the plugin.
     * All configuration values set by the admin(s) are stored encrypted in the Gameyfin database.
     */
    override val configMetadata: PluginConfigMetadata = listOf(
        ConfigMetadata(
            key = "exampleConfigProperty",
            type = String::class.java,
            label = "Example Configuration Key",
            description = "This is an example configuration key for a Gameyfin plugin.",
        ),
        ConfigMetadata(
            key = "secretExampleConfigProperty",
            type = String::class.java,
            label = "Secret Example Configuration Key",
            description = "This is a secret configuration key for a Gameyfin plugin. It will be displayed as a password field in the UI.",
            isSecret = true
        ),
        ConfigMetadata(
            key = "optionalExampleConfigProperty",
            type = String::class.java,
            label = "Optional Example Configuration Key",
            description = "This is an optional configuration key for a Gameyfin plugin. It is not required to be set.",
            isRequired = false
        ),
        ConfigMetadata(
            key = "exampleConfigPropertyWithDefault",
            type = String::class.java,
            label = "Example Configuration Key with Default Value",
            description = "This is an example configuration key with a default value.",
            default = "default"
        ),
        ConfigMetadata(
            key = "exampleEnumConfigProperty",
            type = ExampleEnum::class.java,
            label = "Example Enum Configuration Key",
            description = "This is an example configuration key with an enum value and a default value. This will be displayed as a dropdown in the UI.",
            default = ExampleEnum.OPTION_ONE
        )
    )

    override fun validateConfig(config: Map<String, String?>): PluginConfigValidationResult {
        // Use the built-in validation first (optional)
        val validationResult = super.validateConfig(config)
        if (!validationResult.isValid()) {
            // Return early if the built-in validation failed (also optional)
            return validationResult
        }

        // Custom validation logic

        // Create a mutable map to hold validation errors (or use the one from the previous validation)
        val errors = mutableMapOf<String, String>()

        val exampleConfigValue = config["exampleConfigProperty"]
        if (exampleConfigValue != "helloworld") {
            errors["exampleConfigProperty"] = "Value must be 'helloworld'"
        }

        val exampleEnumConfigProperty = config["exampleEnumConfigProperty"]
        if(exampleEnumConfigProperty == null) {
            errors["exampleEnumConfigProperty"] = "This field is required"
        } else if(ExampleEnum.valueOf(exampleEnumConfigProperty) == ExampleEnum.OPTION_THREE) {
            errors["exampleEnumConfigProperty"] = "Option THREE is deprecated"
        }

        val secretExampleConfigValue = config["secretExampleConfigProperty"]
        if(secretExampleConfigValue == null) {
            errors["secretExampleConfigProperty"] = "This field is required"
        } else if (secretExampleConfigValue.length < 5) {
            errors["secretExampleConfigProperty"] = "Must be at least 5 characters long"
        }

        // If there are validation errors, return an invalid result with the errors
        // Otherwise, return a valid result
        return if(errors.isNotEmpty()) {
            PluginConfigValidationResult.INVALID(errors)
        } else {
            PluginConfigValidationResult.VALID
        }
    }

    @Extension
    class TutorialMetadataProvider : GameMetadataProvider {

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
         * @param gameTitle This is the name of a file or folder that Gameyfin found in a game library.
         * @param maxResults The maximum number of results to return. You can return fewer results if you want.
         * @return A list of GameMetadata objects containing the metadata of the game(s) that match the gameId.
         */
        override fun fetchByTitle(gameTitle: String, maxResults: Int): List<GameMetadata> {

            // Log the gameId and maxResults for debugging purposes.
            log.debug("HelloWorldMetadataProvider: Fetching metadata for gameId: {} with maxResults: {}", gameTitle, maxResults)

            // For demonstration purposes, we will return a hardcoded example result.
            val exampleResult = GameMetadata(
                title = "Hello World Game",
                originalId = "hello-world-game"
            )

            // Return a list containing the example result.
            return listOf(exampleResult)
        }

        /**
         * Implement this method to provide game metadata by its unique identifier.
         *
         * The method receives an "id" which is usually the unique identifier of a video game from your data source.
         * The task of your plugin is to match this id to a video game and return metadata about it.
         * If you could not find any matches, return null.
         *
         * @param id The unique identifier of the game in the source your plugin implements.
         * @return A GameMetadata object containing the metadata of the game, or null if no match was found.
         */
        override fun fetchById(id: String): GameMetadata? {
            return GameMetadata(
                title = "Hello World Game",
                originalId = id
            )
        }
    }
}