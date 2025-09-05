package com.github.heiwenziduo.tinker_zenith.client;

import com.github.heiwenziduo.tinker_zenith.TinkerZenith;
import com.github.heiwenziduo.tinker_zenith.entity.FlyingSword;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Quaternionf;


/**
 * <h3>references:</h3>
 * <ul>
 *     <li>{@link net.minecraft.client.renderer.entity.ItemFrameRenderer} {@link net.minecraft.client.renderer.entity.FoxRenderer} 物品渲染参考</li>
 *     <li><a href="https://learnopengl.com/Getting-started/Transformations">@OpenGL</a> 矩阵变换</li>
 *     <li><a href="https://github.com/Krasjet/quaternion">@Github</a> 四元数</li>
 * </ul>
 * <p>"It is advised to first do scaling operations, then rotations and lastly translations when combining matrices otherwise they may (negatively) affect each other."<p/>
 */
@OnlyIn(Dist.CLIENT)
public class FlyingSwordRenderer extends EntityRenderer<FlyingSword> {
    // 用于将工具手柄端点放在坐标原点 #并非所有工具贴图都能正确放置, 这里的参数对应原版的剑类
    // 1/32 = 0.03125 但这里似乎不适合用其整数倍, 或为itemRender中已有某种缩放的缘故, 待查正//todo
    private static final double textureOffsetX = .203;
    private static final double textureOffsetY = .078;
    private static final double textureOffsetY2 = .491 + textureOffsetY;

    private final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(TinkerZenith.ModId, "");
    private final ItemRenderer itemRenderer;

