package com.redmagic.place.world

import com.redmagic.place.PlaceMC
import org.bukkit.*
import java.io.File

class WorldLoader {

    init {

        val worldCreator = WorldCreator("placemc")

        worldCreator.generateStructures(false)
        worldCreator.keepSpawnInMemory(true)
        worldCreator.generator(EmptyChunkGenerator())



        worldCreator.createWorld()?.let {

            it.spawnLocation = Location(it, 0.0, 65.0, 0.0)
        }

    }
}

fun World.setFloorStage(stage: Int) {
    val size = 45 + stage*10

    for (x in -size..size) {
        for (z in -size..size) {
            val b = getBlockAt(x, 64, z)
            if (b.type == Material.AIR) {
                b.type = Material.WHITE_CONCRETE
            }
        }
    }
}