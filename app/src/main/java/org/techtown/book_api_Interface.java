package org.techtown;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.techtown.reiview_app.NullOnEmptyConverterFactory;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public class book_api_Interface {
    private static Retrofit retrofit = null;
    private static final String BASE_URL = "https://www.aladin.co.kr/ttb/api/"; //자신의 서버 주소값을 입력한다
    public static Retrofit getClient(){

        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .build();
        if(retrofit ==null){
            Gson gson = new GsonBuilder() //서버에서 받아온 제이슨값을 String 형태로 값을 반환을 하기위함
                    .setLenient()
                    .create();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(new NullOnEmptyConverterFactory())
                    .client(okHttpClient)
                    .build();
        }
        return  retrofit;
    }
}
