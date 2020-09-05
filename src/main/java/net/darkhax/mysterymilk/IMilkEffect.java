package net.darkhax.mysterymilk;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

@FunctionalInterface
public interface IMilkEffect {
    
    /**
     * Allows effects to happen when the entity is being milked.
     * 
     * @param player The player doing the milking.
     * @param givenStack The item that was milked.
     * @param target The entity that was milked.
     * @return The item stack to give the player for milking.
     */
    ItemStack apply (PlayerEntity player, ItemStack givenStack, Entity target);
}