/*
 Navicat Premium Data Transfer

 Source Server         : Flattie
 Source Server Type    : MySQL
 Source Server Version : 50633
 Source Host           : moeby.ch:3306
 Source Schema         : moebych_Flattie

 Target Server Type    : MySQL
 Target Server Version : 50633
 File Encoding         : 65001

 Date: 09/01/2018 22:11:04
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for calendar_item
-- ----------------------------
DROP TABLE IF EXISTS `calendar_item`;
CREATE TABLE `calendar_item`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `description` text CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL,
  `repeatable` enum('none','daily','weekly','monthly','yearly') CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `start_datetime` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
  `end_datetime` timestamp(0) NULL DEFAULT NULL,
  `group_fk` int(11) NOT NULL,
  `event_category_fk` int(11) NOT NULL,
  `user_fk` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_calendar_item_group1_idx`(`group_fk`) USING BTREE,
  INDEX `fk_calendar_item_event_category1_idx`(`event_category_fk`) USING BTREE,
  INDEX `fk_calendar_item_user1_idx`(`user_fk`) USING BTREE,
  CONSTRAINT `fk_calendar_item_event_category1` FOREIGN KEY (`event_category_fk`) REFERENCES `event_category` (`id`) ON DELETE NO ACTION ON UPDATE CASCADE,
  CONSTRAINT `fk_calendar_item_group1` FOREIGN KEY (`group_fk`) REFERENCES `group` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_calendar_item_user1` FOREIGN KEY (`user_fk`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for event_category
-- ----------------------------
DROP TABLE IF EXISTS `event_category`;
CREATE TABLE `event_category`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of event_category
-- ----------------------------
INSERT INTO `event_category` VALUES (1, 'event');
INSERT INTO `event_category` VALUES (2, 'absence');
INSERT INTO `event_category` VALUES (3, 'duty');

-- ----------------------------
-- Table structure for group
-- ----------------------------
DROP TABLE IF EXISTS `group`;
CREATE TABLE `group`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `removal_date` date NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for rep_event_exception
-- ----------------------------
DROP TABLE IF EXISTS `rep_event_exception`;
CREATE TABLE `rep_event_exception`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `start_datetime` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
  `end_datetime` timestamp(0) NULL DEFAULT NULL,
  `skipped` tinyint(1) NOT NULL,
  `calendar_item_fk` int(11) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_rep_event_exeption_calendar_item1_idx`(`calendar_item_fk`) USING BTREE,
  CONSTRAINT `fk_rep_event_exeption_calendar_item1` FOREIGN KEY (`calendar_item_fk`) REFERENCES `calendar_item` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for shopping_item
-- ----------------------------
DROP TABLE IF EXISTS `shopping_item`;
CREATE TABLE `shopping_item`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `bought` tinyint(1) NOT NULL,
  `group_fk` int(11) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_shopping_item_group1_idx`(`group_fk`) USING BTREE,
  CONSTRAINT `fk_shopping_item_group1` FOREIGN KEY (`group_fk`) REFERENCES `group` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `email` varchar(45) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `username` varchar(45) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `password` varchar(60) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `removal_date` date NULL DEFAULT NULL,
  `group_fk` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `email_UNIQUE`(`email`) USING BTREE,
  UNIQUE INDEX `username_UNIQUE`(`username`) USING BTREE,
  INDEX `fk_user_group_idx`(`group_fk`) USING BTREE,
  CONSTRAINT `fk_user_group` FOREIGN KEY (`group_fk`) REFERENCES `group` (`id`) ON DELETE NO ACTION ON UPDATE CASCADE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Compact;

SET FOREIGN_KEY_CHECKS = 1;
