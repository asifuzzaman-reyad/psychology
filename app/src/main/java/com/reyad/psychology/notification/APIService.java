package com.reyad.psychology.notification;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {

    @Headers({
            "Content-Type: Application/json",
            "Authorization:Key=AAAAWflTG2Q:APA91bFur937SekeV3nYosOqwhS8Bly6MKVGAMPd9UXJZJcnQQGHBKEhcwgsaPV2KfnbfLydU-Dt_9SGAsmFMlyL5Ow4TO7JoN1ExWxyhrM_us-RgKThXFSLr6iDSQqh1P8gGxdWbVB0"
    })

    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
