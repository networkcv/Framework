/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50549
Source Host           : localhost:3306
Source Database       : yt_college

Target Server Type    : MYSQL
Target Server Version : 50549
File Encoding         : 65001

Date: 2020-03-16 00:14:23
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for apply
-- ----------------------------
DROP TABLE IF EXISTS `apply`;
CREATE TABLE `apply` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` int(11) DEFAULT NULL COMMENT '报名类型 0-体验报名 1-缴费报名',
  `payment_id` int(11) DEFAULT NULL COMMENT '支付明细id',
  `payment_state` int(11) DEFAULT NULL COMMENT '支付状态  1-未支付 2-已支付 3-已退款',
  `student_id` int(11) DEFAULT NULL COMMENT '学生id',
  `student_name` varchar(255) DEFAULT NULL COMMENT '学生姓名',
  `student_age` int(11) DEFAULT NULL,
  `student_phone` varchar(255) DEFAULT NULL,
  `subject_id` int(11) DEFAULT NULL COMMENT '课程id',
  `subject_name` varchar(255) DEFAULT NULL COMMENT '课程名称',
  `subject_level` int(11) DEFAULT NULL COMMENT '课程阶段',
  `subject_cost` double(255,2) DEFAULT NULL COMMENT '课程价目',
  `real_cost` double(255,2) DEFAULT NULL COMMENT '实付金额',
  `date` varchar(255) DEFAULT NULL COMMENT '报名时间',
  `is_first` int(11) DEFAULT NULL COMMENT '是否续保 0-否 1-是',
  `referrer` int(255) DEFAULT NULL COMMENT '介绍人',
  `state` int(11) DEFAULT NULL COMMENT '报名状态 0-待处理 1-已处理',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of apply
-- ----------------------------
INSERT INTO `apply` VALUES ('3', '0', null, null, '5', 'Jack马', null, '15229265350', '1', 'EV3初级', '1', '120.00', null, '2020-03-16', '1', null, '0');

-- ----------------------------
-- Table structure for attendance
-- ----------------------------
DROP TABLE IF EXISTS `attendance`;
CREATE TABLE `attendance` (
  `id` int(11) NOT NULL,
  `classhour_id` int(11) DEFAULT NULL COMMENT '课时id',
  `student_id` int(11) DEFAULT NULL COMMENT '学生id',
  `state` int(11) DEFAULT NULL COMMENT '状态 0-出勤 1-缺勤',
  `expression` int(11) DEFAULT NULL COMMENT '表现 1~5分',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of attendance
-- ----------------------------

-- ----------------------------
-- Table structure for classes
-- ----------------------------
DROP TABLE IF EXISTS `classes`;
CREATE TABLE `classes` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `subject_id` int(11) DEFAULT NULL,
  `teacher_id` int(11) DEFAULT NULL COMMENT '教师 id',
  `classroom_id` int(11) DEFAULT NULL COMMENT '教室 id',
  `total_hours` int(11) DEFAULT NULL COMMENT '总课时',
  `finish_hours` int(11) DEFAULT NULL COMMENT '已完成课时',
  `start_time` varchar(255) DEFAULT NULL COMMENT '上课时间',
  `end_time` varchar(255) DEFAULT NULL COMMENT '下课时间',
  `start_date` varchar(255) DEFAULT NULL COMMENT '开始日期',
  `end_date` varchar(255) DEFAULT NULL COMMENT '结束日期',
  `state` int(10) unsigned zerofill DEFAULT NULL COMMENT '班级状态 0-进行中 1-已结束 2-已中断',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of classes
-- ----------------------------
INSERT INTO `classes` VALUES ('1', null, '1', '1', '0', '1', '1', '1', '1', '1', '1', '0000000001');

-- ----------------------------
-- Table structure for classes_student
-- ----------------------------
DROP TABLE IF EXISTS `classes_student`;
CREATE TABLE `classes_student` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `studnet_id` int(11) DEFAULT NULL,
  `classes_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of classes_student
-- ----------------------------

-- ----------------------------
-- Table structure for classhour
-- ----------------------------
DROP TABLE IF EXISTS `classhour`;
CREATE TABLE `classhour` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `classes_id` int(11) DEFAULT NULL COMMENT '班级id',
  `teacher_id` int(11) DEFAULT NULL COMMENT '老师id',
  `sign` int(11) DEFAULT NULL COMMENT '签到人数',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of classhour
-- ----------------------------

-- ----------------------------
-- Table structure for classroom
-- ----------------------------
DROP TABLE IF EXISTS `classroom`;
CREATE TABLE `classroom` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL COMMENT '教室备注',
  `max` int(11) NOT NULL COMMENT '最大使用人数',
  `address` varchar(255) DEFAULT NULL COMMENT '教室地点',
  `remark` varchar(255) DEFAULT NULL,
  `state` int(11) DEFAULT NULL COMMENT '开放状态 0-未开放 1-已开放',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of classroom
-- ----------------------------
INSERT INTO `classroom` VALUES ('1', 'A', '10', '云童馆内', '无', '1');
INSERT INTO `classroom` VALUES ('2', 'B', '8', '云童馆内', '无', '1');

