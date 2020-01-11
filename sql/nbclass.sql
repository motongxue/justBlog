/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50642
Source Host           : localhost:3306
Source Database       : zb-blog

Target Server Type    : MYSQL
Target Server Version : 50642
File Encoding         : 65001

Date: 2019-12-26 17:37:39
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for blog_article
-- ----------------------------
DROP TABLE IF EXISTS `blog_article`;
CREATE TABLE `blog_article` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) DEFAULT NULL COMMENT '分类名称',
  `intro` varchar(255) DEFAULT NULL,
  `cover` varchar(255) DEFAULT NULL COMMENT '封面图',
  `content` longtext CHARACTER SET utf8 COMMENT '文章内容',
  `content_md` longtext CHARACTER SET utf8 COMMENT 'markdown版的文章内容',
  `editor_type` tinyint(1) DEFAULT NULL COMMENT '1-富文本，2-markdown',
  `alias_name` varchar(50) DEFAULT NULL COMMENT '路径别名',
  `category_id` int(11) DEFAULT NULL COMMENT '分类id',
  `is_top` tinyint(1) DEFAULT NULL COMMENT '是否置顶，1-是，0-否',
  `is_rec` tinyint(1) DEFAULT NULL COMMENT '是否推荐，1-是，0-否',
  `is_public` tinyint(1) DEFAULT NULL COMMENT '1-公开，2-私密',
  `is_comment` tinyint(1) DEFAULT NULL COMMENT '开启评论：1-是，2-否',
  `type` tinyint(1) DEFAULT '1' COMMENT '文章类型，1-图文，2-视频',
  `video` varchar(255) DEFAULT NULL COMMENT '视频地址',
  `origin` tinyint(1) DEFAULT '1' COMMENT '1-原创，2-转载',
  `look_num` int(10) DEFAULT '0' COMMENT '阅读数',
  `support_num` int(10) DEFAULT '0' COMMENT '支持（赞）数',
  `oppose_num` int(10) DEFAULT '0' COMMENT '反对（踩）',
  `comment_num` int(10) DEFAULT '0' COMMENT '评论数',
  `template` varchar(50) DEFAULT NULL COMMENT '模板',
  `seo_key` varchar(255) DEFAULT NULL,
  `seo_desc` varchar(255) DEFAULT NULL,
  `status` tinyint(1) DEFAULT NULL COMMENT '用户状态：1有效; 0-草稿',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of blog_article
-- ----------------------------

-- ----------------------------
-- Table structure for blog_article_tag
-- ----------------------------
DROP TABLE IF EXISTS `blog_article_tag`;
CREATE TABLE `blog_article_tag` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `tag_id` int(11) unsigned NOT NULL COMMENT '标签表主键',
  `article_id` int(11) unsigned NOT NULL COMMENT '文章ID',
  `status` tinyint(1) DEFAULT NULL,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '添加时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of blog_article_tag
-- ----------------------------

-- ----------------------------
-- Table structure for blog_category
-- ----------------------------
DROP TABLE IF EXISTS `blog_category`;
CREATE TABLE `blog_category` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `pid` int(11) DEFAULT NULL,
  `name` varchar(50) DEFAULT NULL COMMENT '分类名称',
  `alias_name` varchar(50) DEFAULT NULL COMMENT '分类别名',
  `type` tinyint(1) DEFAULT NULL COMMENT '0-目录，1-文章栏目，2-页面',
  `sort` int(5) DEFAULT NULL COMMENT '排序',
  `icon` varchar(50) DEFAULT NULL COMMENT '图标',
  `template` varchar(50) DEFAULT NULL COMMENT '模板名称',
  `description` varchar(255) DEFAULT NULL COMMENT '分类描述',
  `seo_title` varchar(255) DEFAULT NULL,
  `seo_key` varchar(255) DEFAULT NULL,
  `seo_desc` varchar(255) DEFAULT NULL,
  `status` tinyint(1) DEFAULT '1' COMMENT '用户状态：1有效; 0删除',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of blog_category
