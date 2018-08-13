/*
 * Created by Paweł Szumański
 */

package com.pawelszumanski.utils;

public enum UnkownArtist {
    DEFAULT("Artist unknown"),
    PL("Artysta nieznany");

    private String value;
    public String getValue() {
        return value;
    }
    private UnkownArtist(String value){
        this.value = value;
    }
}
