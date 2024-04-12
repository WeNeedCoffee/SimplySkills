package net.sweenus.simplyskills.mixins.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.puffish.skillsmod.client.gui.SkillsScreen;
import net.puffish.skillsmod.utils.Bounds2i;
import net.sweenus.simplyskills.client.gui.TextureState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;


@Mixin(SkillsScreen.class)
public abstract class SkillsScreenMixin {

    @Unique
    Map<Identifier, TextureState> textureStates = new HashMap<>();
    @Unique
    private final Identifier cloudsTexture1 = new Identifier("simplyskills", "textures/backgrounds/decor/clouds.png");
    @Unique
    private final Identifier cloudsTexture2 = new Identifier("simplyskills", "textures/backgrounds/decor/clouds_2.png");
    @Unique
    private final Identifier cloudsTexture3 = new Identifier("simplyskills", "textures/backgrounds/decor/clouds_3.png");
    @Unique
    private float cloudsX = 0;

    @Unique
    private Identifier selectedCloudsTexture = null;

    @Unique
    private void selectRandomCloudsTexture() {
        Random random = new Random();
        int textureIndex = random.nextInt(3);
        switch (textureIndex) {
            case 0:
                selectedCloudsTexture = cloudsTexture1;
                break;
            case 1:
                selectedCloudsTexture = cloudsTexture2;
                break;
            case 2:
                selectedCloudsTexture = cloudsTexture3;
                break;
        }
    }

    /* Scaling Variant - not aligned
    @Redirect(method = "drawContentWithCategory(Lnet/minecraft/client/gui/DrawContext;DDLnet/puffish/skillsmod/client/data/ClientSkillCategoryData;)V",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIFFIIII)V"))
    private void redirectDrawTexture(DrawContext context, Identifier texture, int x, int y, float u, float v, int width, int height, int textureWidth, int textureHeight) {
        int originalTextureWidth = 5120;
        int originalTextureHeight = 2880;

        float aspectRatio = (float) originalTextureWidth / (float) originalTextureHeight;

        int newTextureWidth = bounds.width();
        int newTextureHeight = (int) (newTextureWidth / aspectRatio);

        int centerX = bounds.min().x + bounds.width() / 2;
        int centerY = bounds.min().y + bounds.height() / 2;

        int newX = centerX - newTextureWidth / 2;
        int newY = centerY - newTextureHeight / 2;

        context.drawTexture(texture, newX, newY, u, v, newTextureWidth, newTextureHeight, newTextureWidth, newTextureHeight);
    }
     */
    // Maintains image position in 16:9
    /*
    @Redirect(method = "drawContentWithCategory(Lnet/minecraft/client/gui/DrawContext;DDLnet/puffish/skillsmod/client/data/ClientSkillCategoryData;)V",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIFFIIII)V"))
    private void redirectDrawTexture(DrawContext context, Identifier texture, int x, int y, float u, float v, int width, int height, int textureWidth, int textureHeight) {
        SkillsScreen screen = (SkillsScreen) (Object) this;
        SkillsScreenAccessor accessor = (SkillsScreenAccessor) screen;
        Bounds2i bounds = accessor.getBounds();
        int originalTextureWidth = 1920;
        int originalTextureHeight = 1080;

        float aspectRatio = (float) originalTextureWidth / (float) originalTextureHeight;

        int newTextureWidth = bounds.width(); // new width
        int newTextureHeight = (int) (newTextureWidth / aspectRatio);

        int centerX = bounds.min().x + bounds.width() / 2;
        int centerY = bounds.min().y + bounds.height() / 2;

        int newX = centerX - newTextureWidth / 2;
        int newY = centerY - newTextureHeight / 2;

        context.drawTexture(texture, newX, newY, u, v, newTextureWidth, newTextureHeight, newTextureWidth, newTextureHeight);
    }*/

