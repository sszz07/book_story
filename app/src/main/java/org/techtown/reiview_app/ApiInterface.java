package org.techtown.reiview_app;

import org.techtown.reiview_app.home.home_data;
import org.techtown.reiview_app.record.Record_data;
import org.techtown.reiview_app.search.search_data;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiInterface {
    //유저 만들기
    @FormUrlEncoded
    @POST("book_user_make_insert.php")
    Call<login_data> book_user_make_insert(
            @Field("email") String email,
            @Field("password") String password,
            @Field("nick") String nick
    );


    @FormUrlEncoded
    @POST("book_user_make_nick_certification.php")
    Call<login_data> book_user_make_nick_certification(
            @Field("nick") String nick
    );

    @FormUrlEncoded
    @POST("book_user_make_email_certification.php")
    Call<login_data> book_user_make_email_certification(
            @Field("email") String email
    );

    @FormUrlEncoded
    @POST("book_login.php")
    Call<login_data> book_login(
            @Field("email") String email,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("book_user_gallery_update.php")
    Call<login_data> book_user_gallery_update(
            @Field("email") String email,
            @Field("nick") String nick,
            @Field("image") String image,
            @Field("image_time") String image_time
    );

    @FormUrlEncoded
    @POST("book_user_image_x_update.php")
    Call<login_data> book_user_image_x_update(
            @Field("email") String email,
            @Field("nick") String nick,
            @Field("image") String image
    );


    @FormUrlEncoded
    @POST("book_password_remake.php")
    Call<String> book_password_remake(
            @Field("email") String email,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("book_search_insert.php")
    Call<String> search_info_insert(
            @Field("email") String email,
            @Field("book_name") String book_name
    );


    @GET("book_search_select.php")
    Call<List<search_data>> search_info_select(
            @Query("email") String email
    );

    @GET("search_friends_info_select.php")
    Call<List<login_data>> search_friends_info_select(
            @Query("email") String email
    );

    @GET("search_friends_info_select_paging.php")
    Call<List<login_data>> search_friends_info_select2(
            @Query("email") String email,
            @Query("page") int page,
            @Query("limit") int limit
    );

    @FormUrlEncoded
    @POST("book_search_delete.php")
    Call<search_data> search_info_delete(
            @Field("email") String email,
            @Field("book_name") String book_name
    );


    @GET("ItemSearch.aspx")
    Call<String> string_call(
            @Query("ttbkey") String ttbkey,
            @Query("Query") String Query,
            @Query("page") int page,
            @Query("limit") int limit
    );


    @FormUrlEncoded
    @POST("book_now_read_insert.php")
    Call<String> book_now_read_insert(
            @Field("image") String image,
            @Field("book_subject") String book_subject,
            @Field("make") String make,
            @Field("writer") String writer,
            @Field("book_page") String book_page,
            @Field("email") String email
    );

    @FormUrlEncoded
    @POST("book_now_read_delete.php")
    Call<String> book_now_read_delete(
            @Field("email") String email,
            @Field("book_subject") String book_subject
    );

    @FormUrlEncoded
    @POST("search_info_all_delete.php")
    Call<String> search_info_all_delete(
            @Field("email") String email
    );


    @GET("book_now_read_select.php")
    Call<List<search_data>> book_now_read_select(
            @Query("email") String email,
            @Query("book_subject") String book_subject
    );

    @GET("book_home_recyclerview_select.php")
    Call<List<home_data>> book_home_recyclerview_select(
            @Query("email") String email,
            @Query("choise") String choise
    );


    @FormUrlEncoded
    @POST("memo_info_insert.php")
    Call<String> memo_info_insert(
            @Field("email") String email,
            @Field("book_subject") String book_subject,
            @Field("memo") String memo,
            @Field("time") String time
    );

    @FormUrlEncoded
    @POST("memo_info_delete.php")
    Call<home_data> memo_info_delete(
            @Field("idx") int idx,
            @Field("book_subject") String book_subject
    );

    @GET("memo_recyclerview_select.php")
    Call<List<home_data>> memo_recyclerview_select(
            @Query("email") String email,
            @Query("book_subject") String book_subject,
            @Query("page")
                    int page,
            @Query("limit")
                    int limit
    );

    @GET("book_time_recyclerview_select.php")
    Call<List<home_data>> book_time_recyclerview_select(
            @Query("email") String email,
            @Query("book_subject") String book_subject,
            @Query("page")
                    int page,
            @Query("limit")
                    int limit
    );


    @FormUrlEncoded
    @POST("memo_info_update.php")
    Call<String> memo_info_update(
            @Field("idx") int idx,
            @Field("memo") String memo
    );

    @GET("book_read_page_check.php")
    Call<List<home_data>> book_read_page_check(
            @Query("book_subject") String book_subject
    );

    @FormUrlEncoded
    @POST("read_info_update.php")
    Call<String> read_info_update(
            @Field("book_subject") String book_subject,
            @Field("read_time") int read_time,
            @Field("read_page") int read_page,
            @Field("present_time") long present_time
    );


    @FormUrlEncoded
    @POST("read_date_insert.php")
    Call<String> read_date_insert(
            @Field("book_subject") String book_subject,
            @Field("today_read_time") int today_read_time,
            @Field("email") String email,
            @Field("start_day") String start_day
    );


    @FormUrlEncoded
    @POST("book_want_read_insert.php")
    Call<String> book_want_read_insert(
            @Field("image") String image,
            @Field("book_subject") String book_subject,
            @Field("make") String make,
            @Field("writer") String writer,
            @Field("date") String date,
            @Field("book_content") String book_content,
            @Field("email") String email,
            @Field("book_page") String book_page
    );


    @GET("book_want_read_select.php")
    Call<List<search_data>> book_want_read_select(
            @Query("email") String email,
            @Query("book_subject") String book_subject
    );


    @FormUrlEncoded
    @POST("book_want_read_delete.php")
    Call<String> book_want_read_delete(
            @Field("email") String email,
            @Field("book_subject") String book_subject
    );


    @GET("book_want_recyclerview_select.php")
    Call<List<home_data>> book_want_recyclerview_select(
            @Query("email") String email
    );


    @FormUrlEncoded
    @POST("finish_read_info_update.php")
    Call<String> finish_read_info_update(
            @Field("book_subject") String book_subject,
            @Field("email") String email,
            @Field("book_star") float book_star,
            @Field("my_content") String my_content,
            @Field("finish") String finish,
            @Field("all_time_read_time") int all_time_read_time
    );


    @FormUrlEncoded
    @POST("finish_book.php")
    Call<String> finish_book(
            @Field("book_subject") String book_subject,
            @Field("email") String email,
            @Field("book_star") float book_star,
            @Field("my_content") String my_content,
            @Field("finish") String finish,
            @Field("all_time_read_time") int all_time_read_time,
            @Field("present_time") long present_time,
            @Field("read_page_intent") int read_page_intent
    );


    @GET("book_finish_recyclerview_select.php")
    Call<List<home_data>> book_finish_recyclerview_select(
            @Query("email") String email,
            @Query("choise") String choise
    );


    @GET("setting_memo_recyclerview_select.php")
    Call<List<home_data>> setting_memo_recyclerview_select(
            @Query("email") String email,
            @Query("page")
                    int page,
            @Query("limit")
                    int limit
    );


    @GET("setting_read_time_recyclerview_select.php")
    Call<List<home_data>> setting_read_time_recyclerview_select(
            @Query("email") String email,
            @Query("page")
                    int page,
            @Query("limit")
                    int limit,
            @Query("choise") String choise
    );

    @FormUrlEncoded
    @POST("setting_select_out.php")
    Call<String> setting_select_out(
            @Field("email") String email
    );


    @GET("book_record_select.php")
    Call<List<home_data>> book_record_select(
            @Query("email") String email,
            @Query("record_date") String record_date
    );

    @FormUrlEncoded
    @POST("book_record_insert.php")
    Call<home_data> book_record_insert(
            @Field("image") String image,
            @Field("subject") String subject,
            @Field("email") String email,
            @Field("all_read_time") int all_read_time,
            @Field("record_date") String record_date

    );

    @GET("book_recyclerview_select.php")
    Call<List<home_data>> book_recyclerview_select(
            @Query("email") String email,
            @Query("book_subject") String book_subject
    );

    @FormUrlEncoded
    @POST("book_record_graph_insert.php")
    Call<home_data> book_record_graph_insert(
            @Field("email") String email,
            @Field("all_read_time") int all_read_time,
            @Field("record_date") String record_date

    );

    @FormUrlEncoded
    @POST("book_diary_plus_insert.php")
    Call<home_data> book_diary_plus_insert(
            @Field("book_subject") String book_subject,
            @Field("time") String time,
            @Field("email") String email,
            @Field("book_diary_plus_read_start") String book_diary_plus_read_start,
            @Field("book_diary_plus_read_end") String book_diary_plus_read_end,
            @Field("book_diary_plus_memory") String book_diary_plus_memory,
            @Field("book_diary_plus_think") String book_diary_plus_think
    );

    @GET("book_diary_select.php")
    Call<List<home_data>> book_diary_select(
            @Query("email") String email,
            @Query("book_subject") String book_subject,
            @Query("page")
                    int page,
            @Query("limit")
                    int limit
    );


    @FormUrlEncoded
    @POST("book_diary_delete.php")
    Call<home_data> book_diary_delete(
            @Field("idx") int idx
    );

    @GET("book_diary_select_second.php")
    Call<List<home_data>> book_diary_select_second(
            @Query("idx") int idx
    );

    @FormUrlEncoded
    @POST("book_diary_update.php")
    Call<String> book_diary_update(
            @Field("idx") int idx,
            @Field("book_diary_update_read_start") String book_diary_update_read_start,
            @Field("book_diary_update_read_end") String book_diary_update_read_end,
            @Field("book_diary_update_memory") String book_diary_update_memory,
            @Field("book_diary_update_think") String book_diary_update_think
    );

    @GET("book_record_graph_select.php")
    Call<List<Record_data>> book_record_graph_select(
            @Query("email") String email
    );


    @FormUrlEncoded
    @POST("following_info_insert.php")
    Call<String> following_info_insert(
            @Field("email") String email,
            @Field("other_nick") String other_nick,
            @Field("other_email") String other_email,
            @Field("other_image") String other_image,
            @Field("nick") String nick,
            @Field("image") String image
    );


    @GET("follower_info_select.php")
    Call<List<login_data>> follower_info_select(
            @Query("email") String email,
            @Query("other_email") String other_email
    );


    @FormUrlEncoded
    @POST("following_info_delete.php")
    Call<String> following_info_delete(
            @Field("email") String email,
            @Field("other_email") String other_email
    );


    @GET("search_follwing_info_select.php")
    Call<List<login_data>> search_follwing_info_select(
            @Query("email") String email
    );

    @GET("search_follwing_info_select_paging.php")
    Call<List<login_data>> search_following_info_select2(
            @Query("email") String email,
            @Query("page")
                    int page,
            @Query("limit")
                    int limit
    );


    @GET("search_follwer_info_select.php")
    Call<List<login_data>> search_follwer_info_select(
            @Query("email") String email
    );

    @GET("search_follwer_info_select_paging.php")
    Call<List<login_data>> search_follwer_info_select2(
            @Query("email") String email,
            @Query("page")
                    int page,
            @Query("limit")
                    int limit
    );


    @GET("follow_follwing_info_select.php")
    Call<List<login_data>> follow_follwing_info_select(
            @Query("email") String email
    );
}
