package com.gtel.api.application.service.mappers;

import com.gtel.api.application.service.dto.TestDTO;
import com.gtel.api.domains.models.postgres.Test;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TestMapper extends BaseMapper<Test, TestDTO> {
    TestMapper INSTANCE = Mappers.getMapper(TestMapper.class);
}
/*
* cach dung map struct
*
* TestDTO dto = TestMapper.INSTANCE.toDto(new Test(....))
*
*  */