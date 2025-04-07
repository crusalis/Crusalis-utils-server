package org.oreo.crusalisUtilsServer.utils

import org.bukkit.entity.Player

object GeneralUtils {
    fun getNearbyPlayers(player: Player): List<Player> {
        return player.trackedBy.toList()
    }
}