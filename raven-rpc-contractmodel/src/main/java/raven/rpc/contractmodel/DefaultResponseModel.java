package raven.rpc.contractmodel;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class DefaultResponseModel<TData, TCode> implements FullResponseModel<TData, TCode> {

    @Getter
    @Setter
    private TCode code;

    @Getter
    @Setter
    private TData data;

    @Getter
    @Setter
    private List<KeyValue<String, String>> extension;

    @Getter
    @Setter
    private String message;

    //private Class<? extends TCode> codeClass;

    /**
     * 构造函数
     */
    public DefaultResponseModel() {
        extension = new ArrayList<>();
    }

    @Override
    public String getCodeString() {
        if (code instanceof CodeEnum) {
            return ((CodeEnum) code).getValue().toString();
        }

        return code.toString();
    }

}
