package com.example.oo_frontend.Network;

import android.content.Context;
import android.widget.Toast;

import com.example.oo_frontend.Model.ScheduleDto;
import com.example.oo_frontend.Model.Login;
import com.example.oo_frontend.Model.MyPage;
import com.example.oo_frontend.Model.Signup;
import com.example.oo_frontend.Model.SaleItem;
import com.example.oo_frontend.Model.PurchaseItem;
import com.example.oo_frontend.Model.FavoriteItem;
import com.example.oo_frontend.Model.ReviewItem;
import com.example.oo_frontend.Model.Book;
import com.example.oo_frontend.Model.Recommendation;
import com.example.oo_frontend.Model.ChatRoom;
import com.example.oo_frontend.Model.ChatMessage;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import com.google.gson.JsonObject;

import android.util.Log;


public class RetrofitHelper {

    private static RetrofitService apiService;

    public static RetrofitService getApiService() {
        if (apiService == null) {
            apiService = com.example.oo_frontend.Network.RetrofitClient.getClient().create(RetrofitService.class);
        }
        return apiService;
    }

    //이메일 중복 확인
    public static void checkEmail(Context context, String email, final com.example.oo_frontend.Network.ApiCallback<Boolean> callback) {
        RetrofitService api = getApiService();

        api.checkEmail(email).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful() && response.body() != null) {
                    boolean isDuplicate = response.body();
                    Log.d("TEST", "이메일 중복 체크 성공: " + isDuplicate);
                    callback.onSuccess(isDuplicate);
                } else {
                    Log.e("TEST", "이메일 중복 체크 실패: 응답 코드 = " + response.code());
                    callback.onFailure("중복 체크 실패: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Log.e("TEST", "이메일 중복 체크 네트워크 오류: " + t.getMessage());
                callback.onFailure("네트워크 오류: " + t.getMessage());
            }
        });
    }

    // ✅ 회원가입
    public static void signup(Context context, Signup signup, final com.example.oo_frontend.Network.ApiCallback<Login> callback) {
        RetrofitService apiService = getApiService();
        Call<Login> call = apiService.signup(signup);

        call.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                    Toast.makeText(context, "회원가입 성공!", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        String error = response.errorBody().string();
                        callback.onFailure("회원가입 실패: " + error);
                    } catch (IOException e) {
                        callback.onFailure("서버 오류: " + e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                callback.onFailure("통신 실패: " + t.getMessage());
            }
        });
    }

    // ✅ 로그인
    public static void login(Context context, String email, String password, final com.example.oo_frontend.Network.ApiCallback<Login> callback) {
        RetrofitService apiService = getApiService();

        Map<String, String> loginData = new HashMap<>();
        loginData.put("email", email);
        loginData.put("password", password);

        apiService.login(loginData).enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure("로그인 실패");
                }
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                callback.onFailure("네트워크 오류: " + t.getMessage());
            }
        });
    }

    // ✅ 마이페이지 조회
    public static void getMyPage(Context context, Long userId, final com.example.oo_frontend.Network.ApiCallback<MyPage> callback) {
        RetrofitService apiService = getApiService();

        apiService.getMyPage(userId).enqueue(new Callback<MyPage>() {
            @Override
            public void onResponse(Call<MyPage> call, Response<MyPage> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure("마이페이지 정보 조회 실패");
                }
            }

            @Override
            public void onFailure(Call<MyPage> call, Throwable t) {
                callback.onFailure("네트워크 오류: " + t.getMessage());
            }
        });
    }

    // ✅ 마이페이지 -> 시간표 업로드
    public static void uploadScheduleItem(Context context, ScheduleDto dto, final ApiCallback<Void> callback) {
        RetrofitService api = getApiService();

        api.createSchedule(dto).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(null);
                    Toast.makeText(context, "시간표 등록 성공!", Toast.LENGTH_SHORT).show();
                } else {
                    callback.onFailure("시간표 등록 실패: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onFailure("네트워크 오류: " + t.getMessage());
            }
        });
    }

    // ✅ 마이페이지 -> 판매 내역 조회
    public static void getSales(Context context, Long userId, final com.example.oo_frontend.Network.ApiCallback<List<SaleItem>> callback) {
        RetrofitService api = getApiService();
        api.getSales(userId).enqueue(new Callback<List<SaleItem>>() {
            @Override
            public void onResponse(Call<List<SaleItem>> call, Response<List<SaleItem>> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure("판매내역 조회 실패");
                }
            }

            @Override
            public void onFailure(Call<List<SaleItem>> call, Throwable t) {
                callback.onFailure("네트워크 오류: " + t.getMessage());
            }
        });
    }

    // ✅ 마이페이지 -> 판매 내역 -> 상태 변경
    public static void updateSaleStatus(Context context, Long userId, Long bookId, String status, final com.example.oo_frontend.Network.ApiCallback<Void> callback) {
        RetrofitService api = getApiService();
        api.updateSaleStatus(userId, bookId, status).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(null);
                    Toast.makeText(context, "거래 상태가 '" + status + "'(으)로 업데이트되었습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    callback.onFailure("상태 변경 실패: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onFailure("서버 오류: " + t.getMessage());
            }
        });
    }

    // ✅ 마이페이지 -> 구매 내역 조회
    public static void getPurchaseHistory(Context context, Long userId, final com.example.oo_frontend.Network.ApiCallback<List<PurchaseItem>> callback) {
        RetrofitService api = getApiService();

        api.getPurchaseHistory(userId).enqueue(new Callback<List<PurchaseItem>>() {
            @Override
            public void onResponse(Call<List<PurchaseItem>> call, Response<List<PurchaseItem>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure("구매내역 조회 실패");
                }
            }

            @Override
            public void onFailure(Call<List<PurchaseItem>> call, Throwable t) {
                callback.onFailure("네트워크 오류: " + t.getMessage());
            }
        });
    }

    // ✅ 마이페이지 -> 찜목록 조회
    public static void getFavorites(Context context, Long userId, final com.example.oo_frontend.Network.ApiCallback<List<FavoriteItem>> callback) {
        RetrofitService api = getApiService();
        api.getFavoriteList(userId).enqueue(new Callback<List<FavoriteItem>>() {
            @Override
            public void onResponse(Call<List<FavoriteItem>> call, Response<List<FavoriteItem>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure("찜 목록 불러오기 실패");
                }
            }

            @Override
            public void onFailure(Call<List<FavoriteItem>> call, Throwable t) {
                callback.onFailure("네트워크 오류: " + t.getMessage());
            }
        });
    }

    // ✅ 마이페이지 -> 찜목록 삭제
    public static void deleteFavorite(Context context, Long userId, Long bookId, final com.example.oo_frontend.Network.ApiCallback<Void> callback) {
        RetrofitService api = getApiService();

        api.deleteFavorite(userId, bookId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(null);
                    Toast.makeText(context, "찜이 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    callback.onFailure("찜 삭제 실패: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onFailure("네트워크 오류: " + t.getMessage());
            }
        });
    }

    // ✅ 마이페이지 -> 리뷰 등록 ❗리뷰 등록은 안 하는데 걍 넣어놈
    public static void postReview(Context context, ReviewItem review, final com.example.oo_frontend.Network.ApiCallback<ReviewItem> callback) {
        RetrofitService api = getApiService();

        api.postReview(review).enqueue(new Callback<ReviewItem>() {
            @Override
            public void onResponse(Call<ReviewItem> call, Response<ReviewItem> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                    Toast.makeText(context, "리뷰 등록 완료!", Toast.LENGTH_SHORT).show();
                } else {
                    callback.onFailure("리뷰 등록 실패: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<ReviewItem> call, Throwable t) {
                callback.onFailure("네트워크 오류: " + t.getMessage());
            }
        });
    }

    // ✅ 마이페이지 -> 리뷰 목록 조회
    public static void getReviews(Context context, int productId, int sellerId, final com.example.oo_frontend.Network.ApiCallback<List<ReviewItem>> callback) {
        RetrofitService api = getApiService();

        api.getReviews(productId, sellerId, 20, 0, "latest").enqueue(new Callback<List<ReviewItem>>() {
            @Override
            public void onResponse(Call<List<ReviewItem>> call, Response<List<ReviewItem>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure("리뷰 조회 실패: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<ReviewItem>> call, Throwable t) {
                callback.onFailure("네트워크 오류: " + t.getMessage());
            }
        });
    }


    public static void fetchMainPageRaw(Context context, int userId, final com.example.oo_frontend.Network.ApiCallback<JsonObject> callback) {
        RetrofitService service = com.example.oo_frontend.Network.RetrofitClient.getClient().create(RetrofitService.class);

        service.getMainPageRaw(userId).enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure("메인페이지 응답 실패: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                callback.onFailure("네트워크 오류: " + t.getMessage());
            }
        });
    }

    public static void fetchAllBooks(Context context, final com.example.oo_frontend.Network.ApiCallback<List<Book>> callback) {
        RetrofitService api = com.example.oo_frontend.Network.RetrofitClient.getClient().create(RetrofitService.class);

        api.getAllBooks().enqueue(new Callback<List<Book>>() {
            @Override
            public void onResponse(Call<List<Book>> call, Response<List<Book>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure("응답 실패: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Book>> call, Throwable t) {
                callback.onFailure("연결 실패: " + t.getMessage());
            }
        });
    }

    public static void fetchBookDetail(Context context, Long productId, final com.example.oo_frontend.Network.ApiCallback<Book> callback) {
        RetrofitService api = com.example.oo_frontend.Network.RetrofitClient.getClient().create(RetrofitService.class);

        api.getBookDetail(productId).enqueue(new Callback<Book>() {
            @Override
            public void onResponse(Call<Book> call, Response<Book> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure("상세 조회 실패: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Book> call, Throwable t) {
                callback.onFailure("네트워크 오류: " + t.getMessage());
            }
        });
    }

    public static File getFileFromUri(Context context, Uri uri) throws IOException {
        String fileName = null;
        Cursor cursor = null;
        try {
            String[] projection = { MediaStore.Images.Media.DISPLAY_NAME };
            cursor = context.getContentResolver().query(uri, projection, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME);
                fileName = cursor.getString(columnIndex);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        File directory = new File(context.getFilesDir(), "uploads");
        if (!directory.exists()) {
            directory.mkdirs();
        }

        File outputFile = new File(directory, fileName);

        InputStream inputStream = context.getContentResolver().openInputStream(uri);
        if (inputStream == null) {
            throw new IOException("URI에서 InputStream을 열 수 없습니다: " + uri);
        }

        FileOutputStream outputStream = new FileOutputStream(outputFile);
        byte[] buffer = new byte[4 * 1024];
        int read;
        while ((read = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, read);
        }
        outputStream.flush();
        outputStream.close();
        inputStream.close();

        return outputFile;
    }

    public static void getBookDetail(Context context, long productId, com.example.oo_frontend.Network.ApiCallback<Book> callback) {
        RetrofitService service = com.example.oo_frontend.Network.RetrofitClient.getClient().create(RetrofitService.class);
        Call<Book> call = service.getBookDetail(productId);

        call.enqueue(new Callback<Book>() {
            @Override
            public void onResponse(Call<Book> call, Response<Book> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure("응답 실패");
                }
            }

            @Override
            public void onFailure(Call<Book> call, Throwable t) {
                callback.onFailure("네트워크 오류: " + t.getMessage());
            }
        });
    }

    // ✅ 교재 등록 (POST /api/books)
    public static void registerBook(Context context, File imageFile, String title, String professor,
                                    int officialPrice, int price, String description, String category, long sellerId,
                                    final com.example.oo_frontend.Network.ApiCallback<Book> callback) {

        RetrofitService service = com.example.oo_frontend.Network.RetrofitClient.getClient().create(RetrofitService.class);

        // 이미지 파일 -> MultipartBody.Part
        RequestBody reqImage = RequestBody.create(MediaType.parse("image/*"), imageFile);
        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("image", imageFile.getName(), reqImage);

        // 텍스트 필드 -> RequestBody
        RequestBody titlePart = RequestBody.create(MediaType.parse("text/plain"), title);
        RequestBody professorPart = RequestBody.create(MediaType.parse("text/plain"), professor);
        RequestBody officialPricePart = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(officialPrice));
        RequestBody pricePart = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(price));
        RequestBody descriptionPart = RequestBody.create(MediaType.parse("text/plain"), description);
        RequestBody categoryPart = RequestBody.create(MediaType.parse("text/plain"), category);
        RequestBody sellerIdPart = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(sellerId));

        Call<Book> call = service.registerBook(
                imagePart, titlePart, professorPart, officialPricePart,
                pricePart, descriptionPart, categoryPart, sellerIdPart
        );

        call.enqueue(new Callback<Book>() {
            @Override
            public void onResponse(Call<Book> call, Response<Book> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure("서버 응답 오류: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Book> call, Throwable t) {
                callback.onFailure("네트워크 실패: " + t.getMessage());
            }
        });
    }

    public static void fetchRecommendations(Context context, int userId, final com.example.oo_frontend.Network.ApiCallback<List<Recommendation>> callback) {
        RetrofitService service = com.example.oo_frontend.Network.RetrofitClient.getClient().create(RetrofitService.class);
        Call<List<Recommendation>> call = service.getBySchedule(userId);

        call.enqueue(new Callback<List<Recommendation>>() {
            @Override
            public void onResponse(Call<List<Recommendation>> call, Response<List<Recommendation>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure("응답 실패: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<Recommendation>> call, Throwable t) {
                callback.onFailure("네트워크 오류: " + t.getMessage());
            }
        });
    }

    // ✅ 채팅방 목록 불러오기
    public static void fetchChatRooms(Context context, Long userId, final ApiCallback<List<ChatRoom>> callback) {
        RetrofitService service = RetrofitClient.getClient().create(RetrofitService.class);
        Call<List<ChatRoom>> call = service.getChatRoomList(userId);

        call.enqueue(new Callback<List<ChatRoom>>() {
            @Override
            public void onResponse(Call<List<ChatRoom>> call, Response<List<ChatRoom>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure("응답 실패: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<ChatRoom>> call, Throwable t) {
                callback.onFailure("네트워크 오류: " + t.getMessage());
            }
        });
    }

    // ✅ 채팅 메시지 목록 불러오기
    public static void fetchChatMessages(Context context, Long roomId, Long userId,
                                         ApiCallback<List<ChatMessage>> callback) {
        RetrofitService service = RetrofitClient.getClient().create(RetrofitService.class);
        service.getChatMessages(roomId, userId).enqueue(new Callback<List<ChatMessage>>() {
            @Override
            public void onResponse(Call<List<ChatMessage>> call, Response<List<ChatMessage>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure("조회 실패: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<ChatMessage>> call, Throwable t) {
                callback.onFailure("네트워크 오류: " + t.getMessage());
            }
        });
    }

    // ✅ 채팅 메시지 전송 (수정됨)
    public static void sendChatMessage(Context context, Long userId, Long roomId, String messageText,
                                       final ApiCallback<Void> callback) {
        RetrofitService service = RetrofitClient.getClient().create(RetrofitService.class);

        Map<String, String> body = new HashMap<>();
        body.put("message", messageText);

        service.sendMessageToChatRoom(userId, roomId, body).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess(null);
                } else {
                    callback.onFailure("전송 실패: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                callback.onFailure("네트워크 오류: " + t.getMessage());
            }
        });
    }


}
