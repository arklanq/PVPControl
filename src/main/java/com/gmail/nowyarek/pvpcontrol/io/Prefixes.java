package com.gmail.nowyarek.pvpcontrol.io;

public class Prefixes {
    public static String PLUGIN = "§7[§2PVP§aControl§7] ";
    public static String ERROR = "§4";
    public static String WARN = "§cHey! §e";
    public static String LOG = "§7";
    public static String INFO = "§2";
    public static String SUCCESS = "§a";
    public static String DEBUG = "§3";

    // Not static to hide from usage on class
    public void update() {
        Prefixes.PLUGIN = Localization.translate(Text.PLUGIN_PREFIX);
        Prefixes.ERROR = Localization.translate(Text.ERROR_PREFIX);
        Prefixes.WARN = Localization.translate(Text.WARNING_PREFIX);
        Prefixes.LOG = Localization.translate(Text.LOG_PREFIX);
        Prefixes.INFO = Localization.translate(Text.INFO_PREFIX);
        Prefixes.SUCCESS = Localization.translate(Text.SUCCESS_PREFIX);
        Prefixes.DEBUG = Localization.translate(Text.DEBUG_PREFIX);
    }

    public static String getForOutputType(OutputType outputType) {
        switch(outputType) {
            case LOG: {
                return Prefixes.PLUGIN + Prefixes.LOG;
            }
            case DEBUG: {
                return Prefixes.PLUGIN + Prefixes.DEBUG;
            }
            case ERROR: {
                return Prefixes.PLUGIN + Prefixes.ERROR;
            }
            case WARNING: {
                return Prefixes.PLUGIN + Prefixes.WARN;
            }
            case INFO: {
                return Prefixes.PLUGIN + Prefixes.INFO;
            }
            case ANNOUCEMENT: {
                return Prefixes.PLUGIN + Prefixes.SUCCESS;
            }
            default: {
                throw new IllegalArgumentException();
            }
        }
    }
}
