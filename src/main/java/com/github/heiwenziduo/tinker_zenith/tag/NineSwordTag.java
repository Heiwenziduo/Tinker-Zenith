package com.github.heiwenziduo.tinker_zenith.tag;

import com.github.heiwenziduo.tinker_zenith.entity.FlyingSword;
import com.github.heiwenziduo.tinker_zenith.utility.Abbr;
import com.github.heiwenziduo.tinker_zenith.utility.HitBoxUtility;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.interaction.GeneralInteractionModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.interaction.InteractionSource;
import slimeknights.tconstruct.library.modifiers.hook.interaction.InventoryTickModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.interaction.UsingToolModifierHook;
import slimeknights.tconstruct.library.modifiers.impl.SingleLevelModifier;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;


public class NineSwordTag extends SingleLevelModifier implements InventoryTickModifierHook, GeneralInteractionModifierHook {
    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        hookBuilder.addHook(this, ModifierHooks.INVENTORY_TICK, ModifierHooks.GENERAL_INTERACT);
    }

    @Override
    public InteractionResult onToolUse(IToolStackView tool, ModifierEntry modifier, Player player, InteractionHand hand, InteractionSource source) {
        if (source == InteractionSource.RIGHT_CLICK && !tool.isBroken()) {
            GeneralInteractionModifierHook.startUsing(tool, modifier.getId(), player, hand);
            return InteractionResult.CONSUME;
        }
        return InteractionResult.PASS;
    }

    @Override
    public void onUsingTick(IToolStackView tool, ModifierEntry modifier, LivingEntity entity, int timeLeft) {
        if(!(entity instanceof Player player)) return;
        Level level = player.level();

        if(!level.isClientSide){
            int total = Abbr.getFlyingSwordCount(player);
            // 发射需要满足的条件: 1.处于发射窗口(即下述判定) 2.存在飞剑冷却完毕
            if(player.tickCount % 2 != 0) return;
            for (int i=0; i<9; i++){
                FlyingSword sword = Abbr.getSword(player, i);
                if(sword != null){
                    Vec3 target = HitBoxUtility.findVisionPosition(player);
                    boolean lunched = sword.triggerLunch(target);
                    if(lunched) break;
                }
            }
        } else {
            //
        }

    }

    @Override
    public UseAnim getUseAction(IToolStackView tool, ModifierEntry modifier) {
        return UseAnim.BOW;
    }

    @Override
    public int getUseDuration(IToolStackView tool, ModifierEntry modifier) {
        return 72000;
    }

    @Override
    public void onInventoryTick(IToolStackView tool, ModifierEntry modifier, Level world, LivingEntity holder, int itemSlot, boolean isSelected, boolean isCorrectSlot, ItemStack stack) {
        if(!isSelected) return;
        if(!(holder instanceof Player player)) return;
        // player.hold
    }

}
