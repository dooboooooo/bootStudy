package org.zerock.board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.zerock.board.dto.ReplyDTO;
import org.zerock.board.entity.Board;
import org.zerock.board.entity.Reply;
import org.zerock.board.repository.ReplyRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReplyServiceImpl implements ReplyService {

    private final ReplyRepository replyRepository;

    @Override
    public Long register(ReplyDTO replyDTO) {
        // 프론트에서 입력받은 dto를 entity로 변환하여 Reply 객체에 넣는다.
        Reply reply = dtoToEntity(replyDTO);
        // entity를 insert
        replyRepository.save(reply);
        // insert 된 entity의 rno를 가져와 리턴
        return reply.getRno();
    }

    @Override
    public List<ReplyDTO> getList(Long bno) {
        // 매개값으로 받은 게시물 번호를 Board 객체에 넣어 Reply 엔티티 List를 리턴받음
        List<Reply> result = replyRepository.getRepliesByBoardOrderByRno(Board.builder().bno(bno).build());
        // entity로 받은 result를 하나씩 DTO로 변환하여 리스트로 만들어 리턴
        return result.stream().map(reply -> entityToDTO(reply)).collect(Collectors.toList());
    }

    @Override
    public void modify(ReplyDTO replyDTO) {
        // 매개값으로 전달받은 dto 객체를 entity로 변환하여 저장
        Reply reply = dtoToEntity(replyDTO);
        // entity 객체를 update
        replyRepository.save(reply);
    }

    @Override
    public void remove(Long rno) {
        replyRepository.deleteById(rno);
    }
}
