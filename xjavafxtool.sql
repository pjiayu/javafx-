/*
 Navicat Premium Data Transfer

 Source Server         : 本地
 Source Server Type    : MySQL
 Source Server Version : 80023
 Source Host           : localhost:3306
 Source Schema         : xjavafxtool

 Target Server Type    : MySQL
 Target Server Version : 80023
 File Encoding         : 65001

 Date: 05/09/2022 10:45:23
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for baseinfo
-- ----------------------------
DROP TABLE IF EXISTS `baseinfo`;
CREATE TABLE `baseinfo`  (
  `linkname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `linkurl` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `isshow` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `addtime` datetime NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of baseinfo
-- ----------------------------
INSERT INTO `baseinfo` VALUES ('gitee', 'https://gitee.com/', '1', '2022-09-02 20:09:51');
INSERT INTO `baseinfo` VALUES ('baidu', 'https://www.baidu.com/', '1', '2022-09-02 20:10:08');
INSERT INTO `baseinfo` VALUES ('csdn', 'https://www.csdn.net/', '1', '2022-09-02 20:39:52');
INSERT INTO `baseinfo` VALUES ('哔哩哔哩 (゜-゜)つロ 干杯~-bilibili', 'https://www.bilibili.com/', '1', '2022-09-02 23:19:13');
INSERT INTO `baseinfo` VALUES ('力扣', 'https://leetcode.cn/', '0', '2022-09-03 09:28:03');
INSERT INTO `baseinfo` VALUES ('知乎 - 有问题，就会有答案', 'https://www.zhihu.com/', '0', '2022-09-03 09:57:36');

SET FOREIGN_KEY_CHECKS = 1;
