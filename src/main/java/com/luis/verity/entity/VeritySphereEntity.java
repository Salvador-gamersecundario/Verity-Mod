package com.luis.verity.entity;

import com.luis.verity.registry.ModEntities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;

public class VeritySphereEntity extends Entity {

    // Campo estático que tenías en el companion object
    public static EntityType<VeritySphereEntity> ENTITY_TYPE;

    // Constructor principal
    public VeritySphereEntity(EntityType<? extends VeritySphereEntity> entityType, Level world) {
        super(entityType, world);
        // Lo que estaba en el bloque init {}
        this.setNoGravity(true);
        this.noPhysics = true;
    }

    // Constructor secundario
    public VeritySphereEntity(Level world, double x, double y, double z) {
        this(ModEntities.VERITY_SPHERE.get(), world);
        this.setPos(x, y, z);
    }

    @Override
    protected void defineSynchedData() {
        // Obligatorio de implementar en Entity
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag nbt) {
        // Leer datos personalizados del NBT
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag nbt) {
        // Guardar datos personalizados en el NBT
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this);
    }

    @Override
    public void tick() {
        super.tick();
    }

    @Override
    public boolean isPickable() {
        return false;
    }

    @Override
    public boolean isPushable() {
        return false;
    }
}