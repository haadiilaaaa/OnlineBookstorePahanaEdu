package util.enums;



public enum ItemAction {
    ADD("add"),
    EDIT("edit"),
    UPDATE("update"),      // ← make sure this is here
    DELETE("delete"),
    DEFAULT("default");

    private final String value;
    ItemAction(String value) { this.value = value; }
    public String value() { return value; }

    public static ItemAction from(String input) {
        if (input == null) return DEFAULT;
        for (ItemAction a : values()) {
            if (a.value.equalsIgnoreCase(input)) return a;
        }
        return DEFAULT;
    }
}
