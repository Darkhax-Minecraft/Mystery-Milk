package net.darkhax.mysterymilk.item;

import java.util.function.BiConsumer;
import java.util.function.Predicate;

import javax.annotation.Nullable;

import net.darkhax.mysterymilk.IMilkEffect;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.UseAction;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tags.Tag;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.fluids.capability.wrappers.FluidBucketWrapper;

public class ItemMysteryMilk extends Item {
    
    private final int useTicks;
    private final BiConsumer<World, ServerPlayerEntity> effect;
    private final Predicate<Entity> milkingPredicate;
    
    @Nullable
    private IMilkEffect milkingEffect;
    
    public ItemMysteryMilk(int useTicks, Tag<Entity> type, BiConsumer<World, ServerPlayerEntity> effect) {
        
        this(useTicks, e -> type.contains(e), effect);
    }
    
    public ItemMysteryMilk(int useTicks, EntityType<?> type, BiConsumer<World, ServerPlayerEntity> effect) {
        
        this(useTicks, e -> e.getType() == type, effect);
    }
    
    public ItemMysteryMilk(int useTicks, Predicate<Entity> milkingPredicate, BiConsumer<World, ServerPlayerEntity> effect) {
        
        super(new Properties().maxStackSize(1).containerItem(Items.BUCKET));
        
        this.useTicks = useTicks;
        this.milkingPredicate = milkingPredicate;
        this.effect = effect;
        
        MinecraftForge.EVENT_BUS.addListener(this::tryToMilk);
    }
    
    public ItemMysteryMilk withMilkingEffect (IMilkEffect effect) {
        
        this.milkingEffect = effect;
        return this;
    }
    
    private void tryToMilk (EntityInteract event) {
        
        final ItemStack heldItem = event.getItemStack();
        final PlayerEntity player = event.getPlayer();
        
        if (player instanceof ServerPlayerEntity && this.milkingPredicate.test(event.getTarget()) && heldItem.getItem() == Items.BUCKET && !event.getEntityLiving().isChild()) {
            
            player.playSound(SoundEvents.ENTITY_COW_MILK, 1.0F, 1.0F);
            
            CriteriaTriggers.PLAYER_ENTITY_INTERACTION.test((ServerPlayerEntity) player, event.getItemStack(), event.getTarget());
            
            if (!player.isCreative()) {
                
                heldItem.shrink(1);
            }
            
            ItemStack newStack = new ItemStack(this);
            
            if (this.milkingEffect != null) {
                
                newStack = this.milkingEffect.apply(player, newStack, event.getTarget());
            }
            
            if (heldItem.isEmpty()) {
                
                player.setHeldItem(event.getHand(), newStack);
            }
            
            else if (!player.inventory.addItemStackToInventory(newStack)) {
                
                player.dropItem(newStack, false);
            }
        }
    }
    
    @Override
    public ItemStack onItemUseFinish (ItemStack stack, World world, LivingEntity living) {
        
        if (living instanceof ServerPlayerEntity) {
            
            this.effect.accept(world, (ServerPlayerEntity) living);
        }
        
        if (living instanceof PlayerEntity && !((PlayerEntity) living).abilities.isCreativeMode) {
            
            stack.shrink(1);
        }
        
        return stack.isEmpty() ? new ItemStack(Items.BUCKET) : stack;
    }
    
    @Override
    public int getUseDuration (ItemStack stack) {
        
        return this.useTicks;
    }
    
    @Override
    public UseAction getUseAction (ItemStack stack) {
        
        return UseAction.DRINK;
    }
    
    @Override
    public ActionResult<ItemStack> onItemRightClick (World world, PlayerEntity player, Hand hand) {
        
        player.setActiveHand(hand);
        return ActionResult.resultSuccess(player.getHeldItem(hand));
    }
    
    @Override
    public ICapabilityProvider initCapabilities (ItemStack stack, @Nullable CompoundNBT nbt) {
        
        return new FluidBucketWrapper(stack);
    }
}