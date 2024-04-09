package ch.twidev.spectraldamage.nms.rgb;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;

import java.awt.Color;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author M3II0
 */
public class ColorUtils {

    /*
     *
     * Class Values
     *
     * */

    private static Method COLOR_FROM_CHAT_COLOR;
    private static Method CHAT_COLOR_FROM_COLOR;
    private static final boolean hexSupport;
    private static final Pattern gradient = Pattern.compile("<(#[A-Za-z0-9]{6})>(.*?)</(#[A-Za-z0-9]{6})>");;
    private static final Pattern legacyGradient = Pattern.compile("<(&[A-Za-z0-9])>(.*?)</(&[A-Za-z0-9])>");;
    private static final Pattern rgb = Pattern.compile("&\\{(#......)}");;

    static {
        try {
            COLOR_FROM_CHAT_COLOR = ChatColor.class.getDeclaredMethod("getColor");
            CHAT_COLOR_FROM_COLOR = ChatColor.class.getDeclaredMethod("of", Color.class);
        } catch (NoSuchMethodException e) {
            COLOR_FROM_CHAT_COLOR = null;
            CHAT_COLOR_FROM_COLOR = null;
        }
        hexSupport = CHAT_COLOR_FROM_COLOR != null;
    }

    /*
     *
     * Class API
     *
     * */

    public static String colorize(String text) {
        return colorize(text, '&');
    }

    public static String colorize(String text, char colorSymbol) {
        Matcher g = gradient.matcher(text);
        Matcher l = legacyGradient.matcher(text);
        Matcher r = rgb.matcher(text);
        while (g.find()) {
            Color start = Color.decode(g.group(1));
            String between = g.group(2);
            Color end = Color.decode(g.group(3));
            if (hexSupport) text = text.replace(g.group(0), rgbGradient(between, start, end, colorSymbol));
            else text = text.replace(g.group(0), between);
        }
        while (l.find()) {
            char first = l.group(1).charAt(1);
            String between = l.group(2);
            char second = l.group(3).charAt(1);
            ChatColor firstColor = ChatColor.getByChar(first);
            ChatColor secondColor = ChatColor.getByChar(second);
            if (firstColor == null) firstColor = ChatColor.WHITE;
            if (secondColor == null) secondColor = ChatColor.WHITE;
            if (hexSupport) text = text.replace(l.group(0), rgbGradient(between, fromChatColor(firstColor), fromChatColor(secondColor), colorSymbol));
            else text = text.replace(l.group(0), between);
        }
        while (r.find()) {
            if (hexSupport) {
                ChatColor color = fromColor(Color.decode(r.group(1)));
                text = text.replace(r.group(0), color + "");
            } else {
                text = text.replace(r.group(0), "");
            }
        }
        return ChatColor.translateAlternateColorCodes(colorSymbol, text);
    }

    public static String removeColors(String text) {
        return ChatColor.stripColor(text);
    }

    public static List<Character> charactersWithoutColors(String text) {
        text = removeColors(text);
        final List<Character> result = new ArrayList<>();
        for (char var : text.toCharArray()) {
            result.add(var);
        }
        return result;
    }

    public static List<String> charactersWithColors(String text) {
        return charactersWithColors(text, '§');
    }

    public static List<String> charactersWithColors(String text, char colorSymbol) {
        final List<String> result = new ArrayList<>();
        StringBuilder colors = new StringBuilder();
        boolean colorInput = false;
        boolean reading = false;
        for (char var : text.toCharArray()) {
            if (colorInput) {
                colors.append(var);
                colorInput = false;
            } else {
                if (var == colorSymbol) {
                    if (!reading) {
                        colors = new StringBuilder();
                    }
                    colorInput = true;
                    reading = true;
                    colors.append(var);
                } else {
                    reading = false;
                    result.add(colors.toString() + var);
                }
            }
        }
        return result;
    }

    /*
     *
     * Class Utilities
     *
     * */

    private static String rgbGradient(String text, Color start, Color end, char colorSymbol) {
        final StringBuilder builder = new StringBuilder();
        text = ChatColor.translateAlternateColorCodes(colorSymbol, text);
        final List<String> characters = charactersWithColors(text);
        final double[] red = linear(start.getRed(), end.getRed(), characters.size());
        final double[] green = linear(start.getGreen(), end.getGreen(), characters.size());
        final double[] blue = linear(start.getBlue(), end.getBlue(), characters.size());
        if (text.length() == 1) {
            return fromColor(end) + text;
        }
        for (int i = 0; i < characters.size(); i++) {
            String currentText = characters.get(i);
            ChatColor current = fromColor(new Color((int) Math.round(red[i]), (int) Math.round(green[i]), (int) Math.round(blue[i])));
            builder.append(current).append(currentText.replace("§r", ""));
        }
        return builder.toString();
    }

