package com.gmail.nowyarek.pvpcontrol.components.permissions;

public enum Permission {
    ;

    public enum Privillages {
        ADMIN("pvpcontrol.privileges.admin"),
        PLAYER("pvpcontrol.privileges.player");

        private final String permission;

        Privillages(String permission) {
            this.permission = permission;
        }

        public String value() {
            return this.permission;
        }
    }

    public enum Commands {
        ASTERISK("pvpcontrol.commands.*"),
        HELP("pvpcontrol.commands.help"),
        RELOAD("pvpcontrol.commands.reload"),
        TOGGLE("pvpcontrol.commands.toggle");

        private final String permission;

        Commands(String permission) {
            this.permission = permission;
        }

        public String value() {
            return this.permission;
        }
    }

    public enum Bypass {
        ASTERISK("pvpcontrol.bypass.*"),
        FLY("pvpcontrol.bypass.fly"),
        COMMANDS("pvpcontrol.bypass.commands"),
        INVISIBILITY("pvpcontrol.bypass.invisibility"),
        TELEPORTS("pvpcontrol.bypass.teleports"),
        CHORUSFRUITS("pvpcontrol.bypass.chorusfruits"),
        ENDERPEARLS("pvpcontrol.bypass.enderpearls");

        private final String permission;

        Bypass(String permission) {
            this.permission = permission;
        }

        public String value() {
            return this.permission;
        }
    }
}
