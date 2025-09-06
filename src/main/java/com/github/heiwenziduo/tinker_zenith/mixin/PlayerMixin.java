package com.github.heiwenziduo.tinker_zenith.mixin;

import com.github.heiwenziduo.tinker_zenith.api.mixin.FlyingSwordManager;
import com.github.heiwenziduo.tinker_zenith.api.mixin.PlayerMixinAPI;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(value = Player.class)
public abstract class PlayerMixin extends LivingEntity implements PlayerMixinAPI {

    protected PlayerMixin(EntityType<? extends LivingEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

//    @Unique
//    private FlyingSword[] nineSword$HotbarHolder = new FlyingSword[9];

    /** 玩家快捷栏的至多⑨把飞剑的引用，其顺序与快捷栏对应，且没有飞剑的位置为null */
//    @Override
//    public FlyingSword[] getNineSword$HotbarHolder() {
//        return this.nineSword$HotbarHolder;
//    }

    @Unique
    protected FlyingSwordManager flyingSwordManager = new FlyingSwordManager();

    @Unique
    @Override
    public FlyingSwordManager getFlyingSwordManager() {
        return flyingSwordManager;
    }
}