    private static double[] linear(double from, double to, int max) {
        final double[] res = new double[max];
        for (int i = 0; i < max; i++) {
            res[i] = from + i * ((to - from) / (max - 1));
        }
        return res;
    }

    private static Color fromChatColor(ChatColor color) {
        try {
            return (Color) COLOR_FROM_CHAT_COLOR.invoke(color);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private static ChatColor fromColor(Color color) {
        try {
            return (ChatColor) CHAT_COLOR_FROM_COLOR.invoke(null, color);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    static public final String WITH_DELIMITER = "((?<=%1$s)|(?=%1$s))";

    /**
     * @param text The string of text to apply color/effects to
     * @return Returns a string of text with color/effects applied
     */
    public static String translateColorCodes(String text){

        String[] texts = text.split(String.format(WITH_DELIMITER, "&"));

        StringBuilder finalText = new StringBuilder();

        for (int i = 0; i < texts.length; i++){
            if (texts[i].equalsIgnoreCase("&")){
                //get the next string
                i++;
                if (texts[i].charAt(0) == '#'){
                    finalText.append(ChatColor.of(texts[i].substring(0, 7)) + texts[i].substring(7));
                }else{
                    finalText.append(ChatColor.translateAlternateColorCodes('&', "&" + texts[i]));
                }
            }else{
                finalText.append(texts[i]);
            }
        }

        return finalText.toString();
    }

    public static TextComponent translateColorCodesToTextComponent(String text){

        String[] texts = text.split(String.format(WITH_DELIMITER, "&"));

        ComponentBuilder builder = new ComponentBuilder();

        for (int i = 0; i < texts.length; i++){
            TextComponent subComponent = new TextComponent();
            if (texts[i].equalsIgnoreCase("&")){
                //get the next string
                i++;
                if (texts[i].charAt(0) == '#'){
                    subComponent.setText(texts[i].substring(7));
                    subComponent.setColor(ChatColor.of(texts[i].substring(0, 7)));
                    builder.append(subComponent);
                }else{
                    if (texts[i].length() > 1){
                        subComponent.setText(texts[i].substring(1));
                    }else{
                        subComponent.setText(" ");
                    }

                    switch (texts[i].charAt(0)){
                        case '0':
                            subComponent.setColor(ChatColor.BLACK);
                            break;
                        case '1':
                            subComponent.setColor(ChatColor.DARK_BLUE);
                            break;
                        case '2':
                            subComponent.setColor(ChatColor.DARK_GREEN);
                            break;
                        case '3':
                            subComponent.setColor(ChatColor.DARK_AQUA);
                            break;
                        case '4':
                            subComponent.setColor(ChatColor.DARK_RED);
                            break;
                        case '5':
                            subComponent.setColor(ChatColor.DARK_PURPLE);
                            break;
                        case '6':
                            subComponent.setColor(ChatColor.GOLD);
                            break;
                        case '7':
                            subComponent.setColor(ChatColor.GRAY);
                            break;
                        case '8':
                            subComponent.setColor(ChatColor.DARK_GRAY);
                            break;
                        case '9':
                            subComponent.setColor(ChatColor.BLUE);
                            break;
                        case 'a':
                            subComponent.setColor(ChatColor.GREEN);
                            break;
                        case 'b':
                            subComponent.setColor(ChatColor.AQUA);
                            break;
                        case 'c':
                            subComponent.setColor(ChatColor.RED);
                            break;
                        case 'd':
                            subComponent.setColor(ChatColor.LIGHT_PURPLE);
                            break;
                        case 'e':
                            subComponent.setColor(ChatColor.YELLOW);
                            break;
                        case 'f':
                            subComponent.setColor(ChatColor.WHITE);
                            break;
                        case 'k':
                            subComponent.setObfuscated(true);
                            break;
                        case 'l':
                            subComponent.setBold(true);
                            break;
                        case 'm':
                            subComponent.setStrikethrough(true);
                            break;
                        case 'n':
                            subComponent.setUnderlined(true);
                            break;
                        case 'o':
                            subComponent.setItalic(true);
                            break;
                        case 'r':
                            subComponent.setColor(ChatColor.RESET);
                            break;
                    }

                    builder.append(subComponent);
                }
            }else{
                builder.append(texts[i]);
            }
        }

        return new TextComponent(builder.create());

    }

}