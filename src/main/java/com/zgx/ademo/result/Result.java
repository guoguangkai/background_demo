package com.zgx.ademo.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//Result 类是为了构造 response，主要是响应码。实际上由于响应码是固定的，code 属性应该是一个枚举值，这里作了一些简化。
@Data
@AllArgsConstructor //使用后添加一个构造函数，该构造函数含有所有已声明字段属性参数
@NoArgsConstructor
public class Result {
    //响应码
    private int code;
    private String message;
    private Object data;

    public Result(int code) {
        this.code = code;
    }
}
