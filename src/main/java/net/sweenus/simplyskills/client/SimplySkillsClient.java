package net.sweenus.simplyskills.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.option.StickyKeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.sweenus.simplyskills.util.Abilities;
import org.lwjgl.glfw.GLFW;

public class SimplySkillsClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {

        //Keybindings
        KeyBinding bindingAbility1 = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.simplyskills.ability1", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_Z, "key.category.simplyskills"));
        KeyBinding bindingAbility2 = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.simplyskills.ability2", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_R, "key.category.simplyskills"));
        KeyBinding bindingAbility3 = KeyBindingHelper.registerKeyBinding(new StickyKeyBinding("key.simplyskills.ability3", GLFW.GLFW_KEY_V, "key.category.simplyskills", () -> true));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (bindingAbility1.wasPressed()) {
                client.player.sendMessage(Text.literal("Ability #1 has been used!"), false);
                Abilities.testKeybindPacket();
            }

            while (bindingAbility2.wasPressed()) {
                client.player.sendMessage(Text.literal("Ability #2 has been used!"), false);
            }

            if (bindingAbility3.isPressed()) {
                client.player.sendMessage(Text.literal("Toggle Ability is active"), false);
            }
        });
    }
}
