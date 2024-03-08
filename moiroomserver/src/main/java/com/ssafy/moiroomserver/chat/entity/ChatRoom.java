package com.ssafy.moiroomserver.chat.entity;

import com.ssafy.moiroomserver.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "chat_room")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@AllArgsConstructor
@Builder
public class ChatRoom extends BaseEntity {

    private static final String DEFAULT_MESSAGE = "대화를 시작해보세요.";

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatRoomId;

    @Column(name = "last_message", nullable = false)
    @Builder.Default
    private String lastMessage = DEFAULT_MESSAGE;

    public void updateLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }
}