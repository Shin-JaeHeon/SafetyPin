package kr.saintdev.safetypin.models.datas.subprofile;

/**
 * Copyright (c) 2015-2018 Saint software All rights reserved.
 *
 * @Date 2018-06-10
 */

public class ChildObject {
    private int idx = 0;
    private String childName = null;
    private String childNum = null;
    private String childCode = null;

    public ChildObject(String childName, String childNum, String childCode) {
        this.childName = childName;
        this.childNum = childNum;
        this.childCode = childCode;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public int getIdx() {
        return idx;
    }

    public String getChildName() {
        return childName;
    }

    public String getChildNum() {
        return childNum;
    }

    public String getChildCode() {
        return childCode;
    }
}
