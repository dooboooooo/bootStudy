package org.zerock.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.zerock.board.entity.Board;
import org.zerock.board.entity.Reply;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Long> {

    // 게시물 삭제할 때 fk로 연결되어 있기 때문에 해당 게시물에 있는 댓글을 먼저 삭제 후에 게시물을 삭제한다.(트랜젝션 !)
    @Modifying // update delete를 실행하기 위해서 사용해야 하는 어노테이션
    @Query("delete from Reply r where r.board.bno = :bno")
    void deleteByBno(Long bno);

    // 게시물로 댓글 목록 가져오기(쿼리메서드)
    List<Reply> getRepliesByBoardOrderByRno(Board board);
}
