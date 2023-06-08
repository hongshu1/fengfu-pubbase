package cn.rjtech.admin.scheduproductplan;

import com.jfinal.log.Log;

public enum Weekday {

    /**
     * 枚举列表
     */
    mon(1, "mon"),
    tue(1, "tue"),
    wed(1, "wed"),
    thu(1, "thu"),
    fri(1, "fri"),
    sat(4, "sat"),
    sun(5, "sun");

    private final int code;
    private final String name;

    Weekday(int code, String name) {
        this.code = code;
        this.name = name;
    }

    private String getName() {
        return name;
    }

    private int getCode() {
        return code;
    }

    public static int catchName(Weekday wed) {
        int result = -1;
        for (Weekday weekday : values()) {
            if (weekday.getName().equals(wed.getName())) {
                result = weekday.getCode();
                break;
            }
        }
        return result;
    }

}
