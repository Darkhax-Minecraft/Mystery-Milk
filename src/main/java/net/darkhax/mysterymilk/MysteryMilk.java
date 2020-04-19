package net.darkhax.mysterymilk;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.darkhax.bookshelf.item.ItemGroupBase;
import net.darkhax.bookshelf.registry.RegistryHelper;
import net.darkhax.mysterymilk.item.ItemMysteryMilk;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.SpiderEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("mysterymilk")
public class MysteryMilk {
    
    private final Logger log = LogManager.getLogger("Mystery Milk");
    private final RegistryHelper registry = new RegistryHelper("mysterymilk", this.log, new ItemGroupBase("mysterymilk", () -> new ItemStack(Items.MILK_BUCKET)));
    
    public MysteryMilk() {
        
        this.registry.registerItem(new ItemMysteryMilk(32, EntityType.SQUID, this::drinkSquidMilk), "squid_milk");
        this.registry.registerItem(new ItemMysteryMilk(32, EntityType.RAVAGER, this::drinkRavengerMilk), "ravager_milk");
        this.registry.registerItem(new ItemMysteryMilk(32, EntityType.SLIME, this::drinkSlimeMilk), "slime_milk");
        this.registry.registerItem(new ItemMysteryMilk(32, EntityType.PHANTOM, this::drinkPhantomMilk), "phantom_milk");
        this.registry.registerItem(new ItemMysteryMilk(32, e -> e instanceof SpiderEntity, this::drinkSpiderMilk), "spider_milk");
        
        this.registry.initialize(FMLJavaModLoadingContext.get().getModEventBus());
    }
    
    private void drinkSquidMilk (World world, ServerPlayerEntity player) {
        
        player.addPotionEffect(new EffectInstance(Effects.BLINDNESS, 20 * 15));
    }
    
    private void drinkRavengerMilk (World world, ServerPlayerEntity player) {
        
        player.addPotionEffect(new EffectInstance(Effects.REGENERATION, 20 * 5));
        player.addPotionEffect(new EffectInstance(Effects.STRENGTH, 20 * 15));
        player.addPotionEffect(new EffectInstance(Effects.BAD_OMEN, 20 * 60));
    }
    
    private void drinkSlimeMilk (World world, ServerPlayerEntity player) {
        
        player.addPotionEffect(new EffectInstance(Effects.JUMP_BOOST, 20 * 30, 3));
    }
    
    private void drinkPhantomMilk (World world, ServerPlayerEntity player) {
        
        player.addPotionEffect(new EffectInstance(Effects.NIGHT_VISION, 20 * 30));
    }
    
    private void drinkSpiderMilk (World world, ServerPlayerEntity player) {
        
        player.addPotionEffect(new EffectInstance(Effects.POISON, 20 * 30));
    }
}