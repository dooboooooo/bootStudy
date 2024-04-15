package org.zerock.board.repository;

import jakarta.persistence.Table;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.board.entity.Board;
import org.zerock.board.entity.Member;

import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class BoardRepositoryTests {

    @Autowired
    private BoardRepository boardRepository;

    @Test
    public void insertBoard(){
        IntStream.rangeClosed(1,100).forEach(i -> {
            Member member = Member.builder().email("user" + i + "@aaa.com").build(); // Member 객체 생성
            Board board = Board.builder()
                    .title("Title ... " + i)
                    .content("Content ... " + i)
                    .writer(member)
                    .build();
            boardRepository.save(board);
        });
    }

    // 지연로딩 방식을 사용할 때 fk 테이블의 값을 불러오는 부분을 실행하기 위해서 트랜젝션을 걸어준다.
    // 원래는 밑의 코드를 실행할 때 board 테이블의 값만 불러오고 데이터 베이스의 연결이 끊기지만,
    // 트랜젝션을 걸어줌으로 써 getWriter()를 실행할 때 다시 연결해준다.
    @Transactional
    @Test
    public void testRead1() {
        Optional<Board> result = boardRepository.findById(100L);
        Board board = result.get(); // result를 가져와 Board 타입의 변수에 넣는다.
        System.out.println(board);
        System.out.println(board.getWriter());
        // 지연로딩 방식으로 로딩하여 Member 엔티티를 불러오는 부분에서 오류가 발생 ! = 메서드 선언부에 @Transactional 추가

        // 즉시로딩 : fk = 내부적으로 left (outer) join 처리가 된다.
        // board b1_0 left join member w1_0 on w1_0.email = b1_0.writer_email
        // 자식 객체가 left에 위치
    }

}
