package com.sparta.restplaceforj.service;

import com.sparta.restplaceforj.dto.ColumnResponseDto;
import com.sparta.restplaceforj.entity.Column;
import com.sparta.restplaceforj.entity.Plan;
import com.sparta.restplaceforj.exception.CommonException;
import com.sparta.restplaceforj.exception.ErrorEnum;
import com.sparta.restplaceforj.repository.ColumnRepository;
import com.sparta.restplaceforj.repository.PlanRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Columns;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ColumnService {
    private final ColumnRepository columnRepository;
    private final PlanRepository planRepository;


    /**
     * 컬럼 생성 로직
     *
     * @param planId
     * @param columnTitle
     * @return ColumnResponseDto
     */
    @Transactional
    public ColumnResponseDto createColumn(Long planId, String columnTitle){
        Plan plan = planRepository.findPlanById(planId);
        Column columns = Column.builder().title(columnTitle).plan(plan).build();
        columnRepository.save(columns);
        return ColumnResponseDto.builder().title(columns.getTitle()).build();
    }

    /**
     * 컬럼 수정 로직
     *
     * @param planId
     * @param columnId
     * @param columnTitle
     * @return ColumnResponseDto
     */
    @Transactional
    public ColumnResponseDto updateColumn(Long planId, Long columnId ,String columnTitle){
        Column column = columnRepository.findColumnById(columnId);
        if(!column.getPlan().equals(planRepository.findPlanById(planId))){
            throw new CommonException(ErrorEnum.BAD_REQUEST);
        }
        column.updateColumn(columnTitle);
        columnRepository.save(column);
        return ColumnResponseDto.builder().title(column.getTitle()).build();
    }
}
