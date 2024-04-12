package org.zerock.board.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity // 테이블을 자동으로 관리
@Getter
@Builder
@AllArgsConstructor // @Builder의 필수 어노테이션
@NoArgsConstructor // @Builder의 필수 어노테이션
@ToString // 객체가 아닌 문자열로 반환
public class Guestbook extends BaseEntity {
    // extends BaseEntity를 상속받아 날짜 시간 자동처리

    @Id // Guestbook 테이블의 pk 지정(gno로 지정)
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 마리아DB용으로 자동번호를 생성해준다.
    private Long gno; // 방명록에서 사용할 번호

    @Column(length = 100, nullable = false) // 문자 100, not null
    private String title; // 제목

    @Column(length = 1500, nullable = false) // 문자 1500, not null
    private String content; // 내용

    @Column(length = 50, nullable = false) // 문자 50, not null
    private String writer; // 작성자

    // 지정된 필드만 변경할 수 있도록 setter 대신 메서드를 만들어 사용
    public void changeTitle(String title){
        this.title = title; // 세터 역할
    }
    public void changeContent(String content){
        this.content = content; // 세터 역할
    }

}
