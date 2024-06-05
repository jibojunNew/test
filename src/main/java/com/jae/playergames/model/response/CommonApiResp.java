package com.jae.playergames.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommonApiResp<T> {

    private int status;

    private String message;

    private T data;

}
