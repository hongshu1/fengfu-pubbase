package cn.rjtech.enums;

/**
 * Create By ${刘帅} on 2023/4/13.
 */
public enum CreateByEnum {
    CREATE("id",1)
    ;
    private final String text;
    private final long value;

    public String getText() {
        return text;
    }

    public long getValue() {
        return value;
    }

    CreateByEnum(String text, long value) {
        this.text = text;
        this.value = value;
    }
}
