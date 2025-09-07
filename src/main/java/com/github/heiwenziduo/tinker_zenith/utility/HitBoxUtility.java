package com.github.heiwenziduo.tinker_zenith.utility;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/// 碰撞箱相关
public class HitBoxUtility {
    /// 给定实体视线方向最近的实体/方块/空气
    public static Vec3 findVisionPosition(Entity entity, float maxDistance, boolean ignoreBlock, @Nullable List<? extends Entity> excludeEntities) {
        Level level = entity.level();
        Vec3 lookAt = entity.getLookAngle();
        Vec3 startPos = entity.position().add(0, entity.getEyeHeight(), 0);
        Vec3 endPos = startPos.add(lookAt.scale(maxDistance));

        if (!ignoreBlock){
            BlockHitResult blockHitResult = level.clip(new ClipContext(startPos, endPos, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, null));
            endPos = blockHitResult.getLocation();
        }
        Vec3 centerPoint = startPos.lerp(endPos, 0.5);
        double distance = startPos.distanceTo(endPos);
        double dSqr = distance * distance;
        AABB detectBox = AABB.ofSize(centerPoint, distance, distance, distance);
        List<? extends Entity> entitiesList = level.getEntities(entity, detectBox,
                e -> e instanceof LivingEntity && e.isPickable() && e.isAlive());
        for (var e : entitiesList){
            //System.out.println(e);
            if(excludeEntities != null && excludeEntities.contains(e)) continue;
            //todo: 判定范围随目标碰撞箱而定 #getBbWidth, 然后通过向量算出来
            if(Vector0.getTheta(lookAt, startPos.vectorTo(e.position())) >=  (double) 1 /20) continue;
            double d = startPos.distanceToSqr(e.position());
            if(d < dSqr) dSqr = d;
        }
        return lookAt.scale(Math.sqrt(dSqr)).add(startPos);
    }
    /// 给定实体视线方向最近的实体/方块/空气, 默认距离16
    public static Vec3 findVisionPosition(Entity entity) {
        return findVisionPosition(entity, 16, false, null);
    }
}
