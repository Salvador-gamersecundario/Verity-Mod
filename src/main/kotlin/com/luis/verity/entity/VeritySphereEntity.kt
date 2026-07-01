package com.luis.verity.entity

import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.SpawnGroup
import net.minecraft.nbt.NbtCompound
import net.minecraft.network.listener.ClientPlayPacketListener
import net.minecraft.network.packet.Packet
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket
import net.minecraft.world.World

class VeritySphereEntity(
    entityType: EntityType<out VeritySphereEntity>,
    world: World
) : Entity(entityType, world) {

    // Constructor para spawnear en posición específica
    constructor(world: World, x: Double, y: Double, z: Double) : this(ENTITY_TYPE, world) {
        setPosition(x, y, z)
    }

    init {
        // La esfera no tiene hitbox de colisión
        // pero necesita una para que el servidor la trackee
        setNoGravity(true)
        noClip = true
    }

    override fun initDataTracker() {
        // No necesitamos data tracker para este caso
    }

    override fun readCustomDataFromNbt(nbt: NbtCompound) {
        // Persistir posición si es necesario
    }

    override fun writeCustomDataToNbt(nbt: NbtCompound) {
        // Guardar posición si es necesario
    }

    override fun createSpawnPacket(): Packet<ClientPlayPacketListener> {
        return EntitySpawnS2CPacket(this)
    }

    override fun tick() {
        super.tick()
        // La esfera no se mueve, no tiene física
        // Pero podrías animarla aquí (rotación, pulso, etc.)
    }

    // No colisiona con nada
    override fun isCollidable(): Boolean = false

    // No empuja entidades
    override fun isPushable(): Boolean = false

    companion object {
        // Se registra en VerityMod.kt
        lateinit var ENTITY_TYPE: EntityType<VeritySphereEntity>
    }
}