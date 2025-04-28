package org.oreo.crusalisUtilsServer.events


import org.bukkit.*
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.oreo.crusalisUtilsServer.CrusalisUtilsServer
import org.oreo.crusalisUtilsServer.item.ItemManager
import org.oreo.crusalisUtilsServer.utils.GeneralUtils


class ItemClickingListener(private val plugin: CrusalisUtilsServer) : Listener {

    @EventHandler
    fun itemHeld(e: PlayerInteractEvent) {

        if (!ItemManager.isHoldingCustomItem(e.player,ItemManager.teleComunicationSctick)) {
            return
        }

        e.isCancelled = true

        if (e.player.hasCooldown(Material.STICK)) return

        e.player.setCooldown(Material.STICK,10 * 20) //10 sec cooldown
        e.player.playSound(e.player.location, Sound.BLOCK_NOTE_BLOCK_PLING, 0.5F, 2.0F)
        val serverRender = Bukkit.getServer().viewDistance * 16.0
        val raytraceBlock = e.player.rayTraceBlocks(serverRender)?.hitBlock ?: return

        val nearbyPlayers: List<Player> = GeneralUtils.getNearbyPlayers(e.player) + e.player

        Bukkit.getScheduler().runTaskAsynchronously(plugin, Runnable {

            val pingColour = if (e.player.isSneaking){
                ChatColor.RED
            } else if (e.action == Action.RIGHT_CLICK_AIR || e.action == Action.RIGHT_CLICK_BLOCK) {
                ChatColor.YELLOW
            } else {
                ChatColor.WHITE
            }
            
            val nation = CrusalisUtilsServer.nodesInstance!!.getResident(e.player)?.nation

            for (target in nearbyPlayers) {

                if ( CrusalisUtilsServer.nodesInstance!!.getResident(target)?.nation !== nation ) continue

                
                Bukkit.getScheduler().runTask(plugin, Runnable {
                    target.playSound(target.location, Sound.BLOCK_NOTE_BLOCK_PLING, 0.5F, 2.0F)
                })


                CrusalisUtilsServer.glowingBlocksInstance!!.setGlowing(raytraceBlock, target,pingColour)

                val delayInTicks = 20L * CrusalisUtilsServer.glowTimeBlocks // Replace 5 with the number of seconds you want
                Bukkit.getScheduler().runTaskLater(plugin, Runnable {
                    CrusalisUtilsServer.glowingBlocksInstance!!.unsetGlowing(raytraceBlock, target)
                }, delayInTicks)
            }
        })
    }
}