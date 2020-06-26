package me.cunzai;

import net.minecraft.client.Minecraft;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.HoverEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatStyle;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.regex.Pattern;

/**
 * 2 * @Author: EmptyIrony
 * 3 * @Date: 2020/6/26 11:56
 * 4
 */
@Mod(name = "AddOne！", modid = "addone", version = "1.0")
public class AddOne {
    private static final transient String SECTOR_SYMBOL = "§";
    private static final transient String ALL_PATTERN = "[0-9A-FK-ORa-fk-or]";
    private static final transient Pattern VANILLA_PATTERN = Pattern.compile(SECTOR_SYMBOL + "+(" + ALL_PATTERN + ")");
    private boolean first;

    public static String stripColor(String input) {
        return VANILLA_PATTERN.matcher(input).replaceAll("");
    }

    public static String translate(String input) {
        return input.replace("&", "§");
    }

    @Mod.EventHandler
    public void onInstall(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS
                .register(this);
    }

    @SubscribeEvent
    public void onFirstJoinedServer(EntityJoinWorldEvent event) {
        if (!first) {
            Minecraft.getMinecraft()
                    .thePlayer
                    .addChatComponentMessage(new ChatComponentText(translate("&d[AddOne！]&bThis mod is coded by &dEmptyIrony(存在)")));

            first = true;
        }
    }

    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent event) {
        String text = event.message.getFormattedText();
        String need;
        int i = text.indexOf("§f:");
        if (i == -1) {
            i = text.indexOf("§7:");

            if (i == -1) {
                return;
            } else {
                need = "§7:";
            }

        } else {
            need = "§f:";
        }


        String stripColor = stripColor(text);
        int state;

        if (stripColor.startsWith("Guild") || stripColor.startsWith("公会")) {
            state = 1;
        } else if (stripColor.startsWith("Party") || stripColor.startsWith("组队")) {
            state = 2;
        } else if (stripColor.startsWith("Co-op")) {
            state = 3;
        } else if (stripColor.startsWith("Officer")) {
            state = 4;
        } else if (stripColor.startsWith("From")) {
            state = 5;
        } else {
            state = 0;
        }

        String s = stripColor(text.replace(need + " ", "").substring(i));

        ChatComponentText components = new ChatComponentText(translate(" &6[+1]"));
        ChatStyle style = new ChatStyle();
        style.setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText(translate("&7Click to +1!"))));

        switch (state) {
            case 0:
                style.setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, s));
                break;
            case 1:
                style.setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/gc " + s));
                break;
            case 2:
                style.setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/pc " + s));
                break;
            case 3:
                style.setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/cc " + s));
                break;
            case 4:
                style.setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/oc " + s));
                break;
            case 5:
                style.setChatClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/r " + s));
                break;
        }

        components.setChatStyle(style);

        event.message.appendSibling(components);

    }

}
