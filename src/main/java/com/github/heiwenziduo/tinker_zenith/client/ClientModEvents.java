package com.github.heiwenziduo.tinker_zenith.client;

import com.github.heiwenziduo.tinker_zenith.TinkerZenith;
import com.github.heiwenziduo.tinker_zenith.initializer.InitEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = TinkerZenith.ModId, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModEvents {
    //gecko
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {

    }

    //vanilla
    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(InitEntity.FLYING_SWORD.get(), FlyingSwordRenderer::new);
    }

    @SubscribeEvent
    public static void onRegisterLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {

    }
}