/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50540
 Source Host           : localhost:3306
 Source Schema         : yt_college

 Target Server Type    : MySQL
 Target Server Version : 50540
 File Encoding         : 65001

 Date: 14/03/2020 18:32:37
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for apply
-- ----------------------------
DROP TABLE IF EXISTS `apply`;
CREATE TABLE `apply`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `payment_id` int(11) NULL DEFAULT NULL COMMENT '支付明细id',
  `student_id` int(11) NULL DEFAULT NULL COMMENT '学生id',
  `student_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '学生姓名',
  `subject_id` int(11) NULL DEFAULT NULL COMMENT '课程id',
  `subject_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '课程名称',
  `subject_level_state` int(11) NULL DEFAULT NULL COMMENT '课程阶段',
  `subject_cost` double(255, 2) NULL DEFAULT NULL COMMENT '课程价目',
  `real_cost` double(255, 2) NULL DEFAULT NULL COMMENT '实付金额',
  `time` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '报名时间',
  `is_first` int(11) NULL DEFAULT NULL COMMENT '是否续保 0-否 1-是',
  `referrer` int(255) NULL DEFAULT NULL COMMENT '介绍人',
  `state` int(11) NULL DEFAULT NULL COMMENT '报名状态 0-已报名 1-未支付 2-已支付 3-已退款',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for attendance
