package org.zerock.board.service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.zerock.board.dto.GuestbookDTO;
import org.zerock.board.dto.PageRequestDTO;
import org.zerock.board.dto.PageResultDTO;
import org.zerock.board.entity.Guestbook;
import org.zerock.board.entity.QGuestbook;
import org.zerock.board.repository.GuestbookRepository;

import java.util.Optional;
import java.util.function.Function;

@Service
@Log4j2
@RequiredArgsConstructor // 의존성 자동 주입
public class GuestbookServiceImpl implements GuestbookService {

    private final GuestbookRepository repository; // 반드시 final로 선언해야한다.

    // service에서 DB까지 들어가는 친구를 entity
    // 프론트에서 service를 왔다갔다 하는 친구를 dto 라고 한다.
    // 둘이 만나는 service 영역에서 dto -> entity 또는 entity -> dto 변환 작업을 해주어야 한다.
    // 변환 작업은 service interface에 default 키워드를 사용해서 구현해 놓았다.
    // 구현 해놓은 dtoToEntity 메서드를 register 메서드에서 사용하여
    // 프론트에서 들어온 dto를 DB까지 넘어갈 수 있도록 entity로 변환하여 처리하였다.
    @Override
    public Long register(GuestbookDTO dto) {
        log.info("register 메서드 실행 ... : " + dto);
        Guestbook entity = dtoToEntity(dto);
        log.info("dto를 entity로 변환한 결과 entity ... : " + entity);
        repository.save(entity); // DB에 insert(DB는 entity로 가야한다.)
        return entity.getGno(); // insert된 entity의 gno를 리턴한다.
    }

    @Override
    public PageResultDTO<GuestbookDTO, Guestbook> getList(PageRequestDTO requestDTO) {
        // requestDTO의 getPageable 메서드를 정렬조건을 설정하여 0페이지, size 10, 정렬이 담긴 Pageable 객체를 리턴받음 + 검색 추가
        // JPA는 0페이지부터 시작한다.
        Pageable pageable = requestDTO.getPageable(Sort.by("gno").descending());
        BooleanBuilder booleanBuilder = getSearch(requestDTO); // 검색 조건 처리
        // 위의 페이지 정보를 가지고 repository의 findAll 메서드를 실행하여 페이징 된 entity 객체들을 받음(0페이지의 게시물 10개)
        Page<Guestbook> result = repository.findAll(booleanBuilder, pageable); // Querydsl 사용 검색 booleanBuilder 추가
        // entity를 받아 dto로 변환하는 fn 함수를 생성 // entityToDto : service 인터페이스에 구현되어 있음(dto 리턴)
        Function<Guestbook, GuestbookDTO> fn = (entity -> entityToDto(entity));
        // 페이징 된 entity 객체들(result)과 entity를 받아 dto로 변환하는 함수(fn)으로 PageResultDTO를 생성해서 리턴
        return new PageResultDTO<>(result, fn);
    }

    @Override
    public GuestbookDTO read(Long gno) {
        // 매개값으로 받은 gno로 findById 메서드를 실행하여 entity 객체로 받음
        Optional<Guestbook> result = repository.findById(gno);
        // 매개값으로 받은 gno entity 객체가 있으면 entity를 dto로 바꾸어 리턴, 없으면 null 리턴
        return result.isPresent() ? entityToDto(result.get()) : null;
    }

    @Override
    public void remove(Long gno) {
        repository.deleteById(gno);
    }

    @Override
    public void modify(GuestbookDTO dto) {
        // 제목, 내용만 수정 가능
        Optional<Guestbook> result = repository.findById(dto.getGno());
        if(result.isPresent()){
            Guestbook entity = result.get(); // result를 가져와 Guestbook 타입 변수에 넣음
            entity.changeTitle(dto.getTitle());
            entity.changeContent(dto.getContent()); // entity 객체에 수정 제목, 수정 내용을 셋팅
            repository.save(entity); // sava : 있으면 update
        }
    }

    // Querydsl 처리
    private BooleanBuilder getSearch(PageRequestDTO requestDTO){
        String type = requestDTO.getType(); // 검색 타입 값 가져옴
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        QGuestbook qGuestbook = QGuestbook.guestbook;
        String keyword = requestDTO.getKeyword(); // 검색어 값 가져옴
        BooleanExpression expression = qGuestbook.gno.gt(0L); // gno > 0 조건만 생성
        booleanBuilder.and(expression);

        if(type == null || type.trim().length() == 0){ // 검색 조건이 없는 경우
            return booleanBuilder; // gno > 0 조건만 있는 booleanBuilder 리턴
        }

        // 검색 조건 작성
        BooleanBuilder conditionBuilder = new BooleanBuilder();
        if(type.contains("t")){
            conditionBuilder.or(qGuestbook.title.contains(keyword));
        }
        if(type.contains("c")){
            conditionBuilder.or(qGuestbook.content.contains(keyword));
        }
        if(type.contains("w")){
            conditionBuilder.or(qGuestbook.writer.contains(keyword));
        }

        // 모든 조건 통합
        booleanBuilder.and(conditionBuilder); // (gno > 0) and (검색조건.. or 검색조건 .. )
        return booleanBuilder;
    }


}
