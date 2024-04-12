package org.zerock.board.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.zerock.board.entity.Guestbook;
import org.zerock.board.entity.QGuestbook;

import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest //스프링 부트 테스트용
public class GuestbookRepositoryTests {

    @Autowired //인터페이스 자동 주입
    private GuestbookRepository guestbookRepository;

    @Test
    public void insertDummies() { //테이블에 더미 데이터 300개 추가
        IntStream.rangeClosed(1, 300).forEach(i -> { // 인터페이스는 람다식으로 .. ?
            Guestbook guestbook = Guestbook.builder()
                    .title("제목..." + i)
                    .content("내용..." + i)
                    .writer("user" + (i%10)) // 1 ~ 9
                    .build(); //빌더 패턴은 엔티티에 @Builder 필수
            System.out.println(guestbookRepository.save(guestbook));
            //jpa save 메서드를 실행, 출력
        });
    }

    @Test
    public void updateTest(){
        // 게시물을 수정하는 테스트
        Optional<Guestbook> result = guestbookRepository.findById(600L);
        // 300번의 게시물을 찾아와 result에 넣는다.
        
        if(result.isPresent()){ // result에 객체가 있으면 -> Optional 때문에
            Guestbook guestbook = result.get(); // 엔티티를 가져와 변수에 넣는다.
            System.out.println(guestbook.getTitle());
            System.out.println(guestbook.getContent());
            System.out.println(guestbook.getWriter());
            guestbook.changeTitle("수정된 제목 ... "); // 엔티티에 만든 메서드
            guestbook.changeContent("수정된 내용 ... "); // 엔티티에 만든 메서드
            guestbookRepository.save(guestbook); // 있으면 update, 없으면 insert
        }

    }
    
    @Test
    public void testQuery1(){ // 단일 조건으로 쿼리 생성 + 페이징 + 정렬
        // 페이지 타입으로 요청을 처리함(0번 페이지에 10개씩 객체 생성, gno를 기준으로 내림차순 정렬)
        Pageable pageable = PageRequest.of(0, 10, Sort.by("gno").descending());

        // Querydsl용 객체 생성(동적처리용)
        QGuestbook qGuestbook = QGuestbook.guestbook;

        String keyword = "9"; // 프론트 페이지에서 1번을 찾으려고 입력했을 때

        BooleanBuilder builder = new BooleanBuilder(); // 다중 조건 처리용 객체
        // title = 1 표현식을 생성
        BooleanExpression expressionTitle = qGuestbook.title.contains(keyword); // expression : 표현
        // content = 1 표현식을 생성
        BooleanExpression expressionContent = qGuestbook.content.contains(keyword); // expression : 표현
        // 2개의 조건을 합체
        BooleanExpression exAll = expressionTitle.or(expressionContent);
        builder.and(exAll); // 다중 조건 처리용 객체에 표현식을 삽입
        builder.and(qGuestbook.gno.gt(0L)); // where문 추가(gno > 0) : 인덱싱 처리
        // 페이지 타입의 객체가 result 변수에 들어간다. .findAll은 검색된 모든 객체가 나온다.
        Page<Guestbook> result = guestbookRepository.findAll(builder, pageable);
        // 결과 출력
        result.stream().forEach(guestbook -> {
            System.out.println("검색결과 : " + guestbook);
        });

    }








}