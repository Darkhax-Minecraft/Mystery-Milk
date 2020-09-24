package net.darkhax.mysterymilk;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.darkhax.bookshelf.crafting.brewing.BrewingRecipePotion;
import net.darkhax.bookshelf.item.ItemGroupBase;
import net.darkhax.bookshelf.potion.ModPotion;
import net.darkhax.bookshelf.registry.RegistryHelper;
import net.darkhax.mysterymilk.item.ItemMysteryMilk;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.RavagerEntity;
import net.minecraft.entity.monster.SlimeEntity;
import net.minecraft.entity.monster.SpiderEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.potion.Potion;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("mysterymilk")
public class MysteryMilk {
    
    private final Logger log = LogManager.getLogger("Mystery Milk");
    private final RegistryHelper registry = new RegistryHelper("mysterymilk", this.log).withItemGroup(new ItemGroupBase("mysterymilk", () -> new ItemStack(Items.MILK_BUCKET)));
    
    private final Item squidMilk;
    private final Potion inkyPotion;
    
    private final Item ravagerMilk;
    private final Potion cursedPotion;
    
    private final Item slimeMilk;
    private final Potion slimePotion;
    
    private final Item phantomMilk;
    private final Potion phantomSightPotion;
    
    private final Item spiderMilk;
    private final Potion venomPotion;
    
    public MysteryMilk() {
        
        this.squidMilk = this.registry.items.register(new ItemMysteryMilk(32, EntityType.SQUID, this::drinkSquidMilk), "squid_milk");
        this.inkyPotion = this.registry.potions.register(new ModPotion(new EffectInstance(Effects.BLINDNESS, 600, 1)), "inky");
        
        this.ravagerMilk = this.registry.items.register(new ItemMysteryMilk(32, EntityType.RAVAGER, this::drinkRavengerMilk).withMilkingEffect(this::onRavagerMilked), "ravager_milk");
        this.cursedPotion = this.registry.potions.register(new ModPotion(new EffectInstance(Effects.BAD_OMEN, 1200)), "cursed");
        
        this.slimeMilk = this.registry.items.register(new ItemMysteryMilk(32, EntityType.SLIME, this::drinkSlimeMilk).withMilkingEffect(this::onSlimeMilked), "slime_milk");
        this.slimePotion = this.registry.potions.register(new ModPotion(new EffectInstance(Effects.JUMP_BOOST, 400, 1)), "slime");
        
        this.phantomMilk = this.registry.items.register(new ItemMysteryMilk(32, EntityType.PHANTOM, this::drinkPhantomMilk), "phantom_milk");
        this.phantomSightPotion = this.registry.potions.register(new ModPotion(new EffectInstance(Effects.NIGHT_VISION, 500, 1)), "phantom_sight");
        
        this.spiderMilk = this.registry.items.register(new ItemMysteryMilk(32, e -> e instanceof SpiderEntity, this::drinkSpiderMilk), "spider_milk");
        this.venomPotion = this.registry.potions.register(new ModPotion(new EffectInstance(Effects.POISON, 120, 3)), "venom");
        
        this.registry.initialize(FMLJavaModLoadingContext.get().getModEventBus());
        
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
    }
    
    private void setup (FMLCommonSetupEvent event) {
        
        BrewingRecipeRegistry.addRecipe(new BrewingRecipePotion(this.squidMilk, this.inkyPotion));
        BrewingRecipeRegistry.addRecipe(new BrewingRecipePotion(this.ravagerMilk, this.cursedPotion));
        BrewingRecipeRegistry.addRecipe(new BrewingRecipePotion(this.slimeMilk, this.slimePotion));
        BrewingRecipeRegistry.addRecipe(new BrewingRecipePotion(this.phantomMilk, this.phantomSightPotion));
        BrewingRecipeRegistry.addRecipe(new BrewingRecipePotion(this.spiderMilk, this.venomPotion));
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
    
    private ItemStack onSlimeMilked (PlayerEntity player, ItemStack milkStack, Entity entity) {
        
        if (entity instanceof SlimeEntity) {
            
            ((SlimeEntity) entity).setHealth(-5f);
        }
        
        return milkStack;
    }
    
    private ItemStack onRavagerMilked (PlayerEntity player, ItemStack milkStack, Entity entity) {
        
        if (entity instanceof RavagerEntity) {
            
            entity.playSound(SoundEvents.ENTITY_RAVAGER_STUNNED, 1.0F, 1.0F);
            
            ((RavagerEntity) entity).setRevengeTarget(player);
        }
        
        return milkStack;
    }
}