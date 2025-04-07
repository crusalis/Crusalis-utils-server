package org.oreo.crusalisUtilsServer.events

import org.bukkit.Color
import org.bukkit.Particle
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerItemHeldEvent
import org.bukkit.scheduler.BukkitRunnable
import org.oreo.crusalisUtilsServer.CrusalisUtilsServer
import org.oreo.crusalisUtilsServer.item.ItemManager
import org.oreo.crusalisUtilsServer.utils.GeneralUtils.getNearbyPlayers
import phonon.nodes.Nodes
import phonon.nodes.objects.Nation


class ItemHoldingListener(private val plugin: CrusalisUtilsServer) : Listener {

    @EventHandler
    fun itemHeld(e: PlayerItemHeldEvent) {


        val heldItem = e.player.inventory.getItem(e.newSlot) ?: return

        if (!ItemManager.isCustomItem(heldItem,ItemManager.playerTeamInfo)) {
            return
        }

        val nation = CrusalisUtilsServer.nodesInstance!!.getResident(e.player)?.nation

        object : BukkitRunnable() {
            override fun run() {

                spawnGreenRedstoneParticles(e.player,getNearbyPlayers(e.player), nation)

                if (!ItemManager.isCustomItem(e.player.inventory.itemInMainHand,ItemManager.playerTeamInfo)) {
                    cancel() // Cancels this task.
                }
            }
        }.runTaskTimer(plugin, 0L, 1L)
    }

    fun spawnGreenRedstoneParticles(player : Player,players: List<Player>,nation: Nation?) {
        // Define the dust options with a green color and a size (1.0f)

        players.forEach { player0 ->

            val otherPlayerNation = Nodes.getResident(player0)?.nation

            val dustColour = if (nation === null){
                Color.ORANGE
            } else if(otherPlayerNation === nation) {
                Color.GREEN
            } else if (nation.allies.contains(otherPlayerNation)) {
                Color.AQUA
            } else if (nation.enemies.contains(otherPlayerNation)) {
                Color.RED
            } else {
                Color.ORANGE
            }

            val particleLocation = player0.location.clone().add(0.0, player.height - 1.0, 0.0)

            val dustOptions = Particle.DustOptions(dustColour, 5.0f)

            val offsetRange = 0.1
            val offsetX = (Math.random() - 0.5) * offsetRange
            val offsetY = (Math.random() - 0.5) * offsetRange
            val offsetZ = (Math.random() - 0.5) * offsetRange

            player.spawnParticle(
                Particle.DUST,
                particleLocation,
                2,
                offsetX, offsetY, offsetZ,
                0.0,
                dustOptions,
                true
            )
        }
    }
}