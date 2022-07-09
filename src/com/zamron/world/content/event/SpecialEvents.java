package com.zamron.world.content.event;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class SpecialEvents {
    /*
 * Daily events
 * Handles the checking of the day to represent
 * which event will be active on such day
 */
    public static final int SUNDAY = 1;
    public static final int MONDAY = 2;
    public static final int TUESDAY = 3;
    public static final int WEDNESDAY = 4;
    public static final int THURSDAY = 5;
    public static final int FRIDAY = 6;
    public static final int SATURDAY = 7;
    //public static Object getSpecialDay;
    //Double EXP days
    public static int getDoubleEXPWeekend() {
        return (getDay() == FRIDAY || getDay() == SUNDAY || getDay() == SATURDAY) ? 2 : 1;
    }
    //Finds the day of the week
    public static int getDay() {
        Calendar calendar = new GregorianCalendar();
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    //events for each day
    public static String getSpecialDay() {
        switch (getDay()) {
            case MONDAY:
                return "X2 Vote Points";
            case TUESDAY:
                return "X2 PK Points";
            case WEDNESDAY:
                return "X2 Slayer Points";
            case THURSDAY:
                return "X2 PC Points";
            case FRIDAY:
            case SATURDAY:
            case SUNDAY:
                return "X2 Exp. & Lottery";
        }
        return "X2 Exp. & Lottery";
    }
}