-- ----------------------------
DROP TABLE IF EXISTS `attendance`;
CREATE TABLE `attendance`  (
  `id` int(11) NOT NULL,
  `classhour_id` int(11) NULL DEFAULT NULL COMMENT '课时id',
  `student_id` int(11) NULL DEFAULT NULL COMMENT '学生id',
  `state` int(11) NULL DEFAULT NULL COMMENT '状态 0-出勤 1-缺勤',
  `expression` int(11) NULL DEFAULT NULL COMMENT '表现 1~5分',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for classes
-- ----------------------------
DROP TABLE IF EXISTS `classes`;
CREATE TABLE `classes`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `subject_id` int(11) NULL DEFAULT NULL,
  `teacher_id` int(255) NULL DEFAULT NULL COMMENT '教师 id',
  `classroom_id` int(255) NULL DEFAULT NULL COMMENT '教室 id',
  `total_hours` int(255) NULL DEFAULT NULL COMMENT '总课时',
  `finish_hours` int(11) NULL DEFAULT NULL COMMENT '已完成课时',
  `start_time` varchar(0) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '上课时间',
  `end_time` varchar(0) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '下课时间',
  `start_date` varchar(0) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '开始日期',
  `end_date` varchar(0) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '结束日期',
  `state` int(10) UNSIGNED ZEROFILL NULL DEFAULT NULL COMMENT '班级状态 0-进行中 1-已结束 2-已中断',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for classes_student
-- ----------------------------
DROP TABLE IF EXISTS `classes_student`;
CREATE TABLE `classes_student`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `studnet_id` int(11) NULL DEFAULT NULL,
  `classes_id` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for classhour
-- ----------------------------
DROP TABLE IF EXISTS `classhour`;
CREATE TABLE `classhour`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `classes_id` int(11) NULL DEFAULT NULL COMMENT '班级id',
  `teacher_id` int(11) NULL DEFAULT NULL COMMENT '老师id',
  `sign` int(11) NULL DEFAULT NULL COMMENT '签到人数',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for classroom
-- ----------------------------
DROP TABLE IF EXISTS `classroom`;
CREATE TABLE `classroom`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '教室备注',
  `place` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '教室地点',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for payment
-- ----------------------------
DROP TABLE IF EXISTS `payment`;
CREATE TABLE `payment`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` int(11) NULL DEFAULT NULL COMMENT '支付类型 0-现金转账 1-在线支付',
  `state` int(11) NULL DEFAULT NULL COMMENT '支付状态 1-未支付 2-已支付 3-已退款',
  `serial_number` bigint(11) NULL DEFAULT NULL COMMENT '流水号',
  `money` double NULL DEFAULT NULL COMMENT '金额',
  `operator` int(11) NULL DEFAULT NULL COMMENT '操作人 现金转账时填写',
  `time` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for score
-- ----------------------------
DROP TABLE IF EXISTS `score`;
CREATE TABLE `score`  (
  `id` int(255) NOT NULL AUTO_INCREMENT,
  `student_id` int(11) NULL DEFAULT NULL COMMENT '学生id',
  `type` int(11) NULL DEFAULT NULL COMMENT '积分类型',
  `value` int(11) NULL DEFAULT NULL COMMENT '积分数值',
  `remark` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for student
-- ----------------------------
DROP TABLE IF EXISTS `student`;
CREATE TABLE `student`  (
  `id` int(255) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '学生姓名',
  `sex` int(1) NULL DEFAULT NULL COMMENT '学生性别',
  `age` int(11) NULL DEFAULT NULL COMMENT '学生年龄',
  `phone` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联系方式',
  `phone2` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联系方式2',
  `subject_id` int(255) NULL DEFAULT NULL COMMENT '当前学习课程id',
  `score` int(255) NULL DEFAULT NULL COMMENT '总积分',
  `referrer` int(11) NULL DEFAULT NULL COMMENT '介绍人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of student
-- ----------------------------
INSERT INTO `student` VALUES (1, '1', 3, 1, '1', '1', 1, 1, 1);

-- ----------------------------
-- Table structure for subject
-- ----------------------------
DROP TABLE IF EXISTS `subject`;
CREATE TABLE `subject`  (
  `id` int(255) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '课程名称',
  `subject_level_state` int(10) NULL DEFAULT NULL COMMENT '课程阶段状态\r\n',
  `state` int(10) UNSIGNED ZEROFILL NULL DEFAULT NULL COMMENT '课程状态 0-上线 1-下线 2-待发布',
  `cost` double(255, 2) NULL DEFAULT NULL COMMENT '课程费用',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for teacher
-- ----------------------------
DROP TABLE IF EXISTS `teacher`;
CREATE TABLE `teacher`  (
  `id` int(255) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '姓名',
  `sex` int(1) NULL DEFAULT NULL COMMENT '性别',
  `age` int(11) NULL DEFAULT NULL COMMENT '年龄',
  `phone` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '登录密码',
  `post` int(11) NULL DEFAULT NULL COMMENT '岗位',
  `level` int(11) NULL DEFAULT NULL COMMENT '级别',
  `total_hours` int(11) NULL DEFAULT 0 COMMENT '总课时',
  `numbers` int(11) NULL DEFAULT 0 COMMENT '总人数',
  `score` double(11, 2) NULL DEFAULT 0.00 COMMENT '教学质量评分',
  `renewal_rate` double(11, 2) NULL DEFAULT 0.00 COMMENT '续保率',
  `is_delete` int(1) NULL DEFAULT NULL COMMENT '是否离职 0-在职 1-离职',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of teacher
-- ----------------------------
INSERT INTO `teacher` VALUES (1, 'jack', 1, 20, '15229265350', '123456', 6, 6, NULL, NULL, NULL, NULL, 0);
INSERT INTO `teacher` VALUES (2, 'rose', 0, 18, '15596652300', '123456', 6, 5, NULL, NULL, NULL, NULL, 0);

-- ----------------------------
-- Table structure for tmp_student
-- ----------------------------
DROP TABLE IF EXISTS `tmp_student`;
CREATE TABLE `tmp_student`  (
  `id` int(255) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '姓名',
  `phone` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '联系方式',
  `subject_id` int(11) NULL DEFAULT NULL COMMENT '课程id',
  `state` int(11) UNSIGNED ZEROFILL NULL DEFAULT NULL COMMENT '状态 0-待处理 1-已处理 2-已体验',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

SET FOREIGN_KEY_CHECKS = 1;
