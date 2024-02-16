package com.ssafy.moiroomserver.chat.controller;

import com.ssafy.moiroomserver.chat.dto.ChatMessageReqDTO;
import com.ssafy.moiroomserver.chat.service.ChatService;
import com.ssafy.moiroomserver.global.constants.SuccessCode;
import com.ssafy.moiroomserver.global.dto.ApiResponse;
import com.ssafy.moiroomserver.global.dto.PageResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import static com.ssafy.moiroomserver.global.constants.SuccessCode.GET_CHAT_MESSAGE;

@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
@RequestMapping("/chat")
@Slf4j
public class ChatController {

    private final ChatService chatService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    /**
     * 챗 메시지 추가 기능 -> websocket, stomp 기능 구현 테스트 필요
     * @param chatMessageReq
     * @param chatRoomId
     */
    @MessageMapping("/room/{chatRoomId}/send")
    public void addChatMessage(@Payload ChatMessageReqDTO chatMessageReq,
                               @DestinationVariable("chatRoomId") Long chatRoomId) {
        log.info("addChatMessage 진입");
        chatService.addChatMessage(chatMessageReq, chatRoomId);
        String destination = "/queue/chat/room/" + chatRoomId;
        simpMessagingTemplate.convertAndSend(destination, chatMessageReq.getMessage());
    }

    /**
     * 채팅 메시지 내역 조회 api
     * @param chatRoomId
     * @param pgno
     * @return
     */
    @GetMapping("/room/{chatRoomId}")
    public ApiResponse<?> getChatMessages(HttpServletRequest request,
                                          @PathVariable Long chatRoomId,
                                          @RequestParam(required = false, defaultValue = "1") int pgno) {
        PageResponse pageResponse = chatService.getChatMessages(request, chatRoomId, pgno);
        return ApiResponse.success(GET_CHAT_MESSAGE, pageResponse);
    }

    /**
     * 대화방 추가 api
     * 대화방 추가시 member_chat_room 테이블에도 정보가 추가 된다.
     * member_chat_room에는 상대방과 나에 대한 pk 정보를 가지고 있어야 한다.
     * @param memberId
     */
    @PostMapping("/{memberId}") // 대화 상대 member pk
    public ApiResponse<?> addChatRoom(HttpServletRequest request,
                                      @PathVariable Long memberId) {

        chatService.addChatRoom(request, memberId);
        return ApiResponse.success(SuccessCode.ADD_CHAT_ROOM);
    }

    /**
     * 대화방 리스트 조회 api
     * @param request
     * @param pgno
     * @return
     */
    @GetMapping
    public ApiResponse<?> getChatRooms(HttpServletRequest request,
                                       @RequestParam(required = false, defaultValue = "1") int pgno) {
        return ApiResponse.success(SuccessCode.GET_CHAT_ROOM, chatService.getChatRooms(request, pgno));
    }
}