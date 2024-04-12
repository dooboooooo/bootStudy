package org.zerock.board.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.board.dto.GuestbookDTO;
import org.zerock.board.dto.PageRequestDTO;
import org.zerock.board.dto.PageResultDTO;
import org.zerock.board.entity.Guestbook;

@SpringBootTest // 스프링 부트 테스트임을 명시한다.
public class GuestbookServiceTests {

    @Autowired // 생성자 자동주입
    private GuestbookService service;

    @Test // 프론트에서 dto가 넘어왔다고 가정하고 service의 register 메서드 실행 ... 실제로 db까진 닿지 않음
    public void testRegister(){
        GuestbookDTO guestbookDTO = GuestbookDTO.builder()
                .title("테스트 제목 ... ")
                .content("테스트 내용 ... ")
                .writer("user0")
                .build();
        System.out.println(service.register(guestbookDTO));
    }

    @Test
    public void testList(){
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(1)
                .size(10)
                .build(); // 페이지 정보 값 넣기
        // 페이징 처리, entity가 dto로 변환된 dtoList를 가진 resultDTO 객체 리턴받음 + 페이징 정보도 들어있다.
        PageResultDTO<GuestbookDTO, Guestbook> resultDTO = service.getList(pageRequestDTO);

        System.out.println("PREV : " + resultDTO.isPrev()); // false : 1페이지 -> 이전 필요없음
        System.out.println("NEXT : " + resultDTO.isNext()); // true : 1페이지 -> 다음 필요
        System.out.println("TOTAL : " + resultDTO.getTotalPage()); // 31 : 전체 페이지 개수(게시물 301개)
        System.out.println("=======================================================");

        // resultDTO의 dtoList(entity가 dto로 변환됨)를 가져와 돌면서 출력
        for(GuestbookDTO guestbookDTO : resultDTO.getDtoList()){
            System.out.println(guestbookDTO);
        }
        System.out.println("=======================================================");

        // 현재 페이지에 해당하는 페이지 번호(Integer)들 출력
        resultDTO.getPageList().forEach(i -> System.out.println(i)); // 1 2 3 4 5 6 7 8 9 10
    }

}
