package com.example.oo_frontend.Network;

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
    @POST("/api/mypage/schedule")
    Call<Void> uploadSchedule(
            @Header("userId") int userId,
            @Body List<String> scheduleSummary
    );
    // ✅ 마이페이지 -> 판매내역
    @GET("/api/sales")
    Call<List<SaleItem>> getSales(@Header("userId") int userId);

    // ✅ 마이페이지 -> 판매내역 -> 상태 변경 관련
    @PATCH("/api/sales/{bookId}/status")
    Call<Void> updateSaleStatus(
            @Header("userId") int userId,
            @Path("bookId") int bookId,
            @Query("status") String status
    );

    // ✅ 마이페이지 -> 구매내역 조회
    @GET("/api/purchases")
    Call<List<PurchaseItem>> getPurchaseHistory(@Header("userId") int userId);

    // ✅ 마이페이지 -> 찜목록
    @GET("/api/favorites")
    Call<List<FavoriteItem>> getFavoriteList(@Header("userId") int userId);
    @POST("/api/favorites/{bookId}")
    Call<Void> addFavorite(@Header("userId") int userId, @Path("bookId") int bookId);
    @DELETE("/api/favorites/{bookId}") //찜 삭제
    Call<Void> deleteFavorite(
            @Header("userId") int userId,
            @Path("bookId") int bookId
    );

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

    @Multipart
    @POST("/api/books")
    Call<Book> registerBook(
            @Part MultipartBody.Part image,
            @Part("title") RequestBody title,
            @Part("professorName") RequestBody professorName,
            @Part("officialPrice") RequestBody officialPrice,
            @Part("price") RequestBody price,
            @Part("description") RequestBody description,
            @Part("category") RequestBody category,
            @Part("sellerId") RequestBody sellerId
    );

    @GET("/api/books/{productId}")
    Call<Book> getBookDetail(@Path("productId") long productId);

    @GET("/api/books")
    Call<List<Book>> getAllBooks();

    @GET("/api/books/mine")
    Call<List<Book>> getMyBooks();

    @GET("/api/recommendations")
    Call<List<Recommendation>> getBySchedule(@Query("userId") int userId);

    @GET("/chatrooms")
    Call<List<ChatRoom>> getChatRoomList();  // 토큰 없이 호출

    // ✅ 1:1 채팅 메시지 목록 조회 (userId 헤더 사용)
    @GET("/api/chatrooms/{roomId}/messages")
    Call<List<ChatMessage>> getChatMessages(
            @Path("roomId") String roomId,
            @Header("userId") String userId
    );

    // ✅ 채팅 메시지 전송
    @POST("/api/chatrooms/{roomId}/messages")
    Call<Void> sendMessageToChatRoom(
            @Header("userId") String userId,
            @Path("roomId") String roomId,
            @Body Map<String, String> messageBody
    );

}