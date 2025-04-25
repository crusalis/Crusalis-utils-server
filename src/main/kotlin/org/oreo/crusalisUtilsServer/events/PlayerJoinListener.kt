package org.oreo.crusalisUtilsServer.events

import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.plugin.Plugin
import org.oreo.crusalisUtilsServer.CrusalisUtilsServer


class PlayerJoinListener(val plugin: Plugin) : Listener {

    @EventHandler
    fun onPlayerJoin(e: PlayerJoinEvent) {
        val player: Player = e.getPlayer()

        if (plugin.config.getBoolean("positionPingsDisabled")) {
            sendBooleanMessage(player,"positionPingsDisabled")
        }

        if (plugin.config.getBoolean("locationSharingDisabled")) {
            sendBooleanMessage(player,"locationSharingDisabled")
        }

        if (plugin.config.getBoolean("hitboxColoursDisabled")) {
            sendBooleanMessage(player,"hitboxColoursDisabled")
        }

        if (plugin.config.getBoolean("flagToWaypointDisabled")) {
            sendBooleanMessage(player,"flagToWaypointDisabled")

        }
    }

    private fun sendBooleanMessage(player: Player,value: String) {
        player.sendMessage(CrusalisUtilsServer.DISABLE_STRING + value)
    }
}