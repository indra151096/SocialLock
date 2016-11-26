package sociallockinvite.anything.com.sociallock.Interface;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface SocialLockService {
    @FormUrlEncoded
    @POST("social/sendSocialLock.php")
    Call<ResponseBody> sendLockRequest(@Field("topic") String topic, @Field("message") String message);

    @FormUrlEncoded
    @POST("social/")
    Call<ResponseBody> replyLockRequest();
}
