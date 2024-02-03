package cn.mall.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class R<T> implements Serializable {
    Integer code;
    T data;
    String message;

    public R(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public R(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static R success() {
        return new R(200, "success");
    }

    public static R success(String message) {
        return new R(200, message);
    }

    public static R success(String message, Object data) {
        return new R(200, message, data);
    }

    public static R success(Object data) {
        return new R(200, null, data);
    }

    public static R error() {
        return new R(500, "error");
    }

    public static R error(String message) {
        return new R(500, message);
    }

    public static R error(String message, Object data) {
        return new R(500, message, data);
    }

    public static R forbid(String message) {
        return new R(403, message);
    }

}