-- ----------------------------
INSERT INTO `blog_category` VALUES ('1', null, '首页', '/', '2', '1', '', 'index', null, null, null, null, '1', '2019-12-27 11:43:02', '2019-12-27 11:45:07');
-- ----------------------------
-- Table structure for blog_comment
-- ----------------------------
DROP TABLE IF EXISTS `blog_comment`;
CREATE TABLE `blog_comment` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `sid` int(11) DEFAULT NULL COMMENT '被评论的文章或者页面的ID(-1:留言板)',
  `mid` int(11) DEFAULT NULL COMMENT '主评论id',
  `parent_id` int(11) DEFAULT NULL,
  `parent_nickname` varchar(50) DEFAULT NULL,
  `qq` varchar(20) DEFAULT NULL COMMENT '评论人的QQ（未登录用户）',
  `nickname` varchar(50) DEFAULT NULL COMMENT '评论人的昵称（未登录用户）',
  `avatar` varchar(255) DEFAULT NULL COMMENT '评论人的头像地址',
  `email` varchar(100) DEFAULT NULL COMMENT '评论人的邮箱地址（未登录用户）',
  `ip` varchar(20) DEFAULT NULL COMMENT '评论时的ip',
  `os` varchar(50) DEFAULT NULL COMMENT '评论时的系统类型',
  `browser` varchar(50) DEFAULT NULL COMMENT '评论时的浏览器类型',
  `content` text COMMENT '评论的内容',
  `floor` int(10) DEFAULT NULL COMMENT '楼层',
  `support_num` int(10) DEFAULT '0' COMMENT '支持（赞）数',
  `oppose_num` int(10) DEFAULT '0' COMMENT '反对（踩）',
  `status` tinyint(1) DEFAULT '0' COMMENT '0-待审核，1-通过，2-驳回',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '添加时间',
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of blog_comment
-- ----------------------------

