package org.zerock.board.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardDTO { // Member를 참조하는 대신 화면에서 필요한 작성자의 이메일, 이름을 처리한다는 점에서 entity와 다름

    private Long bno;
    private String title;
    private String content;
    private String writerEmail; // 작성자 이메일
    private String writerName; // 작성자 이름
    private LocalDateTime regDate;
    private LocalDateTime modDate;
    private int replyCount; // 댓글수

}
