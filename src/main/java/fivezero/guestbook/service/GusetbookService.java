package fivezero.guestbook.service;

import fivezero.guestbook.dto.GuestbookDTO;
import fivezero.guestbook.dto.PageRequestDTO;
import fivezero.guestbook.dto.PageResultDTO;
import fivezero.guestbook.entitiy.Guestbook;

public interface GusetbookService {

    Long register(GuestbookDTO dto);

    GuestbookDTO read(Long gno);

    void remove(Long gno);

    void modify(GuestbookDTO dto);

    PageResultDTO<GuestbookDTO, Guestbook> getList(PageRequestDTO requestDTO);

    default Guestbook dtoToEntity(GuestbookDTO dto) {
        Guestbook entity = Guestbook.builder()
                .gno(dto.getGno())
                .title(dto.getTitle())
                .content((dto.getContent()))
                .writer(dto.getWriter())
                .build();
        return entity;
    }

    default GuestbookDTO entityToDto(Guestbook entity) {

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
