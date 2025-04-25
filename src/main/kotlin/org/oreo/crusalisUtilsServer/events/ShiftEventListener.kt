package org.oreo.crusalisUtilsServer.events

import com.comphenix.protocol.PacketType
import com.comphenix.protocol.ProtocolLibrary
import com.comphenix.protocol.events.PacketContainer
import com.comphenix.protocol.wrappers.WrappedDataWatcher
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerToggleSneakEvent
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import org.bukkit.scoreboard.Team
import java.lang.reflect.InvocationTargetException
import kotlin.experimental.or


class ShiftEventListener : Listener {
    
    @EventHandler
    fun onPlayerShift(e: PlayerToggleSneakEvent) {

        if (!e.isSneaking) return

        val player = e.player
        val target = raycastForPlayer(player)

        player.sendMessage("You are looking at " + target?.name)

        if (target == null) return

        makePlayerGlowTo(target, player, ChatColor.GOLD)
    }

    private fun raycastForPlayer(player: Player): Player? {
        val maxDistance = player.server.simulationDistance * 16.0 // Convert chunks to blocks
        val direction = player.location.direction.normalize()
        val startLocation = player.eyeLocation

        val currentLocation = startLocation.clone()
        val increment = 0.5 // Check every half block

        while (currentLocation.distance(startLocation) <= maxDistance) {
            val playerAtLocation = getPlayerAtLocation(currentLocation, player)
            if (playerAtLocation != null) {
                return playerAtLocation
            }
            currentLocation.add(direction.clone().multiply(increment))
        }

        return null
    }

private fun getPlayerAtLocation(location: org.bukkit.Location, excludePlayer: Player): Player? {
    val nearbyEntities = location.world.getNearbyEntities(location, 1.0, 1.0, 1.0) // Query a bounding box of 1 block
    return nearbyEntities
        .filterIsInstance<Player>() // Filter only players
        .firstOrNull { it != excludePlayer && !it.isDead }
}


    fun makePlayerGlowTo(target: Player, viewer: Player, color: ChatColor) {
        // Add the target to a scoreboard team
        val scoreboard = Bukkit.getScoreboardManager().getMainScoreboard()
        var team = scoreboard.getTeam("glow_" + viewer.getName())
        if (team == null) {
            team = scoreboard.registerNewTeam("glow_" + viewer.getName())
        }

        team.setColor(color)
        team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER)
        team.addEntry(target.getName())

        // Make the viewer see the glowing
        target.addPotionEffect(PotionEffect(PotionEffectType.GLOWING, Int.Companion.MAX_VALUE, 1, false, false))

        // Send packets so only the viewer sees the glow
        sendGlowPacket(target, viewer, true)
    }

   fun sendGlowPacket(glowingPlayer: Player, viewer: Player?, glowing: Boolean) {
       val packet: PacketContainer =
           ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.ENTITY_METADATA)
       packet.getIntegers().write(0, glowingPlayer.entityId)

       val watcher = WrappedDataWatcher()
       val byteSerializer = WrappedDataWatcher.Registry.get(Byte::class.javaPrimitiveType)

       var flags: Byte = 0
       if (glowing) {
           flags = flags or 0x40 // Bit 6 enables glowing
       }

       watcher.setEntity(glowingPlayer)
       watcher.setObject(0, byteSerializer, flags)

       packet.getWatchableCollectionModifier().write(0, watcher.watchableObjects)

       try {
           ProtocolLibrary.getProtocolManager().sendServerPacket(viewer, packet)
       } catch (e: InvocationTargetException) {
           e.printStackTrace()
       }
   }

}