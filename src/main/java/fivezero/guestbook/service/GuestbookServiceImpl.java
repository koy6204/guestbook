package fivezero.guestbook.service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import fivezero.guestbook.dto.GuestbookDTO;
import fivezero.guestbook.dto.PageRequestDTO;
import fivezero.guestbook.dto.PageResultDTO;
import fivezero.guestbook.entity.Guestbook;
import fivezero.guestbook.repository.GuestbookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import fivezero.guestbook.entity.QGuestbook;

import java.util.Optional;
import java.util.function.Function;

@Service
@Log4j2
@RequiredArgsConstructor // 의존성 자동 주입
public class GuestbookServiceImpl implements GusetbookService {

    private final GuestbookRepository repository; // 반드시 final 선언

    @Override
    public Long register(GuestbookDTO dto) {

        log.info("DTO-----------");
        log.info(dto);

        Guestbook entity = dtoToEntity(dto);

        log.info(entity);

        repository.save(entity);


        return entity.getGno();
    }

    @Override
    public PageResultDTO<GuestbookDTO, Guestbook> getList(PageRequestDTO requestDTO) {

        Pageable pageable = requestDTO.getPageable(Sort.by("gno").descending());

        BooleanBuilder booleanBuilder = getSearch(requestDTO);//감색조건처리

        Page<Guestbook> result = repository.findAll(booleanBuilder,pageable); //querydsl 사용

        Function<Guestbook, GuestbookDTO> fn = (entity -> entityToDto(entity));

        return new PageResultDTO<>(result, fn);


    }

    @Override
    public GuestbookDTO read(Long gno) {

        Optional<Guestbook> result = repository.findById(gno);

        return result.isPresent()? entityToDto(result.get()): null;
    }

    @Override
    public void remove(Long gno) {
        repository.deleteById(gno);
    }

    @Override
    public void modify(GuestbookDTO dto) {

        Optional<Guestbook> result = repository.findById(dto.getGno());

        if (result.isPresent()){

            Guestbook entity = result.get();

            entity.changeTitle(dto.getTitle());
            entity.changeContent(dto.getContent());

            repository.save(entity);
        }
    }

    //querydsl 처리
    private BooleanBuilder getSearch(PageRequestDTO requestDTO) {
        String type = requestDTO.getType();
        BooleanBuilder builder = new BooleanBuilder();
        QGuestBook qGuestBook = QGuestBook.guestBook;
        String keyword = requestDTO.getKeyword();
        BooleanExpression expression = qGuestBook.gno.gt(0L);
        builder.and(expression);
        if(type == null || type.trim().length() == 0)
            return builder;

        BooleanBuilder conditionBuilder = new BooleanBuilder();
        if(type.contains("t"))
            conditionBuilder.or(qGuestBook.title.contains(keyword));
        if(type.contains("c"))
            conditionBuilder.or(qGuestBook.content.contains(keyword));
        if(type.contains("w"))
            conditionBuilder.or(qGuestBook.writer.contains(keyword));

        builder.and(conditionBuilder);
        return builder;
    }


}
