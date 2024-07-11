package com.redmagic.place

import com.redmagic.place.data.getTimedUser
import com.undefined.api.event.event
import com.undefined.api.extension.string.translateColor
import org.bukkit.GameMode
import org.bukkit.event.block.*
import org.bukkit.event.entity.EntityPickupItemEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerJoinEvent
import java.time.LocalDateTime

class BlockListener {

    init {

        event<BlockPlaceEvent> {
            isCancelled = true

            player.getTimedUser()?.let {
                if (it.secondsLeft() == 0 || player.hasPermission("bypass")) {
                    this.player.world.getBlockAt(
                        this.block.x,
                        64,
                        this.block.z
                    ).type = block.type

                    it.startTime = LocalDateTime.now()
                    it.amount++

                    PlaceMC.placeMC.actionBarLoop.actionPlayers.remove(this.player.uniqueId)
                    PlaceMC.placeMC.actionBarLoop.actionPlayers.add(this.player.uniqueId)
                }
            }

        }

        event<BlockBreakEvent> {
            isCancelled = true
        }

        event<InventoryClickEvent> {
            currentItem?.let {
                if (!it.type.isBlock) {
                    isCancelled = true
                    this.clickedInventory?.remove(it)
                    this.whoClicked.closeInventory()
                    this.whoClicked.sendMessage("<red>ᴛʜᴀᴛ ɪѕ ɴᴏᴛ ᴀ ѕᴏʟɪᴅ ʙʟᴏᴄᴋ".translateColor())
                    return@event
                }
            }
        }

        event<PlayerJoinEvent> {
            player.gameMode = GameMode.CREATIVE
        }
        event<PlayerDropItemEvent> { isCancelled = true }
        event<EntityPickupItemEvent> { isCancelled = true }

        event<BlockPistonExtendEvent> { isCancelled = true }
        event<BlockDispenseEvent> { isCancelled = true }

        event<PlayerJoinEvent> {


            val un = "<gray>ᴄʀᴇᴀᴛᴇᴅ ʙʏ : <#3495eb>ᴜɴᴅᴇꜰɪɴᴇᴅ ᴄʀᴇᴀᴛɪᴏɴ".translateColor()
            val fl = "<gray>ѕᴘᴏɴѕᴏʀᴇᴅ ʙʏ : <#DC7BDA>ꜰʟʏᴛᴇ".translateColor()
            val space = "                                                              "

            player.setPlayerListHeaderFooter(
                "<gold>ʟᴇᴀʀɴ ѕᴘɪɢᴏᴛ <red>ᴘʟᴀᴄᴇ\n".translateColor(),
                "$space\n$un\n$fl"
            )

        }
    }

}