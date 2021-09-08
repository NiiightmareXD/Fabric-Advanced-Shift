package net.fabricmc.fas;

import java.io.*;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.server.command.CommandManager;
import net.minecraft.text.LiteralText;
import org.lwjgl.glfw.GLFW;

public class NightmareXDClientMod implements ClientModInitializer {
    File config = new File("FabricAdvancedShift.txt");
    BufferedReader br;
    BufferedWriter bw;

    String st = "350";
    public short delay = 350;

    ClientPlayerEntity playerXD = MinecraftClient.getInstance().player;

    void ConfigUse()
    {
        try {
            if (!config.delete()) {
                System.out.println("(Fabric Advanced Shift) Error Failed To Delete The File Trying Again (I Don't Know Why This Always happen But It Works)");
            }
            DoTheFile();
            Construct();
            bw.write(String.valueOf(delay));
            bw.close();
            if (playerXD != null) {
                playerXD.sendMessage(new LiteralText("§aSuccessfully Saved In The Config§a"), false);
            } else {
                playerXD = MinecraftClient.getInstance().player;
                if (playerXD != null) {
                    playerXD.sendMessage(new LiteralText("§aSuccessfully Saved In The Config§a"), false);
                }
            }

        } catch (IOException e) {
            if (playerXD != null) {
                playerXD.sendMessage(new LiteralText("§4Failed To Save in Config§4"), false);
            }
            System.out.println("(Fabric Advanced Shift) Error Failed To Write To The File");
            e.printStackTrace();
        }
    }
    void Construct() {
        {
            try {
                bw = new BufferedWriter(new FileWriter(config));
            } catch (IOException e) {
                System.out.println("(Fabric Advanced Shift) Error Failed To Create BufferedWriter");
                e.printStackTrace();
            }
        }
    }

    public void DoTheFile() {
        try {
            if (config.createNewFile())
            {
                System.out.println("(Fabric Advanced Shift) New File Created!");
            }
        } catch (IOException e) {
            System.out.println("(Fabric Advanced Shift) Error Failed To Create The File");
            e.printStackTrace();
        }
    }

    public void DoTheFileF() {
        try {
            if (config.createNewFile())
            {
                Construct();
                bw.write("350");
                bw.close();
                System.out.println("(Fabric Advanced Shift) New File Created For The First Time!");
            }
        } catch (IOException e) {
            System.out.println("(Fabric Advanced Shift) Error Failed To Create The File");
            e.printStackTrace();
        }
    }

    KeyBinding ToggleSBMode = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.fas.toggle-speed-bridge-mode", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_K, "category.fas.FabricAdvancedShift"));
    KeyBinding DelayUp = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.fas.Delay-Up", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_O, "category.fas.FabricAdvancedShift"));
    KeyBinding DelayDown = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.fas.Delay-Down", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_I, "category.fas.FabricAdvancedShift"));

