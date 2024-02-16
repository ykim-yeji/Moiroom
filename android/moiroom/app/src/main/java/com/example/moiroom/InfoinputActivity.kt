package com.example.moiroom

import ApiService
import java.net.URL
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.GridView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.moiroom.adapter.DialogAdapter
import com.example.moiroom.data.City
import com.example.moiroom.data.Metropolitan
import com.example.moiroom.data.MyResponse
import com.example.moiroom.data.RequestBody
import com.example.moiroom.databinding.ActivityInfoinputBinding
import com.example.moiroom.databinding.DialogAuthorityBinding
import com.example.moiroom.databinding.DialogFindCityBinding
import com.example.moiroom.databinding.DialogFindMetropolitanBinding
import fetchUserInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.create
import java.io.File
import java.io.FileOutputStream
import java.util.logging.Logger.global

class InfoinputActivity : AppCompatActivity() {

    // activity_infoinput.xml에 뷰바인딩
    private lateinit var binding: ActivityInfoinputBinding

    // 사용자가 입력한 자기소개 저장
    private var userInput: String = ""

    // 입력 글자 수에 따라 추후 수정
    private var textLength: String = "0/30"

    // 사용자 성별 저장
    private var memberGender: String = "default"

    // ApiService 인스턴스를 저장할 변수
    private lateinit var apiService: ApiService

    private var metropolitans: List<Metropolitan> = listOf()
    private var cities: List<City> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 레이아웃 폴더에 있는 activity_infoinput.xml을 inflate해서 바인딩
        // xml을 실제 뷰 객체로 변환하는 과정
        binding = ActivityInfoinputBinding.inflate(layoutInflater)
        // 바인딩된 레이아웃의 최상위 뷰를 현재 액티비티의 뷰로 설정
        setContentView(binding.root)

        // ApiService 인스턴스 생성
        apiService = NetworkModule.provideRetrofit(this)

