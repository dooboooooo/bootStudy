package org.zerock.board.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data // getter, setter, toString, 생성자, equals ... 등등
@Builder(toBuilder = true) // 빌더 패턴(점찍어서 값넣고, 점찍어서 값넣고 ,,, )
public class SampleDTO {
    // dto는 프론트에서 자바까지 객체를 전달하는 용도이다.
    // entity는 DB에서 자바까지 영속성을 담당한다.
    // 나중에는 dtotoentity, entitytodto라는 메서드가 이 2개를 전이 역할을 진행할 예정이다.

    private Long sno;
    private String first;
    private String last;
    private LocalDateTime regTime;


}
