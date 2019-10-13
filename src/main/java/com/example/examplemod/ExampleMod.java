package com.example.examplemod;

import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
            if (attacker instanceof EntityPlayer) {
                Entity injured = event.getEntity();
                DamageSource injury = event.getSource();

                LOGGER.info(">>> " + attacker.getName().getString() +
                        " hurt " + injured.getName().getString() +
                        " with " + injury.getDamageType());

                World world = event.getEntityLiving().getEntityWorld();
                EntityPlayer player = (EntityPlayer) attacker;
                ItemStack mainHandIemStack = player.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND);

                if (mainHandIemStack.getItem().equals(Items.GOLDEN_AXE) &&
                        mainHandIemStack.getDisplayName().getString().equalsIgnoreCase("stormbreaker")) {

                    // Summon lightning
                    world.addWeatherEffect(new EntityLightningBolt(
                            world,
                            event.getEntity().posX,
                            event.getEntity().posY,
                            event.getEntity().posZ,
                            false));

                    // Make an explosion
                    world.createExplosion(
                            event.getEntity(),
                            event.getEntity().posX,
                            event.getEntity().posY,
                            event.getEntity().posZ,
                            8,
                            true);
                }
            }
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
    }
}
