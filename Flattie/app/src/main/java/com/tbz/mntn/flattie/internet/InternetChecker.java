package com.tbz.mntn.flattie.internet;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Check if internet connection active.
 */
public final class InternetChecker {
  private static ConnectivityManager connectivityManager;

  /**
   * Checks if there is an active internet connection.
   * @param context activity which checks the internet connection
   * @return true if connected, false if not
   */
  public static Boolean isInternetConnectionActive(Context context) {
    connectivityManager = (ConnectivityManager) context
        .getSystemService(Context.CONNECTIVITY_SERVICE);
    if (typeConnected(ConnectivityManager.TYPE_MOBILE)
        || typeConnected(ConnectivityManager.TYPE_WIFI)) {
      //we are connected to a network
      return true;
    }
    return false;
  }

  private static Boolean typeConnected(int type) {
    if (connectivityManager.getNetworkInfo(type).getState() == NetworkInfo.State.CONNECTED) {
      return true;
    }
    return false;
  }
}
