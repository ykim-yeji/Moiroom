package com.example.moiroom.extraction


import android.database.Cursor
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.moiroom.databinding.ActivityJaeeontestBinding
import java.io.File
import com.drew.imaging.ImageMetadataReader
import com.drew.metadata.exif.GpsDirectory
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.core.extensions.jsonBody
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.IOException
import android.content.Context

class PhotoExtract: AppCompatActivity() {
    private lateinit var binding: ActivityJaeeontestBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJaeeontestBinding.inflate(layoutInflater)
//        // 바인딩된 레이아웃의 최상위 뷰를 현재 액티비티의 뷰로 설정
        setContentView(binding.root)
        // 갤러리에서 모든 사진 가져오기
        val photos = getAllPhotos(this)
        binding.textview.text = photos
//        postFuel(photos)
    }
//    fun photoExtract() {
//        val photos = getAllPhotos(this)
//        return postFuel(photos)
//    }

    fun getAllPhotos(context: Context): String {
        val projection = arrayOf(
            MediaStore.Images.Media.DATA,
//            MediaStore.Images.Media.DISPLAY_NAME,
//            MediaStore.Images.Media.DATE_ADDED,
//            MediaStore.Images.Media.IS_DOWNLOAD,
//            MediaStore.Images.Media.IS_FAVORITE,
//            MediaStore.Images.Media.IS_TRASHED,
//            MediaStore.Images.Media.HEIGHT,
//            MediaStore.Images.Media.WIDTH,
//            MediaStore.Images.Media.LONGITUDE,
//            MediaStore.Images.Media.LATITUDE,
        )
        val stringBuilder = StringBuilder()
        stringBuilder.append("[")
        // MediaStore에서 이미지를 가져오는 쿼리
        val cursor: Cursor? = context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            null
        )
        Log.d("커서", "$cursor")
        cursor?.use {
            var number = 1
            val columnIndexData = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            while (it.moveToNext()) {
                val photoPath = it.getString(columnIndexData)
//                val photoDayAdded =
//                    it.getString(it.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_ADDED))
//                val photoIsDownload =
//                    it.getString(it.getColumnIndexOrThrow(MediaStore.Images.Media.IS_DOWNLOAD))
//                val photoIsFavorite =
//                    it.getString(it.getColumnIndexOrThrow(MediaStore.Images.Media.IS_FAVORITE))
//                val photoIsTrashed =
//                    it.getString(it.getColumnIndexOrThrow(MediaStore.Images.Media.IS_TRASHED))
//                val photoHeight =
//                    it.getString(it.getColumnIndexOrThrow(MediaStore.Images.Media.HEIGHT))
//                val photoWidth =
//                    it.getString(it.getColumnIndexOrThrow(MediaStore.Images.Media.WIDTH))
//                val photoLongitude =
//                    it.getString(it.getColumnIndexOrThrow(MediaStore.Images.Media.LONGITUDE))
//                val photoLatitude =
//                    it.getString(it.getColumnIndexOrThrow(MediaStore.Images.Media.LATITUDE))

                val a = readMetadata(photoPath)
//                Log.d("사진", "$photoPath")
//                val a = photoPath
                if (a != "{\"latitude\": null, \"longitude\": null}, "
                    && a != "{\"latitude\": 0.0, \"longitude\": 0.0}, "
                    ) {
                    number += 1
                    stringBuilder.append(a)
                }
//                number += 1
                if (number > 300 ) {
                    break
                }

            }
        }
        if (stringBuilder.length > 1 ) {
            stringBuilder.deleteCharAt(stringBuilder.length - 1)
            stringBuilder.deleteCharAt(stringBuilder.length - 1)
        }
        stringBuilder.append("]")
        return stringBuilder.toString()
    }


    private fun readMetadata(path: String): String {
        val fileScope = path
        val pdsLat: String
        val pdsLon: String

        val file = File(fileScope)
        val stringBuilder = StringBuilder()
        try {
            val metadata = ImageMetadataReader.readMetadata(file)
            val gpsDirectory = metadata.getFirstDirectoryOfType(GpsDirectory::class.java)
            // 위도, 경도 호출
            if (gpsDirectory != null &&
                gpsDirectory.containsTag(GpsDirectory.TAG_LATITUDE) &&
                gpsDirectory.containsTag(GpsDirectory.TAG_LONGITUDE)
            ) {
                pdsLat = gpsDirectory.getGeoLocation().latitude.toString()
                pdsLon = gpsDirectory.getGeoLocation().longitude.toString()
                val lat = pdsLat.toDouble() // 위도
                val lon = pdsLon.toDouble() // 경도
                stringBuilder.append("{\"latitude\": $lat, \"longitude\": $lon}, ")
                return stringBuilder.toString()
            } else {
                return stringBuilder.append("{\"latitude\": null, \"longitude\": null}, ").toString()
            }
        } catch (e: IOException) {
            e.printStackTrace()
            return stringBuilder.append("{\"latitude\": null, \"longitude\": null}, ").toString()
        }
    }

    private fun postFuel(data: String) {
        val stringBuilder = StringBuilder()
        stringBuilder.append("{ \"data\": ")
        stringBuilder.append(data)
        stringBuilder.append("}")
        // FuelManager 설정 (선택사항)
        FuelManager.instance.basePath = "http://i10a308.p.ssafy.io:5000"
        binding.textview.text = stringBuilder.toString()
        // 코루틴 사용
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = Fuel.post("/count_clusters")
                    .header("Content-Type" to "application/json")
                    .jsonBody(
                        stringBuilder.toString()
                    )
                    .responseString()

                // 응답 확인
                response.third.fold(
                    success = { data ->
                        Log.d("서버 응답", "$data")
                    },
                    failure = { error -> Log.d("서버 에러", "에러: $error") }
                )
            } catch (e: Exception) {
                println("에러: $e")
            }
        }
    }
//////////////////////////////////

}

