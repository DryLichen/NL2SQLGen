package com.shrcb.NL2SQLGen.enums;

import lombok.Getter;

@Getter
public enum ResultCode {
    // success
    SUCCESS(0000, "Success"),

    //error
    ERROR(5000, "Unknown error");

    private int code;
    private String msg;

    ResultCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}
