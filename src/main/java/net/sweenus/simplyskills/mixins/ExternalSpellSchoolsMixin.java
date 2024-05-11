package net.sweenus.simplyskills.mixins;

import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.puffish.attributesmod.AttributesMod;
import net.spell_engine.api.spell.ExternalSpellSchools;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ExternalSpellSchools.class)
public abstract class ExternalSpellSchoolsMixin {

    @Inject(method = "rangedDamageAttribute()Lnet/minecraft/entity/attribute/EntityAttribute;", at = @At("HEAD"), cancellable = true)
    private static void changeRangedDamageAttribute(CallbackInfoReturnable<EntityAttribute> cir) {
        boolean isModLoaded = net.fabricmc.loader.api.FabricLoader.getInstance().isModLoaded("ranged_weapon_api");
        if (isModLoaded) {
            EntityAttribute rangedDamage = null;
            if (Registries.ATTRIBUTE.get(new Identifier("ranged_weapon:damage")) != null)
                rangedDamage = Registries.ATTRIBUTE.get(new Identifier("ranged_weapon:damage"));
            cir.setReturnValue(rangedDamage);
        } else {
            // Attribute used when Ranged Weapon API is not loaded
            cir.setReturnValue(AttributesMod.RANGED_DAMAGE);
        }
    }

}