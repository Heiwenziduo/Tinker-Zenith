package com.github.heiwenziduo.tinker_zenith.initializer;

import com.github.heiwenziduo.tinker_zenith.TinkerZenith;
import com.github.heiwenziduo.tinker_zenith.tag.FlyingSwordTag;
import net.minecraftforge.eventbus.api.IEventBus;
import slimeknights.tconstruct.library.modifiers.util.ModifierDeferredRegister;
import slimeknights.tconstruct.library.modifiers.util.StaticModifier;

public class InitModifier {
    private static final ModifierDeferredRegister MODIFIERS = ModifierDeferredRegister.create(TinkerZenith.ModId);
    public static void register(IEventBus eventBus) {
        MODIFIERS.register(eventBus);
    }

    public static final StaticModifier<FlyingSwordTag> FlyingSwordTag = MODIFIERS.register("flying_sword", FlyingSwordTag::new);

}