package com.example.oo_frontend.Network;

import com.example.oo_frontend.Model.BookRegisterRequest;
import com.example.oo_frontend.Model.BookRegisterResponse;
import com.example.oo_frontend.Model.ScheduleDto;
import com.example.oo_frontend.Model.SearchResultDto;
import com.example.oo_frontend.Model.Signup;
import com.example.oo_frontend.Model.Login;
import com.example.oo_frontend.Model.User;
import com.example.oo_frontend.Model.MyPage;
import com.example.oo_frontend.Model.SaleItem;
import com.example.oo_frontend.Model.PurchaseItem;
import com.example.oo_frontend.Model.FavoriteItem;
import com.example.oo_frontend.Model.ReviewItem;
import com.example.oo_frontend.Model.Recommendation;
import com.example.oo_frontend.Model.Book;
import com.example.oo_frontend.Model.ChatRoom;
import com.example.oo_frontend.Model.ChatMessage;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Path;
import retrofit2.http.Multipart;
import retrofit2.http.Part;
import retrofit2.http.Header;
import com.google.gson.JsonObject;
import retrofit2.http.PATCH;
import retrofit2.http.DELETE;

public interface RetrofitService {

    // ✅ 이메일 중복 확인 (회원가입 1단계)
    @GET("/api/users/check-email")
    Call<Boolean> checkEmail(@Query("email") String email);

    //닉네임 중복확인
    @GET("/api/users/check-name")
    Call<Boolean> checkName(@Query("name") String name);

    // ✅ 회원가입 요청 (회원가입 2단계)
    @POST("/api/users/signup")
    Call<Login> signup(@Body Signup signup);

    // ✅ 로그인 요청
    @POST("/api/users/login")
    Call<Login> login(@Body Map<String, String> loginData);

    // ✅ 마이페이지 정보 조회 (프로필, 카운트, 시간표 등)
    @GET("/api/mypage")
    Call<MyPage> getMyPage(@Header("userId") int userId);

    // ✅ 마이페이지 -> 시간표 업로드
    @POST("/api/schedule")
    Call<Void> createSchedule(@Body ScheduleDto schedule);

    @POST("/api/schedule/upload")
    Call<Void> uploadSchedule(@Header("userId") long userId, @Body List<String> scheduleSummary);
    // ✅ 마이페이지 -> 판매내역
    @GET("/api/sales")
    Call<List<SaleItem>> getSaleHistory(
            @Header("userId") Long userId,
            @Query("status") String status // 필요 없으면 생략 가능
    );

    // ✅ 마이페이지 -> 판매내역 -> 상태 변경 관련
    @PATCH("/api/sales/{bookId}/status")
    Call<Void> updateSaleStatus(
            @Header("userId") Long userId,
            @Path("bookId") Long bookId,
            @Query("status") String status
    );

    // ✅ 마이페이지 -> 구매내역 조회
    @GET("/api/purchases")
    Call<List<PurchaseItem>> getPurchaseHistory(@Header("userId") Long userId);

    // ✅ 마이페이지 -> 찜목록
    @GET("/api/favorites")
    Call<List<FavoriteItem>> getFavoriteList(@Header("userId") Long userId);
    // 찜 추가
    @POST("/api/favorites/{bookId}")
    Call<String> addFavorite(@Path("bookId") long bookId, @Header("userId") long userId);

    // 찜 해제
    @DELETE("/api/favorites/{bookId}")
    Call<String> deleteFavorite(@Path("bookId") long bookId, @Header("userId") long userId);


    // ✅ 마이페이지 -> 리뷰
    @POST("/api/reviews")
    Call<ReviewItem> postReview(@Body ReviewItem review);

    @GET("/api/reviews")
    Call<List<ReviewItem>> getReviews(
            @Query("productId") int productId,
            @Query("sellerId") int sellerId,
            @Query("limit") int limit,
            @Query("offset") int offset,
            @Query("sortBy") String sortBy
    );

    @GET("/api/main")
    Call<JsonObject> getMainPageRaw(@Query("userId") int userId);

    @POST("/api/mypage/schedule")
    Call<Void> postSchedule(@Body String scheduleText);

    @POST("/api/books/register")
    Call<BookRegisterResponse> registerBook(@Body BookRegisterRequest request);


    @GET("/api/books/{productId}")
    Call<Book> getBookDetail(
            @Path("productId") long productId,
            @Header("viewerId") Long viewerId
    );

    @GET("/api/books/search/title")
    Call<List<Book>> searchByTitle(@Query("keyword") String keyword);

    @GET("/api/books/search/professor")
    Call<List<Book>> searchByProfessor(@Query("keyword") String keyword);


    @GET("/api/books")
    Call<List<Book>> getAllBooks();

    @GET("/api/book/price")
    Call<Double> getAveragePrice(@Query("title") String title);

    @GET("/api/books/mine")
    Call<List<Book>> getMyBooks();

    // 카테고리(단과대)별 교재 목록 조회
    @GET("/api/books/by-department")
    Call<List<Book>> getBooksByDepartment(@Query("department") String department);

    // 시간표 기반 추천
    @GET("/api/recommendation/by-schedule")
    Call<List<Book>> getBySchedule(@Query("userId") Long userId);

    // 과목 기반 추천
    @GET("/api/recommendation/by-subject")
    Call<List<Book>> getBySubject(@Query("userId") Long userId);

    // 교수 기반 추천
    @GET("/api/recommendation/by-professor")
    Call<List<Book>> getByProfessor(@Query("userId") Long userId);



    @POST("/api/chatrooms")
    Call<ChatRoom> getChatRoomList(
            @Query("userId") Long userId,
            @Query("bookId") Long bookId
    );

    @GET("/api/chatrooms")
    Call<List<ChatRoom>> getAllChatRooms(@Header("userId") Long userId);

    // 토큰 없이 호출

    // 1:1 채팅 메시지 목록 조회 (userId 헤더 사용)
    @GET("/api/chatrooms/{roomId}/messages")
    Call<List<ChatMessage>> getChatMessages(
            @Path("roomId") Long roomId,
            @Header("userId") Long userId
    );

    // 채팅 메시지 전송
    @POST("/api/chatrooms/{roomId}/messages")
    Call<Void> sendMessageToChatRoom(
            @Header("userId") Long userId,
            @Path("roomId") Long roomId,
            @Body Map<String, String> messageBody
    );

}