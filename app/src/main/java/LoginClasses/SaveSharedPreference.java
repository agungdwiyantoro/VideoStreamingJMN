package LoginClasses;

import android.content.Context;
import android.content.SharedPreferences;

import android.preference.PreferenceManager;

/**
 * Created by My Computer on 6/17/2016.
 */
public class SaveSharedPreference {

    private static final String countRowsKey = "channels";
    private static final String countRowsCateogiresKey = "categories";
    private static final String countRowsIklanKey = "iklan";
    private static final String checkAnimasi = "booleanLength";
    private static final String checkStatus = "status";

    static SharedPreferences getSharedPreference(Context ctx){
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    public static void setChannel(Context ctx, String link, String marks){
        SharedPreferences.Editor editor = getSharedPreference(ctx).edit();
        editor.putString(marks, link);
        editor.apply();
    }

    public static String getChannel(Context ctx, String marks){
        return getSharedPreference(ctx).getString(marks,"channel link");
    }

    public static void setChannelName(Context ctx, String name,String key){
        SharedPreferences.Editor editor = getSharedPreference(ctx).edit();
       // editor.clear();
        editor.putString(key, name);
        editor.apply();
    }

    public static String getChannelName(Context ctx, String marks){
        return getSharedPreference(ctx).getString(marks,"channel name");
    }


    public static void setIconURL(Context ctx, String name,String key){
        SharedPreferences.Editor editor = getSharedPreference(ctx).edit();
        editor.putString(key, name);
        editor.apply();
    }

    public static String getIconURL(Context ctx,String key){
        return getSharedPreference(ctx).getString(key,null);
    }

    public static void setCountRows(Context ctx, int count){
        SharedPreferences.Editor red = getSharedPreference(ctx).edit();
        //   red.clear();
        red.putInt(countRowsKey, count);
        red.apply();
    }



    public static int getCountRows(Context ctx){
        return getSharedPreference(ctx).getInt(countRowsKey,0);
    }



    public static void setCountRowsIklan(Context ctx, int count){
        SharedPreferences.Editor red = getSharedPreference(ctx).edit();
        red.putInt(countRowsIklanKey, count);
        red.apply();
    }



    public static int getCountRowsIklan(Context ctx){
        return getSharedPreference(ctx).getInt(countRowsIklanKey,0);
    }

    public static void setAnimasi(Context ctx, boolean boel){
        SharedPreferences.Editor red = getSharedPreference(ctx).edit();
        red.putBoolean(checkAnimasi, boel);
        red.apply();
    }



    public static boolean getAnimasi(Context ctx){
        return getSharedPreference(ctx).getBoolean(checkAnimasi,true);
    }


    public static void setStatus(Context ctx, boolean boel){
        SharedPreferences.Editor red = getSharedPreference(ctx).edit();
        red.putBoolean(checkStatus, boel);
        red.apply();
    }



    public static boolean getStatus(Context ctx){
        return getSharedPreference(ctx).getBoolean(checkStatus,false);
    }



    public static void setCategories(Context ctx, String name,String key){
        SharedPreferences.Editor editor = getSharedPreference(ctx).edit();
        editor.putString(key, name);
        editor.apply();
    }

    public static String getCategories(Context ctx,String key){
        return getSharedPreference(ctx).getString(key,null);
    }


    public static void setCategory(Context ctx, String link, String marks){
        SharedPreferences.Editor editor = getSharedPreference(ctx).edit();
        editor.putString(marks, link);
        editor.apply();
    }

    public static String getCategory(Context ctx, String marks){
        return getSharedPreference(ctx).getString(marks,"NO CATEGORY");
    }

    public static void setCategoryURL(Context ctx, String link, String marks){
        SharedPreferences.Editor editor = getSharedPreference(ctx).edit();
        editor.putString(marks, link);
        editor.apply();
    }


    public static void setIklanIconURL(Context ctx, String link, String marks){
        SharedPreferences.Editor editor = getSharedPreference(ctx).edit();
        editor.putString(marks, link);
        editor.apply();
    }


    public static void setIklanLinkURL(Context ctx, String link, String marks){
        SharedPreferences.Editor editor = getSharedPreference(ctx).edit();
        editor.putString(marks, link);
        editor.apply();
    }

    public static String getIklanLinkURL(Context ctx, String marks){
        return getSharedPreference(ctx).getString(marks,"NO URL LINK IKLAN");
    }


    public static void setStatusVIP(Context ctx, String status, String marks){
        SharedPreferences.Editor editor = getSharedPreference(ctx).edit();
        editor.putString(marks, status);
        editor.apply();
    }

    public static String getStatusVIP(Context ctx, String marks){
        return getSharedPreference(ctx).getString(marks,"STATUS");
    }

    public static void setTempCountRows(Context ctx, int count){
        SharedPreferences.Editor red = getSharedPreference(ctx).edit();
        //   red.clear();
        red.putInt("TEMP COUNT ROWS", count);
        red.apply();
    }



    public static int getTempCountRows(Context ctx){
        return getSharedPreference(ctx).getInt("TEMP COUNT ROWS",0);
    }
}