-- ----------------------------
-- Table structure for evaluate
-- ----------------------------
DROP TABLE IF EXISTS `evaluate`;
CREATE TABLE `evaluate` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `student_id` int(11) DEFAULT NULL,
  `student_name` varchar(255) DEFAULT NULL,
  `teacher_id` int(11) DEFAULT NULL,
  `teacher_name` varchar(255) DEFAULT NULL,
  `subject_id` int(11) DEFAULT NULL,
  `subject_name` varchar(255) DEFAULT NULL,
  `classes_id` int(11) DEFAULT NULL,
  `classes_name` varchar(255) DEFAULT NULL,
  `type` int(11) DEFAULT NULL COMMENT '评价类型 0-学生评价老师 1-老师评价学生',
  `value` int(11) DEFAULT NULL COMMENT '评价分值',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of evaluate
-- ----------------------------

-- ----------------------------
-- Table structure for payment
-- ----------------------------
DROP TABLE IF EXISTS `payment`;
CREATE TABLE `payment` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` int(11) DEFAULT NULL COMMENT '支付类型 0-现金转账 1-在线支付',
  `state` int(11) DEFAULT NULL COMMENT '支付状态 1-未支付 2-已支付 3-已退款',
  `serial_number` bigint(11) DEFAULT NULL COMMENT '流水号',
  `money` double DEFAULT NULL COMMENT '金额',
  `operator` int(11) DEFAULT NULL COMMENT '操作人 现金转账时填写',
  `time` varchar(255) DEFAULT NULL COMMENT '操作时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of payment
-- ----------------------------

-- ----------------------------
-- Table structure for score
-- ----------------------------
DROP TABLE IF EXISTS `score`;
CREATE TABLE `score` (
  `id` int(255) NOT NULL AUTO_INCREMENT,
  `student_id` int(11) DEFAULT NULL COMMENT '学生id',
  `type` int(11) DEFAULT NULL COMMENT '积分类型',
  `value` int(11) DEFAULT NULL COMMENT '积分数值',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of score
-- ----------------------------

-- ----------------------------
-- Table structure for student
-- ----------------------------
DROP TABLE IF EXISTS `student`;
CREATE TABLE `student` (
  `id` int(255) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL COMMENT '学生姓名',
  `sex` int(1) DEFAULT NULL COMMENT '学生性别',
  `age` int(11) DEFAULT NULL COMMENT '学生年龄',
  `phone` varchar(255) DEFAULT NULL COMMENT '联系方式',
  `phone2` varchar(255) DEFAULT NULL COMMENT '联系方式2',
  `subject_id` int(255) DEFAULT NULL COMMENT '当前学习课程id',
  `subject_name` varchar(255) DEFAULT NULL,
  `score` int(255) DEFAULT NULL COMMENT '总积分',
  `referrer` int(11) DEFAULT NULL COMMENT '介绍人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of student
-- ----------------------------
INSERT INTO `student` VALUES ('5', 'Jack马', null, null, '15229265350', null, null, null, null, null);

-- ----------------------------
-- Table structure for subject
-- ----------------------------
DROP TABLE IF EXISTS `subject`;
CREATE TABLE `subject` (
  `id` int(255) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL COMMENT '课程名称',
  `hours` int(11) DEFAULT NULL,
  `subject_level` int(10) DEFAULT NULL COMMENT '课程阶段',
  `state` int(10) DEFAULT NULL COMMENT '课程状态 0-上线 1-下线 2-待发布',
  `cost` double(255,2) DEFAULT NULL COMMENT '课程费用',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of subject
-- ----------------------------
INSERT INTO `subject` VALUES ('1', 'EV3初级', '18', '1', '2', '120.00');
INSERT INTO `subject` VALUES ('2', 'EV3中级', '20', '2', '2', '220.00');

-- ----------------------------
-- Table structure for teacher
-- ----------------------------
DROP TABLE IF EXISTS `teacher`;
CREATE TABLE `teacher` (
  `id` int(255) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL COMMENT '姓名',
  `sex` int(1) DEFAULT NULL COMMENT '性别',
  `age` int(11) DEFAULT NULL COMMENT '年龄',
  `phone` varchar(255) DEFAULT NULL COMMENT '手机号',
  `password` varchar(255) DEFAULT NULL COMMENT '登录密码',
  `post` int(11) DEFAULT NULL COMMENT '岗位',
  `level` int(11) DEFAULT NULL COMMENT '级别',
  `total_hours` int(11) DEFAULT '0' COMMENT '总课时',
  `numbers` int(11) DEFAULT '0' COMMENT '总人数',
  `score` double(11,2) DEFAULT '0.00' COMMENT '教学质量评分',
  `renewal_rate` double(11,2) DEFAULT '0.00' COMMENT '续保率',
  `is_delete` int(1) DEFAULT '0' COMMENT '是否离职 0-在职 1-离职',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of teacher
-- ----------------------------
INSERT INTO `teacher` VALUES ('1', 'jack', '1', '20', '15229265350', '123456', '6', '6', null, null, null, null, '0');
INSERT INTO `teacher` VALUES ('2', 'rose', '0', '18', '15596652300', '123456', '6', '5', null, null, null, null, '0');

-- ----------------------------
-- Table structure for trial
-- ----------------------------
DROP TABLE IF EXISTS `trial`;
CREATE TABLE `trial` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL COMMENT '姓名',
  `phone` varchar(255) DEFAULT NULL COMMENT '联系方式',
  `subject_id` int(11) DEFAULT NULL COMMENT '课程id',
  `state` int(11) unsigned zerofill DEFAULT NULL COMMENT '状态 0-待处理 1-已处理 2-已体验',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of trial
-- ----------------------------
