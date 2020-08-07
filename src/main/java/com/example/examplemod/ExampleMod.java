package com.example.examplemod;

import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("stormhammer")
public class ExampleMod
{
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    public ExampleMod() {
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onHurt(LivingHurtEvent event) {
        try {
            Entity attacker = event.getSource().getImmediateSource();
            Entity injured = event.getEntity();
            DamageSource injury = event.getSource();

            LOGGER.info(">>> " + (attacker != null ? attacker.getName().getString() : "The world") +
                    " hurt " + injured.getName().getString() +
                    " with " + injury.getDamageType());

            World world = event.getEntityLiving().getEntityWorld();

            // Summon lightning
            world.addWeatherEffect(new EntityLightningBolt(
                    world,
                    event.getEntity().posX,
                    event.getEntity().posY,
                    event.getEntity().posZ,
                    false));

            explodeQueue.add(event.getEntity());
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
    }

    private Queue<Entity> explodeQueue = new ConcurrentLinkedDeque<>();

    @SubscribeEvent
    public void onTick(TickEvent.WorldTickEvent event) {
        World world = event.world;
        Entity entity = explodeQueue.poll();
        if(entity != null) {
            // Make an explosion
            world.createExplosion(
                    entity,
                    entity.posX,
                    entity.posY,
                    entity.posZ,
                    8,
                    true);
        }
    }
}
