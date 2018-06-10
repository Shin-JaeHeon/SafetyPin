package kr.saintdev.safetypin.models.datas.profile;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Copyright (c) 2015-2018 Saint software All rights reserved.
 *
 * @Date 2018-06-10
 */

public class MeProfileManager {
    private static MeProfileManager instance = null;
    private SharedPreferences r = null;             // Readonly prep
    private SharedPreferences.Editor w = null;      // Write only prep

    public static MeProfileManager getInstance(Context context) {
        if(MeProfileManager.instance == null) {
            MeProfileManager.instance = new MeProfileManager(context);
        }

        return MeProfileManager.instance;
    }

    private MeProfileManager(Context context) {
        this.r = context.getSharedPreferences(Keys.PROFILE_REPO, Context.MODE_PRIVATE);
        this.w = this.r.edit();
    }

    private boolean isSessionAvailable() {
        if(getStringValue(Keys.SESSION) == null) {
            return false;
        } else {
            return true;
        }
    }

    public MeProfileObject getProfileObject() {
        String session = getStringValue(Keys.SESSION);
        String name = getStringValue(Keys.NAME);

        MeProfileObject profile = null;
        if(session != null && name != null) {
            profile = new MeProfileObject(session, name);
        }

        return profile;
    }

    public void setProfileObject(MeProfileObject profileObject) {
        if(profileObject.getSessionId() != null) {
            setValue(Keys.SESSION, profileObject.getSessionId());
        }

        if(profileObject.getName() != null) {
            setValue(Keys.NAME, profileObject.getName());
        }
    }

    private int getIntValue(String key) {
        return this.r.getInt(key, 0);
    }

    private String getStringValue(String key) {
        return this.r.getString(key, null);
    }

    private void setValue(String key, String value) {
        this.w.putString(key, value);
        this.w.commit();
    }

    private void setValue(String key, int value) {
        this.w.putInt(key, value);
        this.w.commit();
    }

    public interface Keys {
        String PROFILE_REPO = "safetypin";

        String SESSION = "session";
        String NAME = "name";
    }
}
