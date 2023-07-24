package dev.spaxter.curseguard.models;

public enum Action {
    BLOCK,
    CENSOR,
    NONE;

    public static Action fromString(final String str) {
        switch (str.toLowerCase()) {
            case "block":
                return BLOCK;
            case "censor":
                return CENSOR;
            default:
                return NONE;
        }
    }
}
