package com.mockserver.util;

import com.mockserver.model.enums.ResponseCode;
import com.mockserver.model.shared.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class Utils {

    public static <T> Response<T> successfulResponse(T data) {
        Response<T> response = new Response<>(ResponseCode.SUCCESSFUL.code, ResponseCode.SUCCESSFUL.message);
        response.setData(data);
        return response;
    }

}