    public boolean shifty = false;
    public boolean sb = false;
    @Override
    public void onInitializeClient() {
        DoTheFileF();
        try {
            br = new BufferedReader(new FileReader(config));
        } catch (FileNotFoundException e) {
            System.out.println("(Fabric Advanced Shift) Error Failed To Create BufferedReader");
            e.printStackTrace();
        }

        while (true) {
            try {
                if ((st = br.readLine()) == null) {
                    break;
                }
            } catch (IOException e) {
                System.out.println("(Fabric Advanced Shift) Error Failed To Read From The File");
                e.printStackTrace();
            }
            if (Short.parseShort(st) != 0 && st != null) {
                delay = Short.parseShort(st);
                if (delay == 0)
                {
                    delay = 350;
                }
            } else {
                delay = 350;
            }
        }
        Construct();
        try {
            bw.write(String.valueOf(delay));
            bw.close();
        } catch (IOException e) {
            System.out.println("(Fabric Advanced Shift) Error Failed To Write To The File");
            e.printStackTrace();
        }
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
            dispatcher.register(CommandManager.literal("tempdelay")
                    .then(CommandManager.literal("up")
                            .executes(context -> {
                                delay = (short) (delay + 50);
                                if (playerXD != null) {
                                    playerXD.sendMessage(new LiteralText("§aDelay Is Now §a" + delay + "§a, 50 More Than Before§a"), false);
                                }
                                return 1;
                            })
                    )
                    .then(CommandManager.literal("down")
                            .executes(context -> {
                                delay = (short) (delay - 50);
                                if (playerXD != null) {
                                    playerXD.sendMessage(new LiteralText("§4Delay Is Now §4" + delay + "§4, 50 Less Than Before§4"), false);
                                }
                                return 1;
                            })
                    )
                    .then(CommandManager.literal("default")
                            .executes(context -> {
                                delay = 350;
                                if (playerXD != null) {
                                    playerXD.sendMessage(new LiteralText("§bDelay Is Now §b" + delay + "§b, Back To Default§b"), false);
                                }
                                return 1;
                            })
                    )
            );
            dispatcher.register(CommandManager.literal("delay")
                    .then(CommandManager.literal("up")
                            .executes(context -> {
                                delay = (short) (delay + 50);
                                ConfigUse();
                                if (playerXD != null) {
                                    playerXD.sendMessage(new LiteralText("§aDelay Is Now §a" + delay + "§a, 50 More Than Before§a"), false);
                                }
                                return 1;
                            })
                    )
                    .then(CommandManager.literal("down")
                            .executes(context -> {
                                delay = (short) (delay - 50);
                                ConfigUse();
                                if (playerXD != null) {
                                    playerXD.sendMessage(new LiteralText("§4Delay Is Now §4" + delay + "§4, 50 Less Than Before§4"), false);
                                }
                                return 1;
                            })
                    )
                    .then(CommandManager.literal("default")
                            .executes(context -> {
                                delay = 350;
                                ConfigUse();
                                if (playerXD != null) {
                                    playerXD.sendMessage(new LiteralText("§bDelay Is Now §b" + delay + "§b, Back To Default§b"), false);
                                }
                                return 1;
                            })
                    )
            );
        });
        Stopwatch stopwatch = Stopwatch.createUnstarted();
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (shifty)
            {
                client.options.keySneak.setPressed(true);
            }

            while (client.options.keySneak.wasPressed()) {
                if (!sb) {
                    if (shifty) {
                        if (client.player != null) {
                            client.player.sendMessage(new LiteralText("Toggle Sneak Mode §4Off§4"), true);
                        }
                        shifty = false;
                    }
                    if (stopwatch.isRunning()) {
                        if (stopwatch.elapsed(TimeUnit.MILLISECONDS) > delay) {
                            stopwatch.stop();
                        } else {
                            if (client.player != null) {
                                if (client.player.isOnGround()) {
                                    shifty = true;
                                    if (client.player != null) {
                                        client.player.sendMessage(new LiteralText("Toggle Sneak Mode §aOn§a"), true);
                                    }
                                } else
                                {
                                    if (client.player != null) {
                                        client.player.sendMessage(new LiteralText("§4You Should Be On Ground To Use This Feature§4"), true);
                                    }
                                }
                            }
                        }
                        stopwatch.reset();
                    }
                    stopwatch.start();
                } else {
                    if (shifty) {
                        if (client.player != null) {
                            client.player.sendMessage(new LiteralText("Toggle Sneak Mode §4Off§4"), true);
                        }
                        shifty = false;
                    }
                }
            }

            while (ToggleSBMode.wasPressed()) {
                if (sb)
                {
                    if (client.player != null) {
                        client.player.sendMessage(new LiteralText("Speed bridge Mode §4Off§4"), true);
                    }
                    sb = false;
                } else {
                    if (client.player != null) {
                        client.player.sendMessage(new LiteralText("Speed bridge Mode §aOn§a"), true);
                    }
                    sb = true;
                }
            }

            while (DelayUp.wasPressed()) {
                delay = (short) (delay + 50);
                ConfigUse();
                if (client.player != null) {
                    client.player.sendMessage(new LiteralText("§aDelay Is Now §a" + delay + "§a, 50 More Than Before§a"), false);
                }
            }

            while (DelayDown.wasPressed()) {
                delay = (short) (delay - 50);
                ConfigUse();
                if (client.player != null) {
                    client.player.sendMessage(new LiteralText("§4Delay Is Now §4" + delay + "§4, 50 Less Than Before§4"), false);
                }
            }
        });
    }
}
