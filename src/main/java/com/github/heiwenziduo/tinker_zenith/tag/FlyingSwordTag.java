package com.github.heiwenziduo.tinker_zenith.tag;

import com.github.heiwenziduo.tinker_zenith.api.FlyingSwordCollideCallback;
import com.github.heiwenziduo.tinker_zenith.entity.FlyingSword;
import com.github.heiwenziduo.tinker_zenith.utility.Abbr;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.armor.EquipmentChangeModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.build.ModifierRemovalHook;
import slimeknights.tconstruct.library.modifiers.hook.display.TooltipModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.interaction.InventoryTickModifierHook;
import slimeknights.tconstruct.library.modifiers.impl.SingleLevelModifier;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.EquipmentChangeContext;
import slimeknights.tconstruct.library.tools.helper.ToolAttackUtil;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import javax.annotation.Nullable;
import java.util.List;

public class FlyingSwordTag extends SingleLevelModifier implements
        EquipmentChangeModifierHook, InventoryTickModifierHook, ModifierRemovalHook, TooltipModifierHook {
    // 此类为所有强化所共有，不要在这里加特指某个工具的数据
    // private FlyingSword flyingSwordEntity;
    // private UUID swordUUID;

    // ResourceLocation仅可包含: a-z0-9/.-_
    /** tinker工具上保存飞剑ID的key */
    public static final ResourceLocation PERSISTENT_UUID_KEY = ResourceLocation.parse("flying-sword-uuid-key");
    public static final ResourceLocation PERSISTENT_SLOT = ResourceLocation.parse("flying-sword-slot");

    /// 生成飞剑，一个工具对应的飞剑应当是唯一的
    private static String generateFlyingSword(IToolStackView tool, Level level, Player player, int itemSlot, ItemStack stack, @Nullable String uuid){
        FlyingSwordCollideCallback onFlyingSwordCollide = (targetEntity) -> {
            System.out.println(targetEntity);
            targetEntity.invulnerableTime = 0;
            //todo: 用快捷栏工具攻击
            ToolAttackUtil.attackEntity(tool, player, InteractionHand.OFF_HAND, targetEntity,
                    ToolAttackUtil.getCooldownFunction(player, InteractionHand.OFF_HAND), false, EquipmentSlot.MAINHAND);

            //targetEntity.hurt(level.damageSources().explosion(player, player), 1);
        };
        FlyingSword flyingSword = new FlyingSword(level, player, itemSlot, stack, onFlyingSwordCollide);

        level.addFreshEntity(flyingSword);
        return flyingSword.getStringUUID();
    }

    private void degenerateFlyingSword(){

    }

    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        hookBuilder.addHook(this, ModifierHooks.EQUIPMENT_CHANGE, ModifierHooks.INVENTORY_TICK, ModifierHooks.REMOVE, ModifierHooks.TOOLTIP);
    }

    @Override
    public void onInventoryTick(IToolStackView tool, ModifierEntry modifier, Level level, LivingEntity holder, int itemSlot, boolean isSelected, boolean isCorrectSlot, ItemStack stack) {
        if(!(holder instanceof Player player)) return;
        //stack = stack.copy();
        // todo: 处理位于副手的情况 08/21
        // if(itemSlot==0 && player.getOffhandItem().equals(tool)) return;


        // DeBug.Console(player, uuid);
//        if(uuid.isEmpty()){
//            // 服务端判断（空） -> 覆写uuid-NBT -> 客户端判断（有） -> 不生成飞剑
//            String uuid1 = generateFlyingSword(tool, level, player, itemSlot, stack, null);
//            tool.getPersistentData().putString(PERSISTENT_UUID_KEY, uuid1);
//        } else {
//            // 没有找到工具离开物品栏的钩子，因此逻辑改为飞剑需定期判定工具存在以续命
//            // level.getEntity(UUID.fromString(uuid));
//        }

        if(level.isClientSide) return; // uuid生成有随机性，为了避免两端不同步问题只用服务端创实体，然后通过某种神秘的minecraft力量同步
        if(player.tickCount % FlyingSword.maxLifetime != 19) return; // 此处有魔法数19
        // 将生成的飞剑实体id存到工具本身上, 再存一个当前槽位方便飞剑那边访问
        String uuid =  tool.getPersistentData().getString(PERSISTENT_UUID_KEY);
        int slot0 = tool.getPersistentData().getInt(PERSISTENT_SLOT);
        if(slot0 != itemSlot) tool.getPersistentData().putInt(PERSISTENT_SLOT, itemSlot);

        if(!Inventory.isHotbarSlot(itemSlot)) return; // 只有位于快捷栏且不在副手时生成飞剑
        FlyingSword flyingSword = Abbr.getSword(player, itemSlot);
        if(flyingSword == null){
            String uuid1 = generateFlyingSword(tool, level, player, itemSlot, stack, null);
            tool.getPersistentData().putString(PERSISTENT_UUID_KEY, uuid1);
        } else if (uuid.isEmpty() || !uuid.equals(flyingSword.getStringUUID()) || itemSlot != flyingSword.getSlotNumber()) {
            flyingSword.setToDiscard("in list but no uuid on tool || uuid not match");
            String uuid1 = generateFlyingSword(tool, level, player, itemSlot, stack, null);
            tool.getPersistentData().putString(PERSISTENT_UUID_KEY, uuid1);
        } else {
            // 完全匹配, 你的剑就是我的剑

        }

        // if(stack!=null && player.tickCount % 20 == 0) DeBug.Console(player, stack.toString());
        // DeBug.Console(player, "客户端："+level.isClientSide);
    }

    @Override
    public @Nullable Component onRemoved(IToolStackView tool, Modifier modifier) {
        tool.getPersistentData().remove(PERSISTENT_UUID_KEY);
        tool.getPersistentData().remove(PERSISTENT_SLOT);
        return null;
    }

    @Override
    public void addTooltip(IToolStackView tool, ModifierEntry modifier, @Nullable Player player, List<Component> tooltip, TooltipKey tooltipKey, TooltipFlag tooltipFlag) {

    }
}
