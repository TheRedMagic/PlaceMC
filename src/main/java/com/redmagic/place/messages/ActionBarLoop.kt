package com.redmagic.place.messages

import com.redmagic.place.data.getTimedUser
import com.undefined.api.extension.string.translateColor
import com.undefined.api.scheduler.repeatingTask
import org.bukkit.Bukkit
import java.util.UUID

class ActionBarLoop {

    val actionPlayers: MutableList<UUID> = mutableListOf()

    init {

        repeatingTask(20) {
            actionPlayers.forEach {
                val ofP = Bukkit.getOfflinePlayer(it)
                if (ofP.isOnline) {
                    ofP.player!!.getTimedUser()?.let {
                        ofP.player!!.sendMessage("<green>ʏᴏᴜ ʜᴀᴠᴇ ${it.secondsLeft()} ѕᴇᴄᴏɴᴅѕ ʟᴇꜰᴛ ɪɴᴛɪʟʟ ʏᴏᴜ ᴄᴀɴ ᴘʟᴀᴄᴇ ᴀ ʙʟᴏᴄᴋ".translateColor())
                    }
                } else {
                    actionPlayers.remove(it)
                }
            }
        }

    }
}