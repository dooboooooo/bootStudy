package org.zerock.board.repository;

import jakarta.persistence.Table;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.zerock.board.entity.Board;
import org.zerock.board.entity.Member;

import java.util.Arrays;
import java.util.List;
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

    @Test
    public void testReadWithWriter(){
        Object result = boardRepository.getBoardWithWriter(100L);
        Object[] arr = (Object[]) result; // [Board(bno=100, title=Title ... 100, content=Content ... 100), Member(email=user100@aaa.com, password=1111, name=user100)]
        System.out.println("==============================================================================");
        System.out.println(Arrays.toString(arr));
    }

    @Test
    public void testGetBoardWithReply(){
        List<Object[]> result = boardRepository.getBoardWithReply(100L);
        System.out.println("==============================================================================");
        for(Object[] arr : result) {
            System.out.println(Arrays.toString(arr));
        }
//        [Board(bno=100, title=Title ... 100, content=Content ... 100), Reply(rno=110, text=Reply ... ... 110, replyer=guest)]
//        [Board(bno=100, title=Title ... 100, content=Content ... 100), Reply(rno=118, text=Reply ... ... 118, replyer=guest)]
    }

    @Test
    public void testWithReplyCount(){
        // Pageable 객체 생성, bno로 내림차순 한 리스트의 첫페이지 게시물 10개
        Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending());
        Page<Object[]> result = boardRepository.getBoardWithReplyCount(pageable);
        result.get().forEach(row -> {
            Object[] arr = (Object[]) row;
            System.out.println(Arrays.toString(arr));
        });
//        [Board(bno=100, title=Title ... 100, content=Content ... 100), Member(email=user100@aaa.com, password=1111, name=user100), 2]
//        [Board(bno=99, title=Title ... 99, content=Content ... 99), Member(email=user99@aaa.com, password=1111, name=user99), 4]
//        [Board(bno=98, title=Title ... 98, content=Content ... 98), Member(email=user98@aaa.com, password=1111, name=user98), 6]
//        [Board(bno=97, title=Title ... 97, content=Content ... 97), Member(email=user97@aaa.com, password=1111, name=user97), 4]
//        [Board(bno=96, title=Title ... 96, content=Content ... 96), Member(email=user96@aaa.com, password=1111, name=user96), 2]
//        [Board(bno=95, title=Title ... 95, content=Content ... 95), Member(email=user95@aaa.com, password=1111, name=user95), 7]
//        [Board(bno=94, title=Title ... 94, content=Content ... 94), Member(email=user94@aaa.com, password=1111, name=user94), 2]
//        [Board(bno=93, title=Title ... 93, content=Content ... 93), Member(email=user93@aaa.com, password=1111, name=user93), 5]
//        [Board(bno=92, title=Title ... 92, content=Content ... 92), Member(email=user92@aaa.com, password=1111, name=user92), 4]
//        [Board(bno=91, title=Title ... 91, content=Content ... 91), Member(email=user91@aaa.com, password=1111, name=user91), 4]
    }

    @Test
    public void testSearch1(){
        boardRepository.search1();
    }

    @Test
    public void testSearchPage() {
        Pageable pageable = PageRequest.of(0,10, Sort.by("bno").descending().and(Sort.by("title").ascending())); // 정렬 두개
        Page<Object[]> result = boardRepository.searchPage("t", "1", pageable);
    }

}