-- ----------------------------
-- Table structure for blog_config
-- ----------------------------
DROP TABLE IF EXISTS `blog_config`;
CREATE TABLE `blog_config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `sys_key` varchar(50) DEFAULT NULL COMMENT 'key',
  `sys_value` varchar(2000) DEFAULT NULL COMMENT 'value',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态   1-有效，0-无效',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `key` (`sys_key`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8 COMMENT='系统配置信息表';

-- ----------------------------
-- Records of blog_config
-- ----------------------------
INSERT INTO `blog_config` VALUES ('1', 'CONFIG_STORAGE', '{\"setFlag\":0,\"type\":0,\"localDomain\":\"http://localhost:8088\",\"localPrefix\":\"\",\"qiniuDomain\":\"http://cdn.nbclass.com\",\"qiniuPrefix\":\"img/blog\",\"qiniuAccessKey\":\"dGKQzLej_0__Xd9kElc7IK-BXFTs\",\"qiniuSecretKey\":\"8sZZbdmEDS4yVx0DCF_-Das\",\"qiniuBucketName\":\"xxx\",\"aliyunDomain\":\"https://xxx.oss-cn-hangzhou.aliyuncs.com\",\"aliyunPrefix\":\"blog/\",\"aliyunEndPoint\":\"oss-cn-hangzhou.aliyuncs.com\",\"aliyunAccessKeyId\":\"LTAI4FgN4uWUatpHLxxjXmv1\",\"aliyunAccessKeySecret\":\"OJTXOBiLxxpKCAMURVlonFGdHb1n1\",\"aliyunBucketName\":\"xxx-img\",\"qcloudDomain\":\"https://xxx.cos.ap-shanghai.myqcloud.com\",\"qcloudPrefix\":\"blog/\",\"qcloudSecretId\":\"AKIDrxxxgT2fMDNHud5HDtOTJlACx\",\"qcloudSecretKey\":\"iBSZP6WifMn3A1cxxxwULwH\",\"qcloudBucketName\":\"sttbaxxx\",\"qcloudRegion\":\"ap-shanghai\"}', '云存储配置信息', '1', '2019-12-25 18:21:53', '2019-12-25 18:21:54');
INSERT INTO `blog_config` VALUES ('2', 'SITE_HOST', 'http://localhost:8088', '网站域名', '1', '2019-12-25 18:21:53', '2019-12-25 18:22:00');
INSERT INTO `blog_config` VALUES ('3', 'SITE_CDN', '', 'CDN域名', '1', '2019-12-25 18:21:53', '2019-12-25 18:22:01');
INSERT INTO `blog_config` VALUES ('4', 'SITE_NAME', 'JustBlog', '网站名称', '1', '2019-12-25 18:21:53', '2019-12-25 18:22:02');
INSERT INTO `blog_config` VALUES ('5', 'SITE_KWD', 'JustBlog,博客模板,nbclass,zb-blog,个人博客，开源博客，shiro,zb-shiro,权限管理项目', '网站关键字', '1', '2019-12-25 18:21:53', '2019-12-25 18:22:03');
INSERT INTO `blog_config` VALUES ('6', 'SITE_DESC', 'JustBlog,博客模板,nbclass,zb-blog,个人博客，开源博客，shiro,zb-shiro,权限管理项目', '网站描述', '1', '2019-12-25 18:21:53', '2019-12-25 18:22:03');
INSERT INTO `blog_config` VALUES ('7', 'SITE_ICON', '/static/img/logo-color.png', '网站ico', '1', '2019-12-25 18:21:53', '2019-12-25 18:23:04');
INSERT INTO `blog_config` VALUES ('8', 'SITE_LOGO', '/static/img/logo-color.png', '网站logo', '1', '2019-12-25 18:21:53', '2019-12-25 18:23:06');
INSERT INTO `blog_config` VALUES ('9', 'EDITOR_TYPE', '2', '编辑器类型', '1', '2019-12-25 18:21:53', '2019-12-26 17:03:07');
INSERT INTO `blog_config` VALUES ('10', 'SYSTEM_PAGE_VIEW', '0', '系统访问数', '1', '2019-12-25 18:21:53', '2019-12-26 17:03:07');
INSERT INTO `blog_config` VALUES ('11', 'SYSTEM_CREATE_TIME', '', '系统创建时间', '1', '2019-12-25 18:21:53', '2019-12-26 17:03:07');
INSERT INTO `blog_config` VALUES ('12', 'SYSTEM_IS_SET', '0', '系统是否设置', '1', '2019-12-25 18:21:53', '2019-12-26 17:03:07');
INSERT INTO `blog_config` VALUES ('13', 'CONFIG_EMAIL', '{\"setFlag\":0,\"host\":\"\",\"port\":\"\",\"username\":\"\",\"password\":\"\",\"from\":\"\"}', '邮件配置', '1', '2019-12-25 18:21:53', '2019-12-25 18:21:53');
INSERT INTO `blog_config` VALUES ('14', 'RESET_PWD_TYPE', '1', '重置密码方式', '1', '2019-12-25 18:21:53', '2019-12-26 17:03:07');
INSERT INTO `blog_config` VALUES ('15', 'SECURITY_CODE', '666666', '安全码', '1', '2019-12-25 18:21:53', '2019-12-26 17:03:07');


-- ----------------------------
-- Table structure for blog_file
-- ----------------------------
DROP TABLE IF EXISTS `blog_file`;
CREATE TABLE `blog_file` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `original_name` varchar(255) DEFAULT NULL COMMENT '原始文件名称',
  `file_name` varchar(50) DEFAULT NULL COMMENT '文件名称',
  `file_type` varchar(20) DEFAULT NULL COMMENT '文件类型',
  `file_size` varchar(20) DEFAULT NULL COMMENT '友链图片地址',
  `file_path` varchar(255) DEFAULT NULL COMMENT '文件相对路径',
  `file_full_path` varchar(255) DEFAULT NULL COMMENT '文件绝对路径',
  `file_hash` varchar(255) DEFAULT NULL,
  `oss_type` tinyint(1) DEFAULT NULL COMMENT 'oss存储类型',
  `status` tinyint(1) DEFAULT NULL COMMENT '状态',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '添加时间',
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of blog_file
-- ----------------------------

-- ----------------------------
-- Table structure for blog_link
-- ----------------------------
DROP TABLE IF EXISTS `blog_link`;
CREATE TABLE `blog_link` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL COMMENT '链接名',
  `url` varchar(200) NOT NULL COMMENT '链接地址',
  `description` varchar(300) DEFAULT NULL COMMENT '链接介绍',
  `img` varchar(255) DEFAULT NULL COMMENT '友链图片地址',
  `email` varchar(100) DEFAULT NULL COMMENT '友链站长邮箱',
  `qq` varchar(50) DEFAULT NULL COMMENT '友链站长qq',
  `origin` tinyint(1) DEFAULT NULL COMMENT '1-管理员添加 2-自助申请',
  `status` tinyint(1) DEFAULT NULL COMMENT '状态',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '添加时间',
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of blog_link
-- ----------------------------
INSERT INTO `blog_link` VALUES ('1', 'JustBlog', 'https://www.nbclass.com', 'JustBlog', '', '', '', '1', '1', '2019-12-26 17:24:30', '2019-12-26 17:24:30');

-- ----------------------------
-- Table structure for blog_slider
-- ----------------------------
DROP TABLE IF EXISTS `blog_slider`;
CREATE TABLE `blog_slider` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(100) DEFAULT NULL COMMENT '内容',
  `img` varchar(255) DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL COMMENT '是否有链接',
  `status` tinyint(1) DEFAULT NULL COMMENT '1-有效，0-无效',
  `type` tinyint(4) DEFAULT NULL COMMENT '1-系统公告，2-轮播',
  `create_time` timestamp NULL DEFAULT NULL,
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of blog_slider
-- ----------------------------

-- ----------------------------
-- Table structure for blog_tag
-- ----------------------------
DROP TABLE IF EXISTS `blog_tag`;
CREATE TABLE `blog_tag` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL COMMENT '标签名称',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态：1有效; 0删除',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of blog_tag
-- ----------------------------

-- ----------------------------
-- Table structure for blog_user
-- ----------------------------
DROP TABLE IF EXISTS `blog_user`;
CREATE TABLE `blog_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` varchar(16) NOT NULL COMMENT '用户id',
  `username` varchar(50) DEFAULT NULL COMMENT '用户名',
  `password` varchar(50) DEFAULT NULL,
  `salt` varchar(128) DEFAULT NULL COMMENT '加密盐值',
  `nickname` varchar(50) DEFAULT NULL COMMENT '昵称',
  `email` varchar(50) DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(20) DEFAULT NULL COMMENT '电话',
  `qq` varchar(20) DEFAULT NULL,
  `avatar` varchar(255) DEFAULT NULL COMMENT '头像地址',
  `last_login_time` timestamp NULL DEFAULT NULL COMMENT '最后登录时间',
  `status` tinyint(1) DEFAULT '1' COMMENT '用户状态：1有效; 0删除',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`,`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of blog_user
-- ----------------------------
INSERT INTO `blog_user` VALUES ('1', '1000000862441216', 'admin', '8fd06f8c1572f52ac9df72398d8fe9ca', '097cf4b9a18844b08b452d4ebefc1de0', 'admin', '523179414@qq.com', null, '523179414', '/static/img/person.jpg', null, '1', '2019-10-16 11:50:10', '2019-10-16 11:54:42');
