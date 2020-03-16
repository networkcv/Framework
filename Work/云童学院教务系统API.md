### 1.课程管理API

#### 1.1添加课程	POST /su/subject		

|     参数     |  类型  |              介绍               |
| :----------: | :----: | :-----------------------------: |
|     name     | String |            课程名称             |
|    hours     |  int   |             总课时              |
| subjectLevel |  int   |            课程阶段             |
|    state     |  int   | 课程状态 0-上线 1-下线 2-待发布 |
|     cost     | double |            课程费用             |

**subjectLevel 详细说明**

| 状态值 |   对应阶段   |
| :----: | :----------: |
|   1    |   EV3 初级   |
|   2    |   EV3 中级   |
|   3    |   EV3 高级   |
|   4    | SCRATCH 初级 |
|   5    | SCRATCH 中级 |
|   6    | SCRATCH 高级 |
|   7    |   C++ 初级   |
|   8    |   C++ 中级   |
|   9    |   C++ 高级   |



#### 1.2 删除课程	DELET /su/subject/{id}	

#### 1.3 修改课程	PUT /su/subject

​	除要修改的字段外，还需要额外传入 课程Id  `I`大小`d`小写

#### 1.4 根据Id查询课程	GET /su/subject/{id}

#### 1.5 分页条件查询所有课程	GET /su/subjects

支持的条件参数：

|     参数     |  类型  |              介绍               |
| :----------: | :----: | :-----------------------------: |
|   pageNum    |  int   |          页数（默认1）          |
|   pageSize   |  int   |       每页条数（默认10）        |
|     name     | String |        模糊查询课程名称         |
| subjectLevel |  int   |            课程阶段             |
|    state     |  int   | 课程状态 0-上线 1-下线 2-待发布 |



### 2.报名管理API

#### 2.1 添加报名信息 POST /ap/Apply

需要查询报名学员是否已存在，存在则需额外返回Id

**体验报名**

|     参数     |  类型  |              介绍              |
| :----------: | :----: | :----------------------------: |
|     type     |  int   | 报名类型 0-体验报名 1-缴费报名 |
|  studentId   |  int   |       学生Id，没有则不填       |
| studentName  | String |            学生姓名            |
|  studentAge  |  int   |            学生年龄            |
| studentPhone | String |          学生联系方式          |
|  subjectId   |  int   |             课程Id             |
|   referrer   |  int   |             介绍人             |

**付费报名**

除了体验报名中的参数，还需传入 `isFirst  int  是否续保 0-否 1-是`

#### 2.2 删除报名信息 DELETE /ap/apply/{id}

#### 2.3 修改报名信息 PUT /ap/apply

#### 2.4 根据Id查询报名信息 GET /ap/apply/{id}

#### 2.5 分页条件查询报名信息 GET /ap/applys

支持的条件参数：

|     参数     |  类型  |            介绍            |
| :----------: | :----: | :------------------------: |
|   pageNum    |  int   |       页数（默认1）        |
|   pageSize   |  int   |     每页条数（默认10）     |
|     type     |  int   |          报名类型          |
| paymentState |  int   |          支付状态          |
| studentName  | String |          学生姓名          |
| studentPhone | String |        学生联系方式        |
| subjectName  | String |          课程名称          |
| subjectLevel |  int   |          课程阶段          |
|  startDate   | String |          起始日期          |
|   endDate    | String |          结束日期          |
|   isFirst    |  int   |     是否续保 0-否 1-是     |
|   referrer   |  int   |           介绍人           |
|    state     |  int   | 报名状态 0-待处理 1-已处理 |

#### 2.6 缴费 