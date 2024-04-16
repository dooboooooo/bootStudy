package org.zerock.board.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "writer") // @ToString은 항상 exclude( : 들어오지 못하게 하다)
// Board 객체의 @ToString을 하면 writer 변수로 선언된 Member 객체도 출력해야 함
// Member 객체를 출력하기 위해서는 Member 객체의 toString()이 호출되어야 하고,
// 이 때 데이터베이스 연결이 필요하게 된다 ..
// 따라서 연관관계가 있는 엔티티클래스의 경우 @ToString 할 때 습관전으로 exclude 속성을 사용해야 한다
// !!!!!!!!!!!지연로딩 할 때 반드시!!!!!!!!!!!!!(해당 속성값으로 지정되면 toString()에서 제외함)
public class Board extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 마리아 DB 자동번호생성
    private Long bno;

    private String title;

    private String content;

    // 현재클래스 TO 필드 타입 클래스
    // 즉시로딩은 엔티티를 불러올 때 참조하는 모든 엔티티도 같이 로딩해(left join) 성능 저하를 일으킬 수 있다.
    // jpa에서 연관관계의 데이터를 어떻게 가져올 것인가를 fetch라고 한다.
    // 성능저하 개선을 위해 즉시로딩을 사용하지 않고 지연로딩(Lazy loading)을 사용하기 위해서 fetch 모드를 지정하였다.
    @ManyToOne(fetch = FetchType.LAZY)
    private Member writer;

    // 수정 시 필요한 Board entity의 제목, 내용에 대해 set 메서드 추가
    public void changeTitle(String title){
        this.title = title;
    }

    public void changeContent(String content){
        this.content = content;
    }


}