        // 입력 글자 수 업데이트
        binding.textLength.text = textLength
        // 사용자 입력 자기소개 저장하기
        binding.editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(
                charSequence: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                userInput = charSequence.toString()
                textLength = "${charSequence?.length ?: 0}/30"
                binding.textLength.text = textLength

                if (charSequence?.length!! > 0) {
                    binding.textCancel.visibility = View.VISIBLE
                } else {
                    binding.textCancel.visibility = View.GONE
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
        // 자기소개 지우기
        binding.textCancel.setOnClickListener {
            binding.editText.text.clear()
        }

        // 성별 선택
        binding.maleCard.setOnClickListener {
            memberGender = "male"

            binding.maleCard.strokeColor = ContextCompat.getColor(this, R.color.sub_yellow)
            binding.maleImage.setColorFilter(ContextCompat.getColor(this, R.color.sub_yellow))
            binding.maleText.setTextColor(ContextCompat.getColor(this, R.color.sub_yellow))

            binding.femaleCard.strokeColor =
                ContextCompat.getColor(this, R.color.gray_high_brightness)
            binding.femaleImage.setColorFilter(
                ContextCompat.getColor(
                    this,
                    R.color.gray_high_brightness
                )
            )
            binding.femaleText.setTextColor(
                ContextCompat.getColor(
                    this,
                    R.color.gray_high_brightness
                )
            )
        }
        binding.femaleCard.setOnClickListener {
            memberGender = "female"

            binding.femaleCard.strokeColor = ContextCompat.getColor(this, R.color.sub_yellow)
            binding.femaleImage.setColorFilter(ContextCompat.getColor(this, R.color.sub_yellow))
            binding.femaleText.setTextColor(ContextCompat.getColor(this, R.color.sub_yellow))

            binding.maleCard.strokeColor =
                ContextCompat.getColor(this, R.color.gray_high_brightness)
            binding.maleImage.setColorFilter(
                ContextCompat.getColor(
                    this,
                    R.color.gray_high_brightness
                )
            )
            binding.maleText.setTextColor(
                ContextCompat.getColor(
                    this,
                    R.color.gray_high_brightness
                )
            )
        }

        lifecycleScope.launch {
//            val response = sendPostRequest()
//            // response가 null이 아니면 로그에 출력
//            response?.let {
//                Log.d("결과", "Not null, POST 성공 - Message: ${it.message}, Status: ${it.status}")
//            }
        }

        binding.findInput.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {

                val response = apiService.getMetropolitan()
                Log.d("결과", "광역시 데이터 요청 결과: $response")

                if (response.isSuccessful) {

                    metropolitans = response.body()?.data ?: emptyList()  // 수정된 부분
                    Log.d("결과", "광역시 데이터: $metropolitans")
                    withContext(Dispatchers.Main) {

                        showMetropolitanSelectDialog(
                            "광역시 선택",
                            metropolitans.map { it.metropolitanName }) { selectedMetropolitanName ->
                            val selectedMetropolitan =
                                metropolitans.find { it.metropolitanName == selectedMetropolitanName }
                            if (selectedMetropolitan != null) {
                                CoroutineScope(Dispatchers.IO).launch {

                                    val cityResponse =
                                        apiService.getCities(selectedMetropolitan.metropolitanId)
                                    Log.d("결과", "군/구 데이터 요청 결과: $cityResponse")
                                    if (cityResponse.isSuccessful) {
                                        cities = cityResponse.body()?.data?.sortedBy { it.cityName }
                                            ?: emptyList()  // 수정된 부분
                                        Log.d("결과", "군/구 데이터: $cities")
                                        withContext(Dispatchers.Main) {

                                            showCitySelectDialog(
                                                "군/구 선택",
                                                cities.map { it.cityName }) { selectedCityName ->
                                                binding.findInputText.text =
                                                    "$selectedMetropolitanName, $selectedCityName"
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        binding.saveButton.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {

                val sharedPreferences = getSharedPreferences("PREFERENCE", Context.MODE_PRIVATE)
                val accessToken = sharedPreferences.getString("accessToken", null)
                val refreshToken = sharedPreferences.getString("refreshToken", null)

                Log.d("결과", "accessToken: $accessToken, refreshToken: $refreshToken")

                // accessToken과 refreshToken이 null인지 검사합니다.
                if (accessToken != null && refreshToken != null) {
                    // 사용자 정보를 가져옵니다.
                    val userInfo = fetchUserInfo(this@InfoinputActivity, accessToken, refreshToken)
                    Log.d("결과", "$userInfo")

                    // 사용자 정보가 null이 아니라면
                    if (userInfo != null) {
                        // metropolitanId와 cityId를 저장할 변수를 선언합니다.
                        var metropolitanId: Long = 0
                        var cityId: Long = 0

                        // 사용자가 선택한 광역시/도와 시/군/구 이름을 저장할 변수를 선언합니다.
                        val selectedLocation = binding.findInputText.text.toString()

                        // selectedLocation을 쉼표로 분리하여 광역시/도 이름과 시/군/구 이름을 가져옵니다.
                        val locationParts = selectedLocation.split(", ")

                        // locationParts에서 광역시/도 이름과 시/군/구 이름을 가져옵니다.
                        val selectedMetropolitanName = locationParts[0]
                        val selectedCityName = locationParts[1]

                        // 광역시/도 리스트에서 사용자가 선택한 광역시/도를 찾습니다.
                        val selectedMetropolitan =
                            metropolitans.find { it.metropolitanName == selectedMetropolitanName }
                        metropolitanId = selectedMetropolitan?.metropolitanId?.toLong() ?: 0

                        // 시/군/구 리스트에서 사용자가 선택한 시/군/구를 찾습니다.
                        val selectedCity = cities.find { it.cityName == selectedCityName }
                        cityId = selectedCity?.cityId?.toLong() ?: 0

                        // 카카오에서 가져온 닉네임을 가져옵니다.
                        val memberNickname: String = userInfo.nickname
                        val memberIntroduction: String = binding.editText.text.toString()

                        val sharedPref = getSharedPreferences("PREFERENCE_NAME", Context.MODE_PRIVATE)
                        with(sharedPref.edit()) {
                            putString("memberGender", memberGender)
                            apply()
                        }

                        val metropolitanIdPart = metropolitanId.toString().toRequestBody(MultipartBody.FORM)
                        val cityIdPart = cityId.toString().toRequestBody(MultipartBody.FORM)
                        val memberGenderPart = memberGender.toRequestBody(MultipartBody.FORM)
                        val memberNicknamePart = memberNickname.toRequestBody(MultipartBody.FORM)
                        val memberIntroductionPart = memberIntroduction.toRequestBody(MultipartBody.FORM)

                        // 이미지 URL을 가져옵니다.
                        val imageUrl: String = userInfo.imageUrl

                        val memberProfileImagePart: MultipartBody.Part = withContext(Dispatchers.IO) {
                            // 이미지 파일을 다운로드합니다.
                            val inputStream = URL(imageUrl).openStream()
                            val downloadedFile = File.createTempFile("downloaded_image", ".png")
                            FileOutputStream(downloadedFile).use { outputStream ->
                                inputStream.copyTo(outputStream)
                            }

                            Log.d("Request Info", "Downloaded image file: ${downloadedFile.absolutePath}")

                            // 다운로드한 파일을 MultipartBody.Part로 만듭니다.
                            val requestFile = downloadedFile
                                .asRequestBody("image/*".toMediaTypeOrNull())
                            val multipartBodyPart = MultipartBody.Part.createFormData("memberProfileImage", downloadedFile.name, requestFile)

                            Log.d("Request Info", "Converted to MultipartBody.Part: $multipartBodyPart")

                            return@withContext multipartBodyPart
                        }


                        Log.d("Request Info", "metropolitanId: $metropolitanId")
                        Log.d("Request Info", "cityId: $cityId")
                        Log.d("Request Info", "memberGender: $memberGender")
                        Log.d("Request Info", "memberNickname: $memberNickname")
                        Log.d("Request Info", "memberIntroduction: $memberIntroduction")
                        Log.d("Request Info", "imageUrl: $imageUrl")

                        val sharedPreferences = getSharedPreferences("PREFERENCE", Context.MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.putLong("metropolitanId", metropolitanId) // 원본 Long 값 저장
                        editor.putLong("cityId", cityId) // 원본 Long 값 저장
                        editor.apply()

                        // 저장된 값 확인
                        val savedMetropolitanId = sharedPreferences.getLong("metropolitanId", -1L)
                        val savedCityId = sharedPreferences.getLong("cityId", -1L)
                        Log.d("Saved Info", "Saved metropolitanId: $savedMetropolitanId")
                        Log.d("Saved Info", "Saved cityId: $savedCityId")

                        val roommateSearchStatusPart = "1".toRequestBody(MultipartBody.FORM)

                        val response = withContext(Dispatchers.IO) {
                            apiService.updateMemberInfo(
                                metropolitanIdPart,
                                cityIdPart,
                                memberGenderPart,
                                memberNicknamePart,
                                memberIntroductionPart,
                                roommateSearchStatusPart,
                                memberProfileImagePart
                            )
                        }

                        Log.d("Response Info", "Response: $response")


                        if (response.isSuccessful) {
                            // 요청이 성공했을 때의 처리
//                            Toast.makeText(
//                                this@InfoinputActivity,
//                                "회원 정보 수정 성공",
//                                Toast.LENGTH_SHORT
//                            ).show()

                            // 요청이 성공했으므로 NowMatchingActivity로 화면을 전환합니다.
                            val intent =
                                Intent(this@InfoinputActivity, NowMatchingActivity::class.java)
                            startActivity(intent)

                        }else {
                            // 요청이 실패했을 때의 처리
                            val errorMsg = response.errorBody()?.string() ?: "Unknown error"
//                            Toast.makeText(
//                                this@InfoinputActivity,
//                                "회원 정보 수정 실패: $errorMsg",
//                                Toast.LENGTH_SHORT
//                            ).show()

                            // 로그에 에러 메시지 출력
                            Log.e("UpdateMemberInfo", "Failed to update member info: $errorMsg")
                        }
                    } else {
                        // 사용자 정보를 가져오지 못한 경우 처리
//                        Toast.makeText(
//                            this@InfoinputActivity,
//                            "사용자 정보를 가져오지 못했습니다",
//                            Toast.LENGTH_SHORT
//                        ).show()
                    }
                } else {
                    // accessToken 또는 refreshToken이 null인 경우 처리
//                    Toast.makeText(this@InfoinputActivity, "토큰 정보를 가져오지 못했습니다", Toast.LENGTH_SHORT)
//                        .show()
                }
            }
        }


    }
        private fun showItemSelectionDialog(
        title: String,
        items: List<String>,
        onItemSelected: (String) -> Unit
    ) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setItems(items.toTypedArray()) { _, which ->
            val selectedItem = items[which]
            onItemSelected(selectedItem)
        }
        builder.show()
    }

    private fun showMetropolitanSelectDialog(
        title: String,
        items: List<String>,
        onItemSelected: (String) -> Unit
    ) {
        val dialog = Dialog(this, R.style.DialogTheme)
        val dialogBinding = DialogFindMetropolitanBinding.inflate(layoutInflater)
        dialog.setContentView(dialogBinding.root)

        val adapter = DialogAdapter(this, items)
        dialogBinding.dataGrid.adapter = adapter

        dialogBinding.dataGrid.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val selectedItem = items[position]
            onItemSelected(selectedItem)
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showCitySelectDialog(
        title: String,
        items: List<String>,
        onItemSelected: (String) -> Unit
    ) {
        val dialog = Dialog(this, R.style.DialogTheme)
        val dialogBinding = DialogFindCityBinding.inflate(layoutInflater)
        dialog.setContentView(dialogBinding.root)

        val adapter = DialogAdapter(this, items)
        dialogBinding.dataGrid.adapter = adapter

        dialogBinding.dataGrid.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val selectedItem = items[position]
            onItemSelected(selectedItem)
            dialog.dismiss()
        }

        dialog.show()
    }

    // 테스트
//    private suspend fun sendPostRequest(): MyResponse? {
//        val globalApplication = application as GlobalApplication
//
//        val apiService = globalApplication.retrofit2.create(ApiInterface::class.java)
//
//        val requestBody = RequestBody(0.1354, 0.3159, 0.7561)
//        Log.d("정보", "$requestBody")
//        return try {
//            val response = apiService.postData(requestBody)
//            Log.d("결과", "POST 성공 - Message: ${response.message}, Status: ${response.status}")
//            response
//        } catch (e: Exception) {
//            Log.e("에러", "POST 요청 보내기 오류", e)
//            null
//        }
//    }
}

