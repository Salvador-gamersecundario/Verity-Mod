package com.luis.verity.entity

import com.luis.verity.registry.ModEntities
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.ClientGamePacketListener
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.level.Level

class VeritySphereEntity(
    entityType: EntityType<out VeritySphereEntity>,
    world: Level
) : Entity(entityType, world) {

    constructor(world: Level, x: Double, y: Double, z: Double) : this(ModEntities.VERITY_SPHERE.get(), world) {
        setPos(x, y, z)
    }

    init {
        // FIX: En Forge 1.20.1, noGravity es un método, no una propiedad
        isNoGravity = true
        noPhysics = true
    }

    override fun defineSynchedData() {}

    override fun readAdditionalSaveData(nbt: CompoundTag) {}

    override fun addAdditionalSaveData(nbt: CompoundTag) {}

    override fun getAddEntityPacket(): Packet<ClientGamePacketListener> {
        return ClientboundAddEntityPacket(this)
    }

    override fun tick() {
        super.tick()
    }

    override fun isPickable(): Boolean = false
    override fun isPushable(): Boolean = false

    companion object {
        lateinit var ENTITY_TYPE: EntityType<VeritySphereEntity>
    }
}