package com.zyt.ytcollege.service.DTO;

import com.zyt.ytcollege.dao.DO.ApplyDO;
import lombok.Data;

/**
 * create by lwj on 2020/3/16
 */
@Data
public class ApplyDTO {
    private ApplyDO applyDO;
    private String startDate;
    private String endDate;
}