    public FlyingSwordRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager);

        itemRenderer = renderManager.getItemRenderer();
    }

    @Override
    public void render(FlyingSword entity, float entityYaw, float partialTicks,
                       PoseStack poseStack, MultiBufferSource buffer, int packedLight) {

        ItemStack stack = entity.getItemStack();
        FlyingSword.BEHAVIOR_MODE_LIST mode = entity.getBehaviorMode();
        int slotNumber = entity.getSlotNumber();
        float lunchPitch = entity.getLunchPitch();
        float xRot;
        xRot = entity.getXRot();

        poseStack.pushPose();
//
//        // 平滑跟踪位置
//        Vec3 smoothedPos = entity.getSmoothedPosition(partialTicks);
//        poseStack.translate(
//                smoothedPos.x - entity.getX(),
//                smoothedPos.y - entity.getY(),
//                smoothedPos.z - entity.getZ()
//        );
//
//        // 轻微抬高飞行位置
//        poseStack.translate(0, 0.4, 0);
//
//        // 朝向移动方向旋转
//        Vec3 motion = entity.getDeltaMovement();
//        if (motion.lengthSqr() > 0.001) {
//            Vec3 norm = motion.normalize();
//            float yaw = (float)Math.atan2(norm.x, norm.z);
//            float pitch = (float)Math.asin(norm.y);
//            poseStack.mulPose(Axis.YP.rotation(yaw));
//            poseStack.mulPose(Axis.XP.rotation(-pitch));
//        }
//
//        // 动画状态驱动变换
//        switch (entity.getAnimationState()) {
//            case "charge" -> {
//                float progress = entity.getAnimationProgress(partialTicks);
//                // 冲刺
//                poseStack.scale(1.4f, 0.7f, 1.4f);
//                poseStack.mulPose(Axis.YP.rotation(progress * Mth.TWO_PI * 2));
//            }
//            case "slash" -> {
//                float progress = entity.getAnimationProgress(partialTicks);
//                // 挥砍椭圆轨迹
//                poseStack.mulPose(Axis.ZP.rotation(Mth.sin(progress * Mth.PI) * 0.8f));
//                poseStack.scale(1.2f, 1.2f, 1.2f);
//            }
//            default -> {
//                // 待机状态，上下浮动
//                float idleFloat = Mth.sin((entity.tickCount + partialTicks) * 0.15f) * 0.2f;
//                float idleRotate = (entity.tickCount + partialTicks) * 0.4f;
//                poseStack.translate(0, idleFloat, 0);
//                poseStack.mulPose(Axis.YP.rotationDegrees(idleRotate));
//            }
//        }
//
        // 渲染模型
//        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityCutout(TEXTURE));
//        model.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY,
//                1.0f, 1.0f, 1.0f, 1.0f);

        // 大概render在clientSide跑, flyingSword是服务端实体, 想要有材质得把stack从服务端同步过来. 见FlyingSword#defineSynchedData
        // todo: 拖尾
        // todo: 发射抵达终点时, 柄朝玩家
        // todo: 位于右侧的发射时左右翻转
        // todo: 再转一轴, 当前仰视时有平移感


        if (mode == FlyingSword.BEHAVIOR_MODE_LIST.LAUNCH || mode == FlyingSword.BEHAVIOR_MODE_LIST.RECOUP){
            //System.out.println(xRot);
            // 与面朝方向相同, 将刀身旋转仰角 = 视角
            Vec3 rotToVision = new Vec3(0, 0, 1).yRot((float) Math.toRadians(entityYaw)); //todo转轴不对
            Quaternionf q41 = new Quaternionf().setAngleAxis(lunchPitch, rotToVision.x, rotToVision.y, rotToVision.z);
            poseStack.mulPose(q41);

            // 与面朝方向垂直, 将刀柄转向面朝点
            Vec3 rotToFlat = new Vec3(0, 0, 1).yRot((float) Math.toRadians(entityYaw - 90));
            Quaternionf q4 = new Quaternionf().setAngleAxis(Math.toRadians(-90 - xRot), rotToFlat.x, rotToFlat.y, rotToFlat.z);
            poseStack.mulPose(q4);

            poseStack.mulPose(Axis.YP.rotationDegrees(180));
        }


        //poseStack.mulPose(Axis.XP.rotationDegrees(entity.tickCount));

        //Quaternionf q40 = new Quaternionf().setAngleAxis(90, xRotAx.x, xRotAx.y, xRotAx.z);
        //poseStack.mulPose(q40);
        //Quaternionf q41 = new Quaternionf().setAngleAxis(Math.toRadians(yaw), 0, 1, 0); // 效果同y轴

        System.out.println("Yaw:"+entityYaw+"\nYRot:"+entity.getYRot());


        if(slotNumber % 2 == 0) poseStack.mulPose(Axis.YP.rotationDegrees(180)); // 将右侧武器刀口朝外
        poseStack.mulPose(Axis.YP.rotationDegrees(entityYaw));
        poseStack.scale(2, 2, 2);
        poseStack.translate(0, textureOffsetY2, 0);
        poseStack.mulPose(Axis.ZP.rotationDegrees(-135));
        poseStack.translate(textureOffsetX, textureOffsetY, 0); //将工具手柄对齐旋转原点

        /*
         08/30 顺序有影响, 四元数究竟是怎样的原理?
         08/31 似乎是倒着向前读的:
         poseStack.mulPose(Axis.YP.rotationDegrees(entity.tickCount));
         poseStack.translate(2, 2, 0);
         上面的旋转轴在(0,0,0) (实体碰撞盒底部中心) 而下面的旋转轴在(2, 2, 0)
         poseStack.translate(2, 2, 0);
         poseStack.mulPose(Axis.YP.rotationDegrees(entity.tickCount));

         //System.out.println(entity.position().toString());//可以获取

         这一段可以让模型原地自转(y), 同时y=0上有一轴始终与模型正面保持垂直 (yawT = entity.tickCount)
         Vec3 xRotAx = new Vec3(0, 0, 1).yRot((float) Math.toRadians(yawT - 90));
         Quaternionf q42 = new Quaternionf().setAngleAxis(Math.toRadians(yawT), xRotAx.x, xRotAx.y, xRotAx.z);
         poseStack.mulPose(q42);
         poseStack.mulPose(Axis.YP.rotationDegrees(yawT));
        */
        if(stack.isEmpty()){
            stack = new ItemStack(Items.DIAMOND_SWORD);
        }
        itemRenderer.renderStatic(
                stack,
                ItemDisplayContext.GROUND,
                15728640,
                OverlayTexture.NO_OVERLAY,
                poseStack,
                buffer,
                entity.level(),
                entity.getId());

        //System.out.println(packedLight); // 15728640 日光下
        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(FlyingSword entity) {
        return TEXTURE;
    }
}
