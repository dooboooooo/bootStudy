package org.zerock.board.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class Member extends BaseEntity {
    @Id
    private String email; // email 주소를 회원의 id로 사용한다.(PK)
    private String password;
    private String name;
}
