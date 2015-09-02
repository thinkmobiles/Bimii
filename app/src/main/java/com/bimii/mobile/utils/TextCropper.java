package com.bimii.mobile.utils;

public final class TextCropper {

    public static String getNameIgnoreApk(String input){
        return input.replaceAll("\\.apk", "");
    }
}
