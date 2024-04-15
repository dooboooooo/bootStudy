package org.zerock.board.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "board") // @ToString은 항상 exclude( : 들어오지 못하게 하다)
// Board 객체의 @ToString을 하면 writer 변수로 선언된 Member 객체도 출력해야 함
// Member 객체를 출력하기 위해서는 Member 객체의 toString()이 호출되어야 하고,
// 이 때 데이터베이스 연결이 필요하게 된다 ..
// 따라서 연관관계가 있는 엔티티클래스의 경우 @ToString 할 때 습관전으로 exclude 속성을 사용해야 한다
// !!!!!!!!!!!지연로딩 할 때 반드시!!!!!!!!!!!!!(해당 속성값으로 지정되면 toString()에서 제외함)
public class Reply extends BaseEntity { // 회원이 아닌 사람도 댓글을 남길 수 있다고 가정 = Board와 연관관계를 맺지 않고 처리
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 마리아 DB 자동번호생성
    private Long rno;

    private String text;

    private String replyer;

    @ManyToOne // 현재클래스 TO 필드 타입 클래스
    private Board board;

}
