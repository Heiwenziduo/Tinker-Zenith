package com.github.heiwenziduo.tinker_zenith.utility;

import com.github.heiwenziduo.tinker_zenith.api.mixin.PlayerMixinAPI;
import com.github.heiwenziduo.tinker_zenith.entity.FlyingSword;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

/** 各种缩写和语法糖 */
public class Abbr {
    public static FlyingSword[] getSwordBar(Player player){
        return ((PlayerMixinAPI) player).getFlyingSwordManager().getBar();
    }

    @Nullable
    public static FlyingSword getSword(Player player, int slot){
        return ((PlayerMixinAPI) player).getFlyingSwordManager().get(slot);
    }

    /** 使得玩家实体可以访问飞剑 */
    public static void setPlayerSwords(Player player, int slot, @Nullable FlyingSword sword){
        ((PlayerMixinAPI) player).getFlyingSwordManager().set(slot, sword);
    }

    /** 获取当前飞剑总数 */
    public static int getFlyingSwordCount(Player player){
        return ((PlayerMixinAPI) player).getFlyingSwordManager().getCount();
    }
}
