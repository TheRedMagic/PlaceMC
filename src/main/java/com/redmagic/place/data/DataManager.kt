package com.redmagic.place.data

import com.redmagic.place.PlaceMC
import com.undefined.api.event.event
import com.undefined.api.scheduler.async
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import java.sql.Time
import java.time.LocalDateTime
import java.util.UUID

class DataManager {

    val cacheMap: HashMap<UUID, TimedUser> = hashMapOf()

    init {

        val world = Bukkit.getWorld("placemc")

        event<PlayerJoinEvent> {

            player.teleport(world!!.spawnLocation)

            if (!cacheMap.containsKey(player.uniqueId)) {
                val config = PlaceMC.placeMC.dataConfig

                if (config.contains(player.uniqueId.toString())) {

                    val time = config.getString("${player.uniqueId}.time")

                    if (time == null) {
                        cacheMap[player.uniqueId] = TimedUser(player.uniqueId, null)
                        return@event
                    }

                    val startTime: LocalDateTime = LocalDateTime.parse(time)
                    val timeU = TimedUser(player.uniqueId, startTime)
                    cacheMap[player.uniqueId] = timeU
                    if (timeU.secondsLeft() != 0) {
                        PlaceMC.placeMC.actionBarLoop.actionPlayers.add(player.uniqueId)
                    } else {
                        timeU.startTime = null
                    }


                } else {
                    val timedUser = TimedUser(player.uniqueId, null)
                    cacheMap[player.uniqueId] = timedUser

                    config.set("${player.uniqueId}.time", null)

                }
            }
        }

        event<PlayerQuitEvent> {

            async {
                PlaceMC.placeMC.dataConfig.set("${player.uniqueId}.time", cacheMap[player.uniqueId]!!.startTime?.toString())
                cacheMap.remove(player.uniqueId)
            }

        }
    }

}

fun Player.getTimedUser(): TimedUser? = PlaceMC.placeMC.dataManager.cacheMap[uniqueId]