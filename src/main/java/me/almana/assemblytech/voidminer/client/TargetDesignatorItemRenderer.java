package me.almana.assemblytech.voidminer.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.MapCodec;
import me.almana.assemblytech.Assemblytech;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.joml.Vector3fc;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.function.Consumer;

public final class TargetDesignatorItemRenderer implements SpecialModelRenderer<Identifier> {
    public static final Identifier ID = Identifier.fromNamespaceAndPath(Assemblytech.MODID, "target_designator");
    private static final Identifier BASE_TEXTURE = texture("target");
    private static final Identifier EARTH_TEXTURE = texture("target_planet1");
    private static final Map<Item, Identifier> TEXTURES = new IdentityHashMap<>();

    private final PieceModel base;
    private final PieceModel target;

    private TargetDesignatorItemRenderer(PieceModel base, PieceModel target) {
        this.base = base;
        this.target = target;
    }

    @Override
    public Identifier extractArgument(ItemStack stack) {
        return TEXTURES.getOrDefault(stack.getItem(), EARTH_TEXTURE);
    }

    public static void applyTextures(Map<Identifier, String> textures) {
        TEXTURES.clear();
        textures.forEach((itemId, textureName) ->
                TEXTURES.put(BuiltInRegistries.ITEM.getValue(itemId), texture(textureName)));
    }

    @Override
    public void submit(
            Identifier texture,
            PoseStack poseStack,
            SubmitNodeCollector submitNodeCollector,
            int lightCoords,
            int overlayCoords,
            boolean hasFoil,
            int outlineColor
    ) {
        poseStack.pushPose();
        poseStack.translate(0.5F, 1.5F, 0.5F);
        poseStack.scale(1.0F, -1.0F, -1.0F);
        submitNodeCollector.submitModel(base, null, poseStack, BASE_TEXTURE, lightCoords, overlayCoords, 0, null);
        submitNodeCollector.submitModel(target, null, poseStack, texture, lightCoords, overlayCoords, 0, null);
        poseStack.popPose();
    }

    @Override
    public void getExtents(Consumer<Vector3fc> output) {
        PoseStack poseStack = new PoseStack();
        poseStack.translate(0.5F, 1.5F, 0.5F);
        poseStack.scale(1.0F, -1.0F, -1.0F);
        base.root().getExtentsForGui(poseStack, output);
        target.root().getExtentsForGui(poseStack, output);
    }

    private static Identifier texture(String name) {
        if ("earth".equals(name)) return EARTH_TEXTURE;
        Identifier id = Identifier.tryBuild(Assemblytech.MODID, "textures/item/target/" + name + ".png");
        return id == null ? EARTH_TEXTURE : id;
    }

    private static LayerDefinition createBaseLayer() {
        MeshDefinition mesh = new MeshDefinition();
        var root = mesh.getRoot();
        root.addOrReplaceChild("base", CubeListBuilder.create()
                .texOffs(6, 0).addBox(-8.0F, -2.0F, -6.0F, 2.0F, 2.0F, 12.0F, new CubeDeformation(0.0F))
                .texOffs(8, 2).addBox(6.0F, -2.0F, -6.0F, 2.0F, 2.0F, 12.0F, new CubeDeformation(0.0F))
                .texOffs(9, 4).addBox(-6.0F, -2.0F, -8.0F, 12.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(10, 6).addBox(-6.0F, -2.0F, 6.0F, 12.0F, 2.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(0, 6).addBox(-6.0F, -1.0F, -6.0F, 12.0F, 0.0F, 12.0F, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 24.0F, 0.0F));
        return LayerDefinition.create(mesh, 32, 32);
    }

    private static LayerDefinition createTargetLayer() {
        MeshDefinition mesh = new MeshDefinition();
        var root = mesh.getRoot();
        root.addOrReplaceChild("target", CubeListBuilder.create()
                .texOffs(0, 0).addBox(-6.0F, -18.0F, 0.0F, 12.0F, 12.0F, 0.0F, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 24.0F, 0.0F));
        return LayerDefinition.create(mesh, 32, 32);
    }

    private static final class PieceModel extends Model<Void> {
        private PieceModel(ModelPart root) {
            super(root, RenderTypes::entityCutout);
        }
    }

    public record Unbaked() implements SpecialModelRenderer.Unbaked<Identifier> {
        public static final MapCodec<TargetDesignatorItemRenderer.Unbaked> MAP_CODEC =
                MapCodec.unit(new TargetDesignatorItemRenderer.Unbaked());

        @Override
        public MapCodec<? extends SpecialModelRenderer.Unbaked<Identifier>> type() {
            return MAP_CODEC;
        }

        @Override
        public SpecialModelRenderer<Identifier> bake(SpecialModelRenderer.BakingContext context) {
            return new TargetDesignatorItemRenderer(
                    new PieceModel(createBaseLayer().bakeRoot()),
                    new PieceModel(createTargetLayer().bakeRoot())
            );
        }
    }
}
