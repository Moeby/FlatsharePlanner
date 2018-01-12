package com.tbz.mntn.flattie.database.dataclasses;

public enum Repeatable {
  NONE, DAILY, WEEKLY, MONTHLY, YEARLY;

  /**
   * Changes an input string into a repeatable value.
   * @param string default: none possible values: daily, weekly, monthly, yearly
   * @return Repeatable
   */
  public static Repeatable toRepeatable(String string) {
    switch (string) {
      case "daily":
        return DAILY;
      case "weekly":
        return WEEKLY;
      case "monthly":
        return MONTHLY;
      case "yearly":
        return YEARLY;
      default:
        return NONE;
    }
  }

  /**
   * Changes an input int into a repeatable value.
   * @param rep default: NONE possible values: 365 = DAILY, 52 WEEKLY, 12 MONTHLY, 1 YEARLY
   * @return Repeatable
   */
  public static Repeatable toRepeatable(int rep) {
    switch (rep) {
      case 365:
        return DAILY;
      case 52:
        return WEEKLY;
      case 12:
        return MONTHLY;
      case 1:
        return YEARLY;
      default:
        return NONE;
    }
  }

  /**
   * Changes current Repeatable into a string.
   * @return string
   */
  @Override
  public String toString() {
    switch (this) {
      case DAILY:
        return "daily";
      case WEEKLY:
        return "weekly";
      case MONTHLY:
        return "monthly";
      case YEARLY:
        return "yearly";
      default:
        return "none";
    }
  }

  /**
   * Changes current Repeatable into a string.
   * @return 0 = NONE, 365 = DAILY, 52 WEEKLY, 12 MONTHLY, 1 YEARLY
   */
  public int toInt() {
    switch (this) {
      case DAILY:
        return 365;
      case WEEKLY:
        return 52;
      case MONTHLY:
        return 12;
      case YEARLY:
        return 1;
      default:
        return 0;
    }
  }
}