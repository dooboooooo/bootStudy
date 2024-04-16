package org.zerock.board.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.zerock.board.entity.Board;
import org.zerock.board.repository.search.SearchBoardRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long>, SearchBoardRepository {
    // 여러 엔티티가 섞여있는 검색기능 구현을 위해 SearchBoardRepository 인터페이스를 추가로 상속받음

    // 한 개의 로우(Object) 내에 Object[]로 나온다. [Board(bno=100, title=Title ... 100, content=Content ... 100), Member(email=user100@aaa.com, password=1111, name=user100)]
    // 내부에 있는 엔티티를 이용할 때는 left join 뒤에 on을 사용하지 않는다.(조인조건 알아서)
    // 지연로딩을 사용할 때 조인을 이용하지 않으면 여러번 쿼리를 실행해야 하기 때문에 여기서 조인을 했음
    // :bno = 매개값으로 받은 bno
    @Query("select b, w from Board b left join b.writer w where b.bno =:bno")
    Object getBoardWithWriter(@Param("bno") Long bno);

    // board 입장에서는 reply와 연관관계가 없는상태(reply가 board를 참조중)
    // 위와 다르게 연관관계가 없는 상태에서는 on을 사용한다.(조인조건 직접 지정)
    @Query("select b, r from Board b left join Reply r on r.board = b where b.bno = :bno")
    List<Object[]> getBoardWithReply(@Param("bno") Long bno);

    @Query(value = "select b, w, count(r) from Board b left join b.writer w left join Reply r on r.board = b group by b" // Board, Member, Reply의 개수
            , countQuery = "select count (b) from Board b") // Board의 개수
    Page<Object[]> getBoardWithReplyCount(Pageable pageable); // 목록 화면에 필요한 데이터

    @Query("SELECT b, w, count(r) FROM Board b LEFT JOIN b.writer w LEFT OUTER JOIN Reply r ON r.board = b WHERE b.bno = :bno")
    Object getBoardByBno(@Param("bno") Long bno); // 상세조회

}
