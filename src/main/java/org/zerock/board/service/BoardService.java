package org.zerock.board.service;

import org.zerock.board.dto.BoardDTO;
import org.zerock.board.dto.PageRequestDTO;
import org.zerock.board.dto.PageResultDTO;
import org.zerock.board.entity.Board;
import org.zerock.board.entity.Member;

public interface BoardService {

    Long register(BoardDTO dto); // 게시물 작성
    PageResultDTO<BoardDTO, Object[]> getList(PageRequestDTO pageRequestDTO); // bno, 제목, 내용, 작성자이메일, 작성자이름, 작성일, 수정일, 댓글수
    BoardDTO get(Long bno); // 조회
    void removeWithReplies(Long bno); // 삭제
    void modify(BoardDTO boardDTO); // 수정


    default Board dtoToEntity(BoardDTO dto) {
        // dto로 넘어온 작성자의 이메일을 Member 엔티티에 넣고
        Member member = Member.builder().email(dto.getWriterEmail()).build();
        // dto로 넘어온 Board 값을 엔티티에 넣는다.
        Board board = Board.builder()
                .bno(dto.getBno())
                .title(dto.getTitle())
                .content(dto.getContent())
                .writer(member)
                .build();
        // Board 엔티티를 리턴한다.
        return board;
    }

    default BoardDTO entityToDTO(Board board, Member member, Long replyCount){
        BoardDTO boardDTO = BoardDTO.builder()
                .bno(board.getBno())
                .title(board.getTitle())
                .content(board.getContent())
                .regDate(board.getRegDate())
                .modDate(board.getModDate())
                .writerEmail(member.getEmail())
                .writerName(member.getName())
                .replyCount(replyCount.intValue()) // Long 타입으로 나와 int로 처리
                .build();
        // Board DTO를 리턴한다.
        return boardDTO;
    }
}
