/*
 * Created by Paweł Szumański
 */

package com.pawelszumanski.utils;

public enum UnknownAlbum {
    DEFAULT("Unknown album"),
    PL("Artysta nieznany");

    private String value;
    public String getValue() {
        return value;
    }
    private UnknownAlbum(String value){
        this.value = value;
    }
}
