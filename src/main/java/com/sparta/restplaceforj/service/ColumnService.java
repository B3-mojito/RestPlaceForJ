package com.sparta.restplaceforj.service;

import com.sparta.restplaceforj.dto.ColumnResponseDto;
import com.sparta.restplaceforj.entity.Column;
import com.sparta.restplaceforj.entity.Plan;
import com.sparta.restplaceforj.exception.CommonException;
import com.sparta.restplaceforj.exception.ErrorEnum;
import com.sparta.restplaceforj.repository.ColumnRepository;
import com.sparta.restplaceforj.repository.PlanRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.Columns;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
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
    public ColumnResponseDto createColumn(Long planId, String columnTitle){
        Plan plan = planRepository.findById(planId).orElseThrow(() -> new CommonException(
                ErrorEnum.COLUMN_NOT_FOUND));
        Column columns = Column.builder().title(columnTitle).plans(plan).build();
        columnRepository.save(columns);
        return new ColumnResponseDto(columns.getTitle());
    }
}
