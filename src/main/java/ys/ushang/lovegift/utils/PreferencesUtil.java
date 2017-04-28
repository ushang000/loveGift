package ys.ushang.lovegift.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.Map;

public class PreferencesUtil
{
    private Context context;
    private SharedPreferences preferences = null;
    private Editor editor = null;
    private static PreferencesUtil util = null;

    private PreferencesUtil(Context context) {
        this.context = context;
    }

    public static PreferencesUtil getInstance(Context ctx) {
        if (util == null)
            util = new PreferencesUtil(ctx);
        return util;
    }

    public void saveOnlyParameters(String name, String key, String value)
    {
        this.preferences = this.context.getSharedPreferences(name, 32768);
        this.editor = this.preferences.edit();
        this.editor.putString(key, value);

        this.editor.commit();
    }

    public void saveOnlyPosition(String name, String key, int position)
    {
        this.preferences = this.context.getSharedPreferences(name, 32768);
        this.editor = this.preferences.edit();
        this.editor.putInt(key, position);
        this.editor.commit();
    }

    public Map<String, ?> getParameterPreferences(String name)
    {
        this.preferences = this.context.getSharedPreferences(name, 0);
        return this.preferences.getAll();
    }

    public Map<String, Integer> getParameterPosition(String name)
    {
        Map result = null;
        this.preferences = this.context.getSharedPreferences(name, 0);
        result = this.preferences.getAll();
        return result;
    }

    public String getParameterSharePreference(String key, String name)
    {
        Map imageMap = getParameterPreferences(name);
        if ((imageMap != null) && (!imageMap.isEmpty())) {
            if (imageMap.containsKey(key)) {
                if (imageMap.get(key) != null) {
                    return imageMap.get(key).toString();
                }
                return null;
            }
            return null;
        }
        return null;
    }

    public Object getPosition(String key, String name)
    {
        Map imageMap = getParameterPreferences(name);
        if ((imageMap != null) && (!imageMap.isEmpty())) {
            return imageMap.get(key);
        }
        return null;
    }

    public boolean getShare() {
        return false;
    }
}
