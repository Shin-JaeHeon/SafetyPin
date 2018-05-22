package kr.saintdev.safetypin.models.tasks.http;

import android.content.Context;

import java.util.HashMap;
import java.util.Iterator;


import kr.saintdev.safetypin.models.tasks.BackgroundWork;
import kr.saintdev.safetypin.models.tasks.OnBackgroundWorkListener;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by yuuki on 18. 4. 22.
 */

public class HttpRequester extends BackgroundWork<HttpResponseObject> {
    private String url = null;
    private HashMap<String, Object> param = null;
//    private MeProfiler me = null;
//    private KakaoLoginManager kakaoLogin = null;

    public HttpRequester(String url, HashMap<String, Object> args, int requestCode, OnBackgroundWorkListener listener, Context context) {
        super(requestCode, listener);
        this.url = url;
        this.param = args;

        // 인증서 관련 데이터를 가지고 있다.
//        this.me = MeProfiler.getInstance(context);
//        this.kakaoLogin = KakaoLoginManager.getInstance(context);
    }

    @Override
    protected HttpResponseObject script() throws Exception {
        OkHttpClient client = new OkHttpClient();
        FormBody.Builder reqBuilder = new FormBody.Builder();

        // 사용자 인증 값을 넣습니다.
//        KakaoLoginObject kakaoLoginObject = this.kakaoLogin.getKakaoLoginObject();
//        MeProfileObject profileObj = this.me.getCertification();

//        if(kakaoLoginObject == null) {
//            // 인증서가 없네요.
//            throw new Exception("Unknown Certificate.");
//        }
//
//        reqBuilder.add("X-kakao-id", kakaoLoginObject.getKakaoID());
//        if(profileObj != null) {
//            reqBuilder.add("X-user-uuid", profileObj.getUserKey());
//        } else {
//            reqBuilder.add("X-user-uuid", "");
//        }

        // 인자 값이 있다면 넣어줍니다.
        if(param != null) {
            Iterator keyIterator = param.keySet().iterator();

            while (keyIterator.hasNext()) {
                String key = (String) keyIterator.next();
                Object value = param.get(key);

                if(value == null) {
                    reqBuilder.add(key, "null");
                } else {
                    reqBuilder.add(key, value.toString());
                }
            }
        }

        RequestBody reqBody = reqBuilder.build();
        Request request = new Request.Builder().url(this.url).post(reqBody).build();

        Response response = client.newCall(request).execute();
        String jsonScript = response.body().string();

        HttpResponseObject responseObj = new HttpResponseObject(jsonScript);

        // 응답 완료
        response.close();

        return responseObj;
    }
}
