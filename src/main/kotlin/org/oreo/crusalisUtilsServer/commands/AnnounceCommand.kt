package org.oreo.crusalisUtilsServer.commands

import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.ChatColor
import org.bukkit.Sound
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.oreo.crusalisUtilsServer.CrusalisUtilsServer
import java.util.*

class AnnounceCommand : CommandExecutor {

    override fun onCommand(commandSender: CommandSender, command: Command, s: String, args: Array<String>): Boolean {
        if (commandSender !is Player) {
            commandSender.sendMessage(ChatColor.RED.toString() + "You can't use this command in the console!")
            return false
        }

        val sender = commandSender
        val resident = CrusalisUtilsServer.nodesInstance!!.getResident(sender)

        if (resident?.nation == null) {
            sender.sendMessage(ChatColor.RED.toString() + "You are not part of any nation!")
            return false
        }

        val nation = resident.nation!!

        // Check if the sender is the nation leader
        if (nation.capital.leader!!.name != resident.name) {
            sender.sendMessage(ChatColor.RED.toString() + "You aren't a nation leader.")
            return false
        }

        var message =  ""

        for (word in args) {
            message += "$word "
        }

        for ( player in nation.playersOnline ) {

            player.playSound(player.location, Sound.ENTITY_ENDERMAN_TELEPORT, 2.0f, 0.5f)
            sendActionBar(player, message)
        }

        return true
    }

    // Method to send a message above the player's hotbar
    private fun sendActionBar(player: Player, message: String) {
        val formattedMessage = ChatColor.AQUA.toString() + "" + ChatColor.BOLD + message.uppercase(Locale.getDefault())
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent(formattedMessage))
    }
}
