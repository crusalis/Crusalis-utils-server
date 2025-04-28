package org.oreo.crusalisUtilsServer

import fr.skytasul.glowingentities.GlowingBlocks
import fr.skytasul.glowingentities.GlowingEntities
import org.bukkit.Bukkit
import org.bukkit.plugin.ServicePriority
import org.bukkit.plugin.java.JavaPlugin
import org.oreo.crusalisUtilsServer.commands.AnnounceCommand
import org.oreo.crusalisUtilsServer.commands.CrusalisUtilsCommands
import org.oreo.crusalisUtilsServer.commands.IncomeSummary
import org.oreo.crusalisUtilsServer.events.ItemClickingListener
import org.oreo.crusalisUtilsServer.events.ItemHoldingListener
import org.oreo.crusalisUtilsServer.events.ShiftEventListener
import org.oreo.crusalisUtilsServer.item.ItemManager
import phonon.nodes.Nodes


class CrusalisUtilsServer : JavaPlugin() {

    override fun onEnable() {

        logger.info("Crusalis utils enabled")
        logger.info("Millions must download crusalis utils")

        // Register the singleton with the server's ServicesManager
        Bukkit.getServicesManager().register(Nodes::class.java, Nodes, this, ServicePriority.Normal)

        // Retrieve the shared instance
        nodesInstance = Bukkit.getServicesManager().load(Nodes::class.java)
        if (nodesInstance == null) {
            // Handle error: service not registered
            logger.severe("Nodes not detected!")
        }

        getCommand("nation-announcements")!!.setExecutor(AnnounceCommand())
        getCommand("income")!!.setExecutor(IncomeSummary())
        getCommand("cu")!!.setExecutor(CrusalisUtilsCommands())

        ItemManager.init( this )

        enableListeners()

        saveDefaultConfig()

        glowingEntitiesInstance = GlowingEntities(this)
        glowingBlocksInstance = GlowingBlocks(this)
        glowTimePlayers = config.getInt("glow_time_players")
        glowTimeBlocks = config.getInt("glow_time_blocks")
    }

    private fun enableListeners(){
        server.pluginManager.registerEvents(ItemHoldingListener(this), this)
        server.pluginManager.registerEvents(ItemClickingListener(this), this)
        server.pluginManager.registerEvents(ShiftEventListener(this), this)
    }


    override fun onDisable() {
        logger.info("Crusalis utils disabled")
        logger.info("Oreo's code is terrible anyway")
    }


    companion object {
        var nodesInstance : Nodes? = null

        var glowingEntitiesInstance: GlowingEntities? = null
        var glowingBlocksInstance: GlowingBlocks? = null

        var glowTimePlayers : Int = 0
        var glowTimeBlocks : Int = 0
    }
}
