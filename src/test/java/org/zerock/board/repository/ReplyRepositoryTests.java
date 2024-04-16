package org.zerock.board.repository;

import jakarta.persistence.Table;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.board.entity.Board;
import org.zerock.board.entity.Reply;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class ReplyRepositoryTests {

    @Autowired
    private ReplyRepository replyRepository;

    @Test
    public void insertReply(){
        IntStream.rangeClosed(1, 300).forEach(i -> {
            long bno = (long) (Math.random() * 100) + 1; // 1 ~ 100 랜덤 번호 bno에 저장

            Board board = Board.builder().bno(bno).build(); // 랜덤 게시물

            Reply reply = Reply.builder()
                    .text("Reply ... ... " + i)
                    .board(board)
                    .replyer("guest") // 아직 member 연결 안함
                    .build();
            replyRepository.save(reply);
        });
    }

    @Test
    public void readRead1() {
        Optional<Reply> result = replyRepository.findById(1L);
        Reply reply = result.get(); // result를 가져와 Reply 타입의 변수에 넣는다.
        System.out.println(reply);
        System.out.println(reply.getBoard()); // fk = 내부적으로 left (outer) join 처리가 된다.
        System.out.println(reply.getBoard().getWriter()); // 연결연결 ~
        // reply r1_0 left join board b1_0 on b1_0.bno=r1_0.board_bno
        // left join member w1_0 on w1_0.email=b1_0.writer_email
        // 자식 객체(fk)가 left에 위치 = reply > board > member
        // 위와 같이 특정한 엔티티를 조회할 때 연관관계를 가진 모든 엔티티를 같이 로딩하는것을 Eager loading(즉시 로딩)이라고 한다. = 성능저하
    }

    @Test
    public void testListByBoard(){
        List<Reply> replyList = replyRepository.getRepliesByBoardOrderByRno(Board.builder().bno(97L).build());
        replyList.forEach(reply -> {
            System.out.println(reply);
        });
    }

}
