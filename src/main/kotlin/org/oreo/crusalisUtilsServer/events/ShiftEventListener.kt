package org.oreo.crusalisUtilsServer.events


import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerToggleSneakEvent
import org.bukkit.plugin.Plugin
import org.oreo.crusalisUtilsServer.CrusalisUtilsServer
import org.oreo.crusalisUtilsServer.utils.GeneralUtils


class ShiftEventListener(val plugin: Plugin) : Listener {

    @EventHandler
    fun onPlayerShift(e: PlayerToggleSneakEvent) {

        if (!e.isSneaking) return

        val player = e.player
        val targets : ArrayList<Player> = arrayListOf()

        for ( nearbyPlayer in GeneralUtils.getNearbyPlayers(player) ) {

            if (!player.hasLineOfSight(nearbyPlayer)) continue

            targets.add(nearbyPlayer)
        }

        if (targets.isEmpty()) return

        Bukkit.getScheduler().runTaskAsynchronously(plugin, Runnable {
            for (target in targets) {

                val playerResident = CrusalisUtilsServer.nodesInstance!!.getResident(player) ?: return@Runnable
                val targetResident = CrusalisUtilsServer.nodesInstance!!.getResident(target) ?: return@Runnable

                val playerNation = playerResident.nation ?: return@Runnable
                val targetNation = targetResident.nation ?: return@Runnable

                val teamColour: ChatColor = if (playerNation === targetNation) {

                    if (playerResident.town === targetResident.town) {
                        ChatColor.GREEN
                    }

                    ChatColor.DARK_GREEN
                } else if (playerNation.allies.contains(targetNation)) {
                    ChatColor.DARK_AQUA
                } else if (playerNation.enemies.contains(targetNation)) {
                    ChatColor.RED
                } else {
                    ChatColor.GOLD
                }

                CrusalisUtilsServer.glowingEntitiesInstance!!.setGlowing(target, player, teamColour)

                // Glow removal logic
                val delayInTicks = 20L * CrusalisUtilsServer.glowTimePlayers
                Bukkit.getScheduler().runTaskLater(plugin, Runnable {
                    CrusalisUtilsServer.glowingEntitiesInstance!!.unsetGlowing(target, player)
                }, delayInTicks)
            }
        })
    }

//    private fun raycastForPlayer(player: Player): Player? {
//
//        val world = player.world
//        val direction = player.eyeLocation.direction.normalize()
//
//        // Reduce max distance and increase step size
//        val maxDistance = player.server.simulationDistance * 16.0
//
//        // Turns out paper has its own raytrace method so im using that
//        val result = world.rayTrace(
//            player.eyeLocation,
//            direction,
//            maxDistance,
//            FluidCollisionMode.NEVER,
//            true,
//            0.5
//        ) { entity -> entity is Player && entity != player && !entity.isDead }
//
//        return result?.hitEntity as? Player
//    }
}