    // MAIN
    @Redirect(method = "drawContentWithCategory(Lnet/minecraft/client/gui/DrawContext;DDLnet/puffish/skillsmod/client/data/ClientSkillCategoryData;)V",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIFFIIII)V"))
    private void redirectDrawTexture(DrawContext context, Identifier texture, int x, int y, float u, float v, int width, int height, int textureWidth, int textureHeight) {
        SkillsScreen screen = (SkillsScreen) (Object) this;
        SkillsScreenAccessor accessor = (SkillsScreenAccessor) screen;
        Bounds2i bounds = accessor.getBounds();
        // Original texture dimensions
        int originalTextureWidth = 5120;
        int originalTextureHeight = 1440;

        // Calculate the aspect ratio of the texture
        float aspectRatio = (float) originalTextureWidth / (float) originalTextureHeight;

        // New width and height based on bounds height while maintaining aspect ratio
        int newTextureHeight = bounds.height(); // new height
        int newTextureWidth = (int) (newTextureHeight * aspectRatio);

        // Calculate the center position of the bounds
        int centerX = bounds.min().x + bounds.width() / 2;
        int centerY = bounds.min().y + bounds.height() / 2;

        // Adjust x and y to make the image's center align with the center of the bounds
        int newX = centerX - newTextureWidth / 2;
        int newY = centerY - newTextureHeight / 2;

        // Draw a black rectangle behind the texture
        int colorBlack = 0xFF000000; // ARGB format for opaque black
        context.fill(x, y, x + bounds.width(), y + bounds.height(), colorBlack);

        // Draw the texture centered at the specified point
        context.drawTexture(texture, newX, newY, u, v, newTextureWidth, newTextureHeight, newTextureWidth, newTextureHeight);
        // Don't draw star systems when prominent is loaded
        if (!FabricLoader.getInstance().isModLoaded("prominent"))
            drawParallaxTextures(context, bounds);
    }

