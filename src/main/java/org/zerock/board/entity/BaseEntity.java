package org.zerock.board.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass // '직접 테이블용은 아님'을 명시
@Getter
@EntityListeners(value = {AuditingEntityListener.class}) // setter 대신 감시용 코드(데이터 변경을 감지하여 적용 -> Main 메서드에 추가 코드 삽입 필수
abstract class BaseEntity { // abstract 상속간에 추상클래스로 동작(상속관계만 접근 가능)
    // 테이블의 공통되는 부분을 상속 해 줄 클래스
    
    @CreatedDate // 게시물 생성 시 동작 - @EntityListeners
    @Column(name = "regdate", updatable = false) // DB 테이블에 필드명 지정, 업데이트 되지 않게 함
    private LocalDateTime regDate; // 게시물 등록시간
    
    @LastModifiedDate // 게시물 수정 시 동작 - @EntityListeners
    @Column(name = "moddate") // DB 테이블에 필드명 지정함
    private LocalDateTime modDate; // 게시물 수정시간
    
}
