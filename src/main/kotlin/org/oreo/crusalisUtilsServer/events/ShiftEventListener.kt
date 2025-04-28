package org.oreo.crusalisUtilsServer.events


import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.FluidCollisionMode
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerToggleSneakEvent
import org.bukkit.plugin.Plugin
import org.oreo.crusalisUtilsServer.CrusalisUtilsServer


class ShiftEventListener(val plugin: Plugin) : Listener {

    @EventHandler
    fun onPlayerShift(e: PlayerToggleSneakEvent) {


        if (!e.isSneaking) return

        val player = e.player
        val target = raycastForPlayer(player)

        Bukkit.getScheduler().runTaskAsynchronously(plugin, Runnable {

            player.sendMessage("You are looking at ${target?.name}")

            if (target == null) return@Runnable

            val playerNation = CrusalisUtilsServer.nodesInstance!!.getResident(player)?.nation ?: return@Runnable
            val residentNation = CrusalisUtilsServer.nodesInstance!!.getResident(target)?.nation ?: return@Runnable

            val teamColour : ChatColor = if (playerNation === residentNation){
                ChatColor.GREEN
            } else if (playerNation.allies.contains(residentNation)){
                ChatColor.AQUA
            } else if (playerNation.enemies.contains(residentNation)){
                ChatColor.RED
            } else {
                ChatColor.GOLD
            }

            CrusalisUtilsServer.glowingEntitiesInstance!!.setGlowing(target,player,teamColour)
        })

    }

    private fun raycastForPlayer(player: Player): Player? {

        val world = player.world
        val direction = player.eyeLocation.direction.normalize()

        // Reduce max distance and increase step size
        val maxDistance = player.server.simulationDistance * 16.0

        // Turns out paper has its own raytrace method so im using that
        val result = world.rayTrace(
            player.eyeLocation,
            direction,
            maxDistance,
            FluidCollisionMode.NEVER,
            true,
            0.5
        ) { entity -> entity is Player && entity != player && !entity.isDead }

        return result?.hitEntity as? Player
    }
}