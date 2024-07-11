package com.redmagic.place

import com.redmagic.place.data.DataManager
import com.redmagic.place.data.getTimedUser
import com.redmagic.place.messages.ActionBarLoop
import com.redmagic.place.world.WorldLoader
import com.redmagic.place.world.setFloorStage
import com.undefined.api.UndefinedAPI
import com.undefined.api.command.UndefinedCommand
import com.undefined.api.event.event
import com.undefined.api.extension.string.translateColor
import com.undefined.api.scheduler.TimeUnit
import com.undefined.api.scheduler.repeatingTask
import com.undefined.api.scheduler.sync
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.configuration.serialization.ConfigurationSerialization
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.plugin.java.JavaPlugin
import org.yaml.snakeyaml.Yaml
import java.io.File
import java.time.LocalDateTime
import kotlin.time.Duration

class PlaceMC : JavaPlugin() {

    companion object {
        lateinit var undefinedAPI: UndefinedAPI
        lateinit var placeMC: PlaceMC
    }

    lateinit var dataConfig: YamlConfiguration
    lateinit var dataManager: DataManager
    lateinit var actionBarLoop: ActionBarLoop

    var stage: Int = 1

    var startTime: LocalDateTime? = null

    private val dataFile = File(dataFolder, "data.yml")

    override fun onEnable() {
        // Plugin startup logic
        undefinedAPI = UndefinedAPI(this)
        placeMC = this

        saveResource("data.yml", false)

        dataConfig = YamlConfiguration.loadConfiguration(dataFile)

        stage = dataConfig.getInt("stage")
        dataConfig.getString("startTime")?.let {
            startTime = LocalDateTime.parse(it)
        }

        WorldLoader()
        Bukkit.getWorld("placemc")?.let {
            if (it.getBlockAt(0,64,0).type == Material.AIR) {
                it.setFloorStage(1)
                stage = 1
            }
        }

        dataManager = DataManager()
        actionBarLoop = ActionBarLoop()

        repeatingTask(20*60, true) {
            saveDataFile()
        }


        explaningCanvas()
        startCommand()
        BlockListener()
    }

    private fun explaningCanvas() {
        repeatingTask(1, TimeUnit.MINUTES, true) {
            startTime?.let {
                val now = LocalDateTime.now()
                val days = java.time.Duration.between(it, now).toDays() + 1
                if (days > stage) {
                    sync {
                        Bukkit.getWorld("placemc")?.let {
                            it.setFloorStage(days.toInt())
                            stage = days.toInt()

                            Bukkit.getOnlinePlayers().forEach {
                                it.sendTitle("<green>ᴄᴀɴᴠᴀѕ ᴇxᴘᴀɴᴅɪɴɢ...".translateColor(), "")
                            }
                        }
                    }
                }
            }
        }
    }

    private fun startCommand() {
        UndefinedCommand("start", "placemc.start.command")
            .addExecutePlayer {
                val player = player!!
                player.sendMessage("<green> Event has started".translateColor())
                startTime = LocalDateTime.now()
                return@addExecutePlayer false
            }

        UndefinedCommand("spawn", aliases = listOf("back", "s"))
            .addExecutePlayer {
                val player = player!!
                player.sendMessage("<green>ᴛᴇʟᴇᴘᴏʀᴛᴇᴅ ʙᴀᴄᴋ ᴛᴏ ѕᴘᴀᴡɴ".translateColor())
                player.teleport(Bukkit.getWorld("placemc")!!.spawnLocation)
                return@addExecutePlayer false
            }
    }

    private fun saveDataFile() {
        dataConfig.save(dataFile)
    }

    override fun onDisable() {
        // Plugin shutdown logic

        dataConfig.set("stage", stage)
        dataConfig.set("startTime", startTime)

        saveDataFile()

    }


}
