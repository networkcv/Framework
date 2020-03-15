package com.zyt.ytcollege.service;

import com.github.pagehelper.Page;
import com.zyt.ytcollege.dao.DO.ClassroomDO;
import com.zyt.ytcollege.util.JsonMsg;
import com.zyt.ytcollege.util.Paging;

/**
 * create by lwj on 2020/3/15
 */
public interface ClassroomService {
    JsonMsg removeClassroomById(int id);

    JsonMsg editClassroom(ClassroomDO classRoomDO);

    ClassroomDO findClassroomById(int id);

    Page<ClassroomDO> findAllClassroom(Paging paging, ClassroomDO classRoomDO);

    JsonMsg saveClassroom(ClassroomDO classRoomDO);
}
