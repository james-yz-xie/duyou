package com.tencent.wxcloudrun.config;

import java.util.HashMap;

public final class ApiResponse {

  private Integer code;
  private String errorMsg;
  private Object data;

  private ApiResponse(int code, String errorMsg, Object data) {
    this.code = code;
    this.errorMsg = errorMsg;
    this.data = data;
  }
  
  public Integer getCode() { return code; }
  public String getErrorMsg() { return errorMsg; }
  public Object getData() { return data; }
  
  public static ApiResponse ok() {
    return new ApiResponse(0, "", new HashMap<>());
  }

  public static ApiResponse ok(Object data) {
    return new ApiResponse(0, "", data);
  }

  public static ApiResponse error(String errorMsg) {
    return new ApiResponse(-1, errorMsg, new HashMap<>());
  }
}
