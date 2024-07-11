package com.redmagic.place.world

import org.bukkit.Location
import org.bukkit.World
import org.bukkit.block.Biome
import org.bukkit.generator.BiomeProvider
import org.bukkit.generator.ChunkGenerator
import org.bukkit.generator.WorldInfo
import java.util.*

class EmptyChunkGenerator: ChunkGenerator() {

    override fun shouldGenerateNoise(): Boolean = false

    override fun shouldGenerateNoise(worldInfo: WorldInfo, random: Random, chunkX: Int, chunkZ: Int): Boolean = false

    override fun shouldGenerateSurface(): Boolean = false

    override fun shouldGenerateCaves(): Boolean = false

    override fun shouldGenerateDecorations(): Boolean = false

    override fun shouldGenerateMobs(): Boolean = false

    override fun shouldGenerateStructures(): Boolean = false

    override fun getDefaultBiomeProvider(worldInfo: WorldInfo): BiomeProvider {
        return object : BiomeProvider() {


            override fun getBiome(p0: WorldInfo, p1: Int, p2: Int, p3: Int): Biome = Biome.PLAINS

            override fun getBiomes(p0: WorldInfo): MutableList<Biome> = mutableListOf(Biome.PLAINS)
        }
    }

    override fun getFixedSpawnLocation(world: World, random: Random): Location {
        return Location(world, 0.0, 65.0, 0.0)
    }
}