package org.zerock.board.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tbl_memo") // DB 테이블명을 지정
@ToString                 // 객체가 아닌 문자로(DB에 들어갈 때)
@Getter
@Builder                  // 메서드.필드(값).필드(값).builer; (빌더 패턴)
// @AllArgsConstructor, @NoArgsConstructor 필수 !
@AllArgsConstructor       // new 클래스 할 때 모든 필드 값 파라미터로 만든다.
@NoArgsConstructor        // new 클래스 할 때 아무것도 없이 만든다.(기본)
public class Memo {
    // 엔티티 : 데이터베이스에 테이블과 필드를 생성시켜 관리하는 객체
    // 엔티티를 이용해서 jpa를 활성화 하려면 application.properties에 필수 항목 추가
    @Id   // 기본키를 명시
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mno;

    @Column(length = 200, nullable = false)
    private String memoText;
}
