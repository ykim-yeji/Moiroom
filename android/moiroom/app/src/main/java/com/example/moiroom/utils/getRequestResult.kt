package com.example.moiroom.utils

import android.util.Log
import android.util.LruCache
import com.example.moiroom.data.Interest
import com.example.moiroom.data.MatchedMember
import com.example.moiroom.data.MatchedMemberList
import com.example.moiroom.data.Member

val cacheSize = 4 * 1024 * 1024 // 4MB
val cacheUserInfo = LruCache<String, Member>(cacheSize)
val cacheMatchedMemberList = LruCache<String, MatchedMemberList>(cacheSize)

var okCount: Int = 0

fun getRequestResult(result: Boolean) {
    // ok response를 받아야 하는 갯수
    val targetCount: Int = 1
    Log.d("TAG", "getRequestResult: $result 응답 받음! okCount: $okCount")

    if (result) {
        okCount += 1
        if (okCount == targetCount) {
            // 매칭 리스트 GET 요청
            Log.d("TAG", "getRequestResult: 매칭리스트 요청 보내기!")

            getUserInfo()
            getMatchedMember()
        }
    }
}

fun getMatchedMember() {
    // 응답 데이터 저장 (현재: 더미 데이터)
    val responseMatchedMemberList: MatchedMemberList = MatchedMemberList(
        listOf(
            MatchedMember(
                2,
                "https://images.dog.ceo/breeds/labrador/n02099712_7866.jpg",
                "모이루미",
                "여자",
                2000,
                "서울특별시",
                "강남구",
                "운동을 좋아해서 같이 운동할 수 있는 분이 좋아요.",
                98,
                "비슷한 생활루틴을 가졌어요!",
                5123,
                6656,
                8634,
                4156,
                3223,
                1278,
                6723,
                3534,
                "23:40",
                "06:30",
                listOf(
                    Interest(
                        "운동",
                        48
                    ),
                    Interest(
                        "음악",
                        36
                    ),
                    Interest(
                        "요리",
                        11
                    ),
                    Interest(
                        "게임",
                        5
                    )
                )
            ),
            MatchedMember(
                3,
                "https://images.dog.ceo/breeds/pembroke/n02113023_7275.jpg",
                "모이라",
                "여자",
                1998,
                "서울특별시",
                "강남구",
                "반가워요. 잘 살고 싶어요.",
                90,
                "취미가 비슷해요!",
                4423,
                8156,
                8034,
                2456,
                1187,
                7545,
                3634,
                3565,
                "00:10",
                "08:38",
                listOf(
                    Interest(
                        "음악",
                        60
                    ),
                    Interest(
                        "그림",
                        25
                    ),
                    Interest(
                        "여행",
                        15
                    )
                )
            ),
            MatchedMember(
                4,
                "https://images.dog.ceo/breeds/malamute/n02110063_15580.jpg",
                "모바모바일",
                "여자",
                1998,
                "서울특별시",
                "강남구",
                "ㅎㅇㅎㅇ~~",
                88,
                "취미가 비슷해요!",
                5423,
                8956,
                7934,
                6456,
                5187,
                6545,
                6634,
                2565,
                "00:10",
                "08:38",
                listOf(
                    Interest(
                        "음악",
                        60
                    ),
                    Interest(
                        "그림",
                        25
                    ),
                    Interest(
                        "여행",
                        15
                    )
                )
            ),
            MatchedMember(
                5,
                "https://images.dog.ceo/breeds/samoyed/n02111889_6625.jpg",
                "모란드",
                "여자",
                1996,
                "서울특별시",
                "강남구",
                "안녕하세요!!",
                84,
                "잘 맞을 것 같은데요???!",
                6423,
                7956,
                7034,
                7456,
                7187,
                5545,
                6934,
                6565,
                "00:30",
                "07:38",
                listOf(
                    Interest(
                        "음악",
                        60
                    ),
                    Interest(
                        "그림",
                        25
                    ),
                    Interest(
                        "여행",
                        15
                    )
                )
            ),
            MatchedMember(
                6,
                "https://images.dog.ceo/breeds/komondor/n02105505_515.jpg",
                "모드리치",
                "여자",
                1992,
                "서울특별시",
                "강남구",
                "모이룸조아",
                81,
                "취미가 비슷해요!",
                5823,
                8256,
                4934,
                3456,
                7187,
                5545,
                7634,
                4565,
                "00:20",
                "08:10",
                listOf(
                    Interest(
                        "음악",
                        60
                    ),
                    Interest(
                        "그림",
                        25
                    ),
                    Interest(
                        "여행",
                        15
                    )
                )
            ),
            MatchedMember(
                7,
                "https://images.dog.ceo/breeds/havanese/00100trPORTRAIT_00100_BURST20191112123933390_COVER.jpg",
                "모라이스",
                "여자",
                2000,
                "서울특별시",
                "강남구",
                "여행을 아주 좋아합니다",
                79,
                "취미가 비슷해요!",
                4423,
                6956,
                4934,
                8456,
                7187,
                6545,
                7334,
                4565,
                "00:04",
                "08:38",
                listOf(
                    Interest(
                        "음악",
                        60
                    ),
                    Interest(
                        "그림",
                        25
                    ),
                    Interest(
                        "여행",
                        15
                    )
                )
            ),
            MatchedMember(
                8,
                "https://images.dog.ceo/breeds/sheepdog-english/n02105641_6875.jpg",
                "모버릭",
                "여자",
                1999,
                "서울특별시",
                "강남구",
                "룸메 찾는 중이여요",
                77,
                "취미가 비슷해요!",
                4423,
                7956,
                7734,
                5456,
                5587,
                5545,
                6634,
                3565,
                "00:10",
                "08:38",
                listOf(
                    Interest(
                        "음악",
                        60
                    ),
                    Interest(
                        "그림",
                        25
                    ),
                    Interest(
                        "여행",
                        15
                    )
                )
            ),
            MatchedMember(
                9,
                "https://images.dog.ceo/breeds/ovcharka-caucasian/IMG_20190628_144843.jpg",
                "모소리",
                "여자",
                2001,
                "서울특별시",
                "강남구",
                "안녀하에여",
                75,
                "취미가 비슷해요!",
                5423,
                8956,
                7934,
                6456,
                5187,
                6545,
                6634,
                2565,
                "00:10",
                "08:38",
                listOf(
                    Interest(
                        "음악",
                        60
                    ),
                    Interest(
                        "그림",
                        25
                    ),
                    Interest(
                        "여행",
                        15
                    )
                )
            ),
            MatchedMember(
                10,
                "https://images.dog.ceo/breeds/finnish-lapphund/mochilamvan.jpg",
                "모빌리티",
                "여자",
                1998,
                "서울특별시",
                "강남구",
                "하이용",
                68,
                "취미가 비슷해요!",
                5423,
                8956,
                7934,
                6456,
                5187,
                6545,
                6634,
                2565,
                "00:10",
                "08:38",
                listOf(
                    Interest(
                        "음악",
                        60
                    ),
                    Interest(
                        "그림",
                        25
                    ),
                    Interest(
                        "여행",
                        15
                    )
                )
            ),
            MatchedMember(
                11,
                "https://images.dog.ceo/breeds/sheepdog-shetland/n02105855_5880.jpg",
                "모잉쥐",
                "여자",
                1999,
                "서울특별시",
                "강남구",
                "모 이 룸 최 고",
                66,
                "취미가 비슷해요!",
                5423,
                8956,
                7934,
                6456,
                5187,
                6545,
                6634,
                2565,
                "00:10",
                "08:38",
                listOf(
                    Interest(
                        "음악",
                        60
                    ),
                    Interest(
                        "그림",
                        25
                    ),
                    Interest(
                        "여행",
                        15
                    )
                )
            ),
        ),
        1,
        10,
        1,
        10
    )
    // 메모리 캐시에 저장
    cacheMatchedMemberList.put("matchedMemberList", responseMatchedMemberList)
}

fun getUserInfo() {
    // 본인 정보 GET 요청

    // 응답 데이터 저장 (현재: 더미 데이터)
    val responseUserInfo: Member = Member(
        1,
        "https://images.dog.ceo/breeds/samoyed/n02111889_6249.jpg",
        "안드리아",
        "여자",
        "김민식",
        1999,
        "서울특별시",
        "강남구",
        "멍멍이를 엄청 좋아해요. 댕댕.",
        1,
        6520,
        7552,
        6993,
        7653,
        5683,
        4210,
        6020,
        8758,
        "23:47",
        "06:32",
        listOf(
            Interest(
                "운동",
                48
            ),
            Interest(
                "음악",
                36
            ),
            Interest(
                "요리",
                11
            ),
            Interest(
                "게임",
                5
            )
        )
    )
    // 메모리 캐시에 저장
    cacheUserInfo.put("userInfo", responseUserInfo)
}