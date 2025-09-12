package com.github.heiwenziduo.tinker_zenith.initializer;

import com.github.heiwenziduo.tinker_zenith.TinkerZenith;
import com.github.heiwenziduo.tinker_zenith.entity.FlyingSword;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class InitEntity {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, TinkerZenith.ModId);

    public static final RegistryObject<EntityType<FlyingSword>> FLYING_SWORD =
            ENTITY_TYPES.register("flying_sword", () -> EntityType.Builder
                    .<FlyingSword>of(FlyingSword::new, MobCategory.MISC)
                    .sized(.5F, .5F)
                    .clientTrackingRange(10)
                    .updateInterval(3) // default: 3
                    .build(ResourceLocation.fromNamespaceAndPath(TinkerZenith.ModId, "flying_sword").toString())
            );

    public static void register(IEventBus eventBus){
        ENTITY_TYPES.register(eventBus);
    }
}
