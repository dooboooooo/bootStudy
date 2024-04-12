package org.zerock.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.zerock.board.entity.Guestbook;

public interface GuestbookRepository extends JpaRepository<Guestbook, Long>, QuerydslPredicateExecutor<Guestbook> {
    // extends JpaRepository<엔티티명, PK의 타입>
    // JPA가 CRUD를 해준다. -> JpaRepository에서 상속받아 사용하는 메서드
        // insert 작업 : save(엔티티 객체)
        // select 작업 : findById(키 타입), getOne(키 타입/차단된 메서드)
        // update 작업 : save(엔티티 객체)
        // delete 작업 : deleteById(키 타입),  delete(엔티티 객체)

    // Querydsl : Q도메인을 이용해서 자동으로 검색 조건을 완성 시킴(다중검색용)
        // http://querydsl.com/ -> 참고하여 api 의존성 주입을 받아야 한다.
        // QuerydslPredicateExecutor<Guestbook> : 인터페이스는 다중상속 가능(Qdomain 사용을 위한 추가 상속)
    
}
