package kr.saintdev.safetypin.models.datas.profile;

/**
 * Copyright (c) 2015-2018 Saint software All rights reserved.
 *
 * @Date 2018-06-10
 */

public class MeProfileObject {
    private String sessionId = null;
    private String name = null;

    public MeProfileObject(String sessionId, String name) {
        this.sessionId = sessionId;
        this.name = name;
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getName() {
        return name;
    }
}
