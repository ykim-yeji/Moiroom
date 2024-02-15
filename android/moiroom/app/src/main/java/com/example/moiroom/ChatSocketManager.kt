package com.example.moiroom

import android.util.Log
import io.reactivex.disposables.Disposable
import okhttp3.OkHttpClient
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient
import okhttp3.WebSocket
import okhttp3.Request
import ua.naiksoftware.stomp.dto.LifecycleEvent
import java.util.concurrent.Flow

//class ChatSocketManager(private val activity: ChatActivity) {
//    private lateinit var stompClient: StompClient
//
//    fun connect() {
//        val socketUrl = "wss://moiroom.n-e.kr/ws"
//        val client = OkHttpClient()
//
//        // Stomp Connection 초기화
//        stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, socketUrl, null, client)
//
//        stompClient.lifecycle().subscribe { event ->
//            when (event.type) {
//                LifecycleEvent.Type.OPENED -> Log.d("MYMYTAG", "Stomp connection opened")
//                LifecycleEvent.Type.ERROR -> Log.e("MYMYTAG", "Error", event.exception)
//                LifecycleEvent.Type.CLOSED -> Log.d("MYMYTAG", "Stomp connection closed")
//                else -> Log.d("MYMYTAG", "Other event: ${event.type}")
//            }
//        }
//
//        // Stomp 연결
//        stompClient.connect()
//    }
//
//    fun send(chatRoomId: String, message: String) {
//        val destination = "/queue/chat/room/$chatRoomId"
//        stompClient.send(destination, message)
//    }
//
//    fun close() {
//        // Stomp 연결 해제
//        stompClient.disconnect()
//    }
//
//    fun subscribe(destination: String) {
//        // "/topic" 접두사 추가
//        val destinationWithPrefix = "/topic$destination"
//
//        // 메시지 수신 구독
//        stompClient.topic(destinationWithPrefix).subscribe { message ->
//            Log.d("MYMYTAG", "Received message: ${message.payload}")
//
//            // 메시지 처리를 위한 콜백 함수 실행
////            handleReceivedMessage(message.payload)
//        }
//    }
//}


class ChatSocketManager(private val activity: ChatActivity) {
    private lateinit var stompClient: StompClient

    var disposable: Disposable? = null


    fun connect(chatRoomId: Long) {
        val socketUrl = "wss://moiroom.n-e.kr/ws"
        val client = OkHttpClient()
        stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, socketUrl, null, client)

        stompClient.lifecycle().subscribe { event ->
            when (event.type) {
                LifecycleEvent.Type.OPENED -> {
                    Log.d("MYMYTAG", "Stomp connection opened")
                    subscribe(chatRoomId)
                }
                LifecycleEvent.Type.ERROR -> Log.e("MYMYTAG", "Error", event.exception)
                LifecycleEvent.Type.CLOSED -> Log.d("MYMYTAG", "Stomp connection closed")
                else -> Log.d("MYMYTAG", "Other event: ${event.type}")
            }
        }

        stompClient.connect()
    }

//    fun subscribe(chatRoomId: Long) {
//        Log.d("MYMYTAG", "Subscribe function is called with chatRoomId: $chatRoomId") // 로그 출력 코드 추가
//        stompClient.topic("/queue/chat/room/$chatRoomId").subscribe({ topicMessage ->
//            Log.d("MYMYTAG", "Received: ${topicMessage.payload}")
//        }, { throwable ->
//            Log.e("MYMYTAG", "Error on subscribe topic", throwable)
//        })
//    }

//    fun subscribe(chatRoomId: Long) {
//            subscription = stompClient.topic("/queue/chat/room/$chatRoomId").subscribe({ topicMessage ->
//                Log.d("MYMYTAG", "Received: ${topicMessage.payload}")
//            }, { throwable ->
//                Log.e("MYMYTAG", "Error on subscribe topic", throwable)
//            })
//
//    }
    fun send(chatRoomId: String, message: String) {
        stompClient.send("/room/$chatRoomId/send", message).subscribe({}, { throwable ->
            Log.e("ChatSocketManager", "Error on send", throwable)
        })
    }

    fun disconnect() {
        stompClient.disconnect()
    }

    fun subscribe(chatRoomId: Long) {
        disposable = stompClient.topic("/queue/chat/room/$chatRoomId").subscribe({ topicMessage ->
            Log.d("MYMYTAG", "Received: ${topicMessage.payload}")
        }, { throwable ->
            Log.e("MYMYTAG", "Error on subscribe topic", throwable)
        })
    }

    fun unsubscribe() {
        disposable?.dispose()
    }
}

