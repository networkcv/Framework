package com.zyt.ytcollege.service.DTO;

import com.zyt.ytcollege.dao.DO.StudentDO;
import com.zyt.ytcollege.dao.DO.TeacherDO;
import lombok.Data;

/**
 * create by lwj on 2020/3/14
 */
@Data
public class TeacherListDTO {
    private StudentDO studentDO;
    private TeacherDO teacherDO;
}
