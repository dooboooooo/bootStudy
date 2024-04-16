package org.zerock.board.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.board.dto.BoardDTO;
import org.zerock.board.dto.PageRequestDTO;
import org.zerock.board.dto.PageResultDTO;

@SpringBootTest
public class BoardServiceTests {

    @Autowired
    private BoardService boardService;

    @Test
    public void testRegister(){
        BoardDTO dto = BoardDTO.builder()
                .title("Test")
                .content("Test ,,, ")
                .writerEmail("user55@aaa.com")
                .build();
        Long bno = boardService.register(dto);

    }

    @Test
    public void testList(){
        PageRequestDTO pageRequestDTO = new PageRequestDTO();
        PageResultDTO<BoardDTO, Object[]> result = boardService.getList(pageRequestDTO);
        for(BoardDTO boardDTO : result.getDtoList()){
            System.out.println(boardDTO);
        }
    }

    @Test
    public void testGet(){
        Long bno = 100L;
        BoardDTO boardDTO = boardService.get(bno);
        System.out.println(boardDTO);
        // BoardDTO(bno=100, title=Title ... 100, content=Content ... 100, writerEmail=user100@aaa.com, writerName=user100, regDate=2024-04-15T17:17:30.239982, modDate=2024-04-15T17:17:30.239982, replyCount=2)
    }

    @Test
    public void testRemove(){
        Long bno = 1L;
        boardService.removeWithReplies(bno);
    }

    @Test
    public void testModify(){
        BoardDTO boardDTO = BoardDTO.builder()
                .bno(2L)
                .title("수정 테스트 제목")
                .content("수정 테스트 내용")
                .build();
        boardService.modify(boardDTO);
    }
}
