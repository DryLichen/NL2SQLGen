package com.shrcb.NL2SQLGen.exceptions;

import com.shrcb.NL2SQLGen.enums.ResultCode;
import lombok.Getter;

@Getter
public class ApiException extends RuntimeException{

    private ResultCode resultCode;
    private String msg;

    public ApiException(ResultCode resultCode, String msg) {
        this.resultCode = resultCode;
        this.msg = msg;
    }

    public ApiException(ResultCode resultCode) {
        this.resultCode = resultCode;
        this.msg = resultCode.getMsg();
    }

}
