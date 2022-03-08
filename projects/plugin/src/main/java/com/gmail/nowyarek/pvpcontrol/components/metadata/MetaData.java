package com.gmail.nowyarek.pvpcontrol.components.metadata;

public enum MetaData {
    ;

    public enum Bypass {
        OP_PROTECTION("pvpcontrol.bypass.op_protection");

        private final String metadataKey;

        Bypass(String metadataKey) {
            this.metadataKey = metadataKey;
        }

        public String value() {
            return this.metadataKey;
        }
    }
}
