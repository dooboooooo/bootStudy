package org.zerock.board.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.zerock.board.dto.BoardDTO;
import org.zerock.board.dto.PageRequestDTO;
import org.zerock.board.dto.PageResultDTO;
import org.zerock.board.entity.Board;
import org.zerock.board.entity.Member;
import org.zerock.board.repository.BoardRepository;
import org.zerock.board.repository.ReplyRepository;

import java.util.function.Function;

@Service
@RequiredArgsConstructor
@Log4j2
public class BoardServiceImpl implements BoardService {

    private final BoardRepository repository; // 자동 주입 final
    private final ReplyRepository replyRepository; // 게시물 삭제 시 댓글도 삭제 위해서 추가

    @Override
    public Long register(BoardDTO dto) {
        log.info("BoardServiceImpl.register() 실행 ... dto : " + dto);
        Board board = dtoToEntity(dto); // 입력받은 dto를 entity로 변환
        repository.save(board); // entity를 insert
        return board.getBno(); // insert된 entity의 bno를 리턴
    }

    @Override
    public PageResultDTO<BoardDTO, Object[]> getList(PageRequestDTO pageRequestDTO) {
        log.info("BoardServiceImpl.register() 실행 ... pageRequestDTO : " + pageRequestDTO);

        Function<Object[], BoardDTO> fn = (en -> entityToDTO((Board)en[0], (Member)en[1], (Long)en[2])); // 여러 entity가 섞여있어 Object[]

//        Page<Object[]> result = repository.getBoardWithReplyCount(pageRequestDTO.getPageable(Sort.by("bno").descending()));
        // 검색 추가
        Page<Object[]> result = repository.searchPage(pageRequestDTO.getType(), pageRequestDTO.getKeyword(), pageRequestDTO.getPageable(Sort.by("bno").descending())  );

        return new PageResultDTO<>(result, fn);
    }

    @Override
    public BoardDTO get(Long bno) {
        Object result = repository.getBoardByBno(bno); // board, member, count(reply)
        Object[] arr = (Object[]) result; // 배열에 저장
        // 배열에 담긴 board, member, count(reply)를 각각에 맞는 엔티티로 entityToDTO 메서드를 실행하여 BoardDTO를 리턴받음
        return entityToDTO((Board)arr[0], (Member)arr[1], (Long)arr[2]);
    }

    @Transactional // 게시물이 삭제될 때 댓글이 먼저 삭제된 후 삭제되었으면 게시물 삭제해야함
    @Override
    public void removeWithReplies(Long bno) {
        replyRepository.deleteByBno(bno); // 댓글 먼저 삭제
        repository.deleteById(bno);
    }

    @Transactional // 트랜잭션이 붙어야만 테스트 진행되었음 .. ?
    @Override
    public void modify(BoardDTO boardDTO) {
        Board board = repository.getOne(boardDTO.getBno()); // bno를 이용해 board entity 가져옴
        // modify()는 findById()를 이용하는 대신 필요한 순간까지 로딩을 지연하는 방식인 getOne() 이용해서 처리

        if(board != null) { // bno에 해당하는 게시물이 있으면
            board.changeTitle(boardDTO.getTitle());
            board.changeContent(boardDTO.getContent()); // 수정
            repository.save(board); // update
        }
    }
}
