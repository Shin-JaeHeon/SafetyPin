package kr.saintdev.safetypin.models.components.lib;

/**
 * Copyright (c) 2015-2018 Saint software All rights reserved.
 *
 * @Date 2018-06-10
 */

public class Functions {
    public static boolean checkEmpty(String[] args) {
        for(String s : args) {
            if(s == null || s.length() == 0) {
                return false;
            }
        }

        return true;
    }
}