    @Unique
    private void drawParallaxTextures(DrawContext context, Bounds2i bounds) {
        Identifier parallaxTexture1 = new Identifier("simplyskills", "textures/backgrounds/decor/planet_01.png");
        Identifier parallaxTexture2 = new Identifier("simplyskills", "textures/backgrounds/decor/planet_02.png");
        Identifier parallaxTexture3 = new Identifier("simplyskills", "textures/backgrounds/decor/planet_03.png");
        Identifier parallaxTexture4 = new Identifier("simplyskills", "textures/backgrounds/decor/planet_04.png");
        Identifier parallaxTexture5 = new Identifier("simplyskills", "textures/backgrounds/decor/planet_05.png");
        Identifier parallaxTexture6 = new Identifier("simplyskills", "textures/backgrounds/decor/planet_06.png");
        Identifier parallaxTexture7 = new Identifier("simplyskills", "textures/backgrounds/decor/planet_07.png");
        Identifier parallaxTexture8 = new Identifier("simplyskills", "textures/backgrounds/decor/planet_08.png");
        Identifier parallaxTexture9 = new Identifier("simplyskills", "textures/backgrounds/decor/planet_09.png");
        Identifier parallaxTexture10 = new Identifier("simplyskills", "textures/backgrounds/decor/planet_10.png");
        Identifier parallaxTexture11 = new Identifier("simplyskills", "textures/backgrounds/decor/planet_11.png");
        Identifier parallaxTexture12 = new Identifier("simplyskills", "textures/backgrounds/decor/planet_12.png");
        Identifier parallaxTexture13 = new Identifier("simplyskills", "textures/backgrounds/decor/planet_13.png");
        Identifier parallaxTexture14 = new Identifier("simplyskills", "textures/backgrounds/decor/planet_14.png");
        Identifier parallaxTexture15 = new Identifier("simplyskills", "textures/backgrounds/decor/planet_15.png");
        Identifier parallaxTexture16 = new Identifier("simplyskills", "textures/backgrounds/decor/planet_16.png");
        Identifier parallaxTexture17 = new Identifier("simplyskills", "textures/backgrounds/decor/planet_17.png");
        Identifier parallaxTexture18 = new Identifier("simplyskills", "textures/backgrounds/decor/planet_18.png");
        Identifier parallaxTexture19 = new Identifier("simplyskills", "textures/backgrounds/decor/planet_19.png");
        Identifier parallaxTexture20 = new Identifier("simplyskills", "textures/backgrounds/decor/planet_20.png");
        Identifier parallaxTexture21 = new Identifier("simplyskills", "textures/backgrounds/decor/planet_21.png");
        Identifier parallaxTexture22 = new Identifier("simplyskills", "textures/backgrounds/decor/planet_22.png");
        Identifier parallaxTexture23 = new Identifier("simplyskills", "textures/backgrounds/decor/planet_23.png");
        Identifier parallaxTexture24 = new Identifier("simplyskills", "textures/backgrounds/decor/planet_24.png");
        Identifier parallaxTexture25 = new Identifier("simplyskills", "textures/backgrounds/decor/planet_25.png");
        Identifier parallaxTexture26 = new Identifier("simplyskills", "textures/backgrounds/decor/planet_26.png");
        Identifier parallaxTexture27 = new Identifier("simplyskills", "textures/backgrounds/decor/planet_27.png");
        Identifier parallaxTexture28 = new Identifier("simplyskills", "textures/backgrounds/decor/planet_28.png");
        Identifier parallaxTexture29 = new Identifier("simplyskills", "textures/backgrounds/decor/planet_29.png");
        Identifier parallaxTexture30 = new Identifier("simplyskills", "textures/backgrounds/decor/planet_30.png");

        long currentTime = System.currentTimeMillis();

        updateAndDrawAnimatedTexture(context, parallaxTexture1, bounds, currentTime);
        updateAndDrawAnimatedTexture(context, parallaxTexture2, bounds, currentTime);
        updateAndDrawAnimatedTexture(context, parallaxTexture3, bounds, currentTime);
        updateAndDrawAnimatedTexture(context, parallaxTexture4, bounds, currentTime);
        updateAndDrawAnimatedTexture(context, parallaxTexture5, bounds, currentTime);
        updateAndDrawAnimatedTexture(context, parallaxTexture6, bounds, currentTime);
        updateAndDrawAnimatedTexture(context, parallaxTexture7, bounds, currentTime);
        updateAndDrawAnimatedTexture(context, parallaxTexture8, bounds, currentTime);
        updateAndDrawAnimatedTexture(context, parallaxTexture9, bounds, currentTime);
        updateAndDrawAnimatedTexture(context, parallaxTexture10, bounds, currentTime);
        updateAndDrawAnimatedTexture(context, parallaxTexture11, bounds, currentTime);
        updateAndDrawAnimatedTexture(context, parallaxTexture12, bounds, currentTime);
        updateAndDrawAnimatedTexture(context, parallaxTexture13, bounds, currentTime);
        updateAndDrawAnimatedTexture(context, parallaxTexture14, bounds, currentTime);
        updateAndDrawAnimatedTexture(context, parallaxTexture15, bounds, currentTime);
        updateAndDrawAnimatedTexture(context, parallaxTexture16, bounds, currentTime);
        updateAndDrawAnimatedTexture(context, parallaxTexture17, bounds, currentTime);
        updateAndDrawAnimatedTexture(context, parallaxTexture18, bounds, currentTime);
        updateAndDrawAnimatedTexture(context, parallaxTexture19, bounds, currentTime);
        updateAndDrawAnimatedTexture(context, parallaxTexture20, bounds, currentTime);
        updateAndDrawAnimatedTexture(context, parallaxTexture21, bounds, currentTime);
        updateAndDrawAnimatedTexture(context, parallaxTexture22, bounds, currentTime);
        updateAndDrawAnimatedTexture(context, parallaxTexture23, bounds, currentTime);
        updateAndDrawAnimatedTexture(context, parallaxTexture24, bounds, currentTime);
        updateAndDrawAnimatedTexture(context, parallaxTexture25, bounds, currentTime);
        updateAndDrawAnimatedTexture(context, parallaxTexture26, bounds, currentTime);
        updateAndDrawAnimatedTexture(context, parallaxTexture27, bounds, currentTime);
        updateAndDrawAnimatedTexture(context, parallaxTexture28, bounds, currentTime);
        updateAndDrawAnimatedTexture(context, parallaxTexture29, bounds, currentTime);
        updateAndDrawAnimatedTexture(context, parallaxTexture30, bounds, currentTime);
    }

