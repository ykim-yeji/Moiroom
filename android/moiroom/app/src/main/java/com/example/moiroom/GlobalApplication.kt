// 네이티브 앱 키 작성을 위한 코드(성현)
// 패키지=애플리케이션이기 때문에 기본적으로는 애플리케이션당 패키지가 하나 뿐이지만
// 따로 설정하는 이유는, kotlin의 특별한 제약/규칙 때문에 하나의 파일이 하나의 패키지에 속해야하기 때문이다.
package com.example.moiroom

import android.app.Application
import androidx.multidex.MultiDexApplication
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.kakao.sdk.common.KakaoSdk
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Converter
import java.lang.reflect.Type
import com.google.firebase.auth.ktx.auth
import com.google.firebase.storage.ktx.storage

// Application: 앱의 진입점에서 실행되는 컴포넌트
class GlobalApplication : Application() {

    // Retrofit: HTTP 통신을 수행할 때 사용하는 인스턴스
    lateinit var retrofit: Retrofit
    lateinit var retrofit2: Retrofit
    lateinit var retrofitInstagram: Retrofit


    // override: 부모 클래스에 정의된 매서드를 자식 클래스에서 동일한 시그니처(이름, 매개변수)로 다시 구현
    // onCreate: 액티비티가 처음 생성될 때/서비스가 시작될 때 호출되는 초기화 매서드
    override fun onCreate() {
        // super: 부모클래스
        super.onCreate()

        // 발급받은 카카오키를 통해 카카오SDK 초기화
        KakaoSdk.init(this, "dafe1201ec34d9c314a0564155c291ba")

        // Retrofit 인스턴스 생성
        retrofit = Retrofit.Builder()
            // API의 기본 URL 설정
            .baseUrl("https://jsonplaceholder.typicode.com/") //더미데이터
            // 서버에서 받은 JSON 데이터를 객체로 변환하기 위한 Gson 컨버터를 Retrofit에 추가
            .addConverterFactory(GsonConverterFactory.create())
            // 위 정보를 바탕으로 Retrofit 인스턴트 생성
            .build()

        // 데이터 전송용
        retrofit2 = Retrofit.Builder()
            .baseUrl("http://i10a308.p.ssafy.io:5000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofitInstagram = Retrofit.Builder()
            .baseUrl("https://api.instagram.com/oauth/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        GlobalApplication.auth = Firebase.auth
        GlobalApplication.db = FirebaseFirestore.getInstance()
        GlobalApplication.storage = Firebase.storage
    }

    companion object {
        lateinit var auth: FirebaseAuth
        var email: String? = null
        lateinit var db: FirebaseFirestore
        lateinit var storage: FirebaseStorage
        fun checkAuth(): Boolean {
            var currentUser = auth.currentUser
            return currentUser?.let {
                email = currentUser.email
                currentUser.isEmailVerified
            } ?: let {
                false
            }
        }
    }
}