package org.zerock.board.service;

import org.zerock.board.dto.GuestbookDTO;
import org.zerock.board.dto.PageRequestDTO;
import org.zerock.board.dto.PageResultDTO;
import org.zerock.board.entity.Guestbook;

public interface GuestbookService { // 추상메서드
    Long register(GuestbookDTO dto); // 글쓰기

    PageResultDTO<GuestbookDTO, Guestbook> getList(PageRequestDTO requestDTO); // 리스트

    GuestbookDTO read(Long gno); // 조회, 조회 후 프론트에 뿌릴것이므로 dto로 반환

    // interface는 실제 실행 코드를 가지는 구현객체가 필요하지만,
    // default 키워드를 사용하면 인터페이스에서도 구현이 가능하다.
    default Guestbook dtoToEntity(GuestbookDTO dto) { // dto를 entity로 변환
        Guestbook entity = Guestbook.builder()
                .gno(dto.getGno())
                .title(dto.getTitle())
                .content(dto.getContent())
                .writer(dto.getWriter())
                .build();
        return entity;
    }

    default GuestbookDTO entityToDto(Guestbook entity){ // entity로 dto로 변환
        GuestbookDTO dto = GuestbookDTO.builder()
                .gno(entity.getGno())
                .title(entity.getTitle())
                .content(entity.getContent())
                .writer(entity.getWriter())
                .regDate(entity.getRegDate())
                .modDate(entity.getModDate())
                .build();
        return dto;
    }

}