    @Unique
    private void updateAndDrawAnimatedTexture(DrawContext context, Identifier texture, Bounds2i bounds, long currentTime) {
        if (cloudsX == 0)
            cloudsX = -bounds.width();
        int frameCount = 120;
        int spriteSheetWidth = 7680;
        int frameWidth = spriteSheetWidth / frameCount;
        int frameHeight = 64;

        // Initialize texture state if not present
        textureStates.computeIfAbsent(texture, k -> {
            float initialX = bounds.min().x + (float) Math.random() * (bounds.width() - frameWidth);
            float initialY = bounds.min().y + (float) Math.random() * (bounds.height() - frameHeight);
            float scale = (Math.max(0.4f, (float) (Math.random() * 5f)));
            float speed = Math.min(0.04f, (float) ((Math.random() * 0.08f) / scale));
            long animationSpeed = Math.max(240, (long) (Math.random() * 300 + (10 * scale)));
            float brightness = Math.min(0.5f, 0.1f + (float) Math.random());
            TextureState newState = new TextureState(initialX, bounds.min().y, speed, scale, animationSpeed, brightness);
            float[] newPosition = updatePlanetPosition(initialX, initialY, bounds, speed, scale);
            newState.x = newPosition[0];
            newState.y = newPosition[1];
            return newState;
        });

        TextureState state = textureStates.get(texture);
        int currentFrame = (int) ((currentTime / state.animationSpeed) % frameCount);
        int u = currentFrame * frameWidth;
        float[] newPosition = updatePlanetPosition(state.x, state.y, bounds, state.speed, state.scale);
        state.x = newPosition[0];
        state.y = newPosition[1];
        MatrixStack matrixStack = context.getMatrices();
        matrixStack.push();
        matrixStack.scale(state.scale, state.scale, 1.0f);

        int boundsWidth = bounds.width();
        int scaledWidth = (int) (frameWidth * state.scale);
        float effectiveXCenter = state.x + scaledWidth / 2.0f;
        float distanceToLeftEdge = effectiveXCenter - bounds.min().x;
        float distanceToRightEdge = bounds.max().x - effectiveXCenter;
        float effectiveEdgeDistance = Math.min(distanceToLeftEdge, distanceToRightEdge);
        float edgeFactor = effectiveEdgeDistance / (boundsWidth / 2.0f);
        float dynamicBrightness = state.brightness * edgeFactor;
        dynamicBrightness = Math.max(dynamicBrightness, 0.0f);

        RenderSystem.setShaderColor(dynamicBrightness, dynamicBrightness, dynamicBrightness, 1.0F);
        context.drawTexture(texture, (int) state.x, (int) state.y, u, 0, frameWidth, frameHeight, spriteSheetWidth, frameHeight);

        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        matrixStack.pop();
        updateAndDrawCloudsTexture(context, bounds);
    }

    @Unique
    private float[] updatePlanetPosition(float x, float y, Bounds2i bounds, float moveSpeed, float scale) {
        int frameCount = 120;
        int spriteSheetWidth = 7680;
        int frameWidth = spriteSheetWidth / frameCount;
        int frameHeight = 64;
        int scaledWidth = (int) (frameWidth * scale);
        int scaledHeight = (int) (frameHeight * scale);
        float newX = x - moveSpeed;
        float newY = y + moveSpeed;
        int outOfBoundsMargin = 800;

        boolean outOfBoundsHorizontally = newX + scaledWidth + outOfBoundsMargin < bounds.min().x || newX - outOfBoundsMargin > bounds.max().x;
        boolean outOfBoundsVertically = newY - outOfBoundsMargin > bounds.max().y;

        if (outOfBoundsHorizontally || outOfBoundsVertically) {
            newX = bounds.min().x + (float) Math.random() * (bounds.width() - scaledWidth);
            newY = bounds.min().y - scaledHeight - (float) Math.random() * (scaledHeight + outOfBoundsMargin);
        }

        return new float[]{newX, newY};
    }

    @Unique
    private void updateAndDrawCloudsTexture(DrawContext context, Bounds2i bounds) {
        if (selectedCloudsTexture == null) {
            selectRandomCloudsTexture();
        }
        float cloudsSpeed = 0.001f;
        float newX = cloudsX + cloudsSpeed;
        if (newX <= -bounds.width()) {
            newX = -bounds.width();
        } else if (newX > 0) {
            newX = 0;
        }
        cloudsX = newX;
        MatrixStack matrixStack = context.getMatrices();
        matrixStack.push();

        // Set partial transparency
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 0.1F);
        int cloudsHeight = 1440;
        context.drawTexture(selectedCloudsTexture, (int) cloudsX, bounds.min().y, 0, 0, bounds.width() + 10240, cloudsHeight, bounds.width() + 10240, cloudsHeight);
        RenderSystem.disableBlend();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        matrixStack.pop();
    }
}