package com.jinglitong.springshop;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName ShopRespose
 * @Description TODO
 * @Author zili.zong
 * @Date 2019/1/7 14:26
 * @Version 1.0
 **/
@ApiModel(value = "返回值说明", description = "返回值说明")
public class ShopRespose<T> implements Serializable {
    /**
     * Created by
     */
    private static final long serialVersionUID = -7124170122394232970L;



    public ShopRespose(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }


    /***
     * 标志位 0操作失败，1 成功！
     */
    @ApiModelProperty(value = "状态码201:失败，0:成功", name = "状态码201:失败，0:成功")
    private Integer code;
    /***
     * 描述信息，可以提供给用户
     */
    @ApiModelProperty(value = "提示信息", name = "提示信息")
    private String message;


    /***
     * 附加的数据
     */
    @ApiModelProperty(value = "返回数据", name = "返回数据")

    private T data;


    public ShopRespose() {
        this(0, "");
    }

    public ShopRespose(T t) {
        this(0, "");
        data = t;
    }

    public ShopRespose(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public ShopRespose(ErrorEnum errorEnum) {
        this.code = errorEnum.getCode();
        this.message = errorEnum.getMsg();
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static <T> ShopRespose<T> errorMessage(BindingResult result) {
        List<FieldError> errors = result.getFieldErrors();
        StringBuffer returnMessage = new StringBuffer();
        returnMessage.append("error param：{\n");
        for (FieldError error : errors) {
            returnMessage.append(error.getField());
            returnMessage.append(":");
            returnMessage.append(error.getDefaultMessage());
            returnMessage.append("\n");
        }
        returnMessage.append("}");
        ShopRespose shopRespose = new ShopRespose();
        shopRespose.setMessage(returnMessage.toString());
        shopRespose.setCode(-1);
        return shopRespose;
    }

    @Override
    public String toString() {
        return "ShopRespose{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
