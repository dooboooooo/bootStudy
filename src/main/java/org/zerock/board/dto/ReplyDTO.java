package org.zerock.board.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReplyDTO { // 프론트 ~ 서비스를 왔다갔다하는 그릇
    private Long rno;
    private String text;
    private String replyer;
    private Long bno; // 게시글 번호
    private LocalDateTime regDate, modDate;
}
