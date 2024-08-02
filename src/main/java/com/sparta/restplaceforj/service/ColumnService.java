package com.sparta.restplaceforj.service;

import com.sparta.restplaceforj.dto.ColumnRequestDto;
import com.sparta.restplaceforj.dto.ColumnResponseDto;
import com.sparta.restplaceforj.entity.Card;
import com.sparta.restplaceforj.entity.Column;
import com.sparta.restplaceforj.entity.Plan;
import com.sparta.restplaceforj.exception.CommonException;
import com.sparta.restplaceforj.exception.ErrorEnum;
import com.sparta.restplaceforj.repository.CardRepository;
import com.sparta.restplaceforj.repository.ColumnRepository;
import com.sparta.restplaceforj.repository.PlanRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.hibernate.boot.model.internal.XMLContext.Default;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ColumnService {

  private final ColumnRepository columnRepository;
  private final PlanRepository planRepository;
  private final CardRepository cardRepository;


  /**
   * 컬럼 생성 로직
   *
   * @param planId
   * @param requestDto
   * @return ColumnResponseDto
   */
  @Transactional
  public ColumnResponseDto createColumn(Long planId, ColumnRequestDto requestDto)
      throws CommonException {

    Plan plan = planRepository.findByIdOrThrow(planId);

    Column columns = Column.builder()
        .title(requestDto.getTitle())
        .date(requestDto.getDate())
        .plan(plan)
        .defaultValue(Boolean.FALSE)
        .build();

    columnRepository.save(columns);

    return ColumnResponseDto.builder()
        .id(columns.getId())
        .title(columns.getTitle())
        .date(columns.getDate())
        .build();
  }

  /**
   * 컬럼 수정 로직
   *
   * @param planId
   * @param columnId
   * @param requestDto
   * @return ColumnResponseDto
   */
  @Transactional
  public ColumnResponseDto updateColumn(Long planId, Long columnId,
      ColumnRequestDto requestDto) {
    Column column = columnRepository.findByIdOrThrow(columnId);

    if (!column.getPlan().equals(planRepository.findByIdOrThrow(planId))) {
      throw new CommonException(ErrorEnum.BAD_REQUEST);
    }

    column.updateColumn(requestDto);
    columnRepository.save(column);

    return ColumnResponseDto.builder()
        .id(columnId)
        .title(column.getTitle())
        .date(column.getDate())
        .build();
  }

  /**
   * 컬럼 삭제 로직
   *
   * @param planId
   * @param columnId
   */
  @Transactional
  public void deleteColumn(Long planId, Long columnId) {
    Column column = columnRepository.findByIdOrThrow(columnId);
    Plan plan = planRepository.findByIdOrThrow(planId);

    if (!column.getPlan().equals(plan)) {
      throw new CommonException(ErrorEnum.BAD_REQUEST);
    }
    Column defaultColum = columnRepository.findOneByPlanAndDefaultValue(plan, Boolean.TRUE)
        .orElseThrow(() -> new CommonException(ErrorEnum.BAD_REQUEST));

    List<Card> cardList = cardRepository.findByColumn(column);

    for (Card card : cardList) {
      card.changeColum(defaultColum);
    }

    columnRepository.delete(column);
  }

  /**
   * 컬럼 다건 조회 로직
   *
   * @param planId
   */
  @Transactional
  public List<ColumnResponseDto> getColumnList(Long planId) {
    Plan plan = planRepository.findByIdOrThrow(planId);

    return columnRepository.findByPlanId(plan.getId());
  }
}
