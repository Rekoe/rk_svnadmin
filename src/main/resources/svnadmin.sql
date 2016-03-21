-- MySQL dump 10.13  Distrib 5.1.56, for unknown-linux-gnu (x86_64)
--
-- Host: localhost    Database: svnadmin
-- ------------------------------------------------------
-- Server version	5.1.56-LTOPS-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Current Database: `svnadmin`
--

/*!40000 DROP DATABASE IF EXISTS `svnadmin`*/;

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `svnadmin` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `svnadmin`;

--
-- Table structure for table `i18n`
--

DROP TABLE IF EXISTS `i18n`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `i18n` (
  `lang` varchar(20) NOT NULL,
  `id` varchar(200) NOT NULL,
  `lbl` varchar(200) NOT NULL,
  PRIMARY KEY (`lang`,`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `i18n`
--

LOCK TABLES `i18n` WRITE;
/*!40000 ALTER TABLE `i18n` DISABLE KEYS */;
INSERT INTO `i18n` VALUES ('zh_CN','i18n.add.title','增加语言'),('zh_CN','i18n.current','当前语言'),('zh_CN','i18n.error.lang','语言不可以为空'),('zh_CN','i18n.error.lbl','标签不可以为空'),('zh_CN','i18n.id','键值'),('zh_CN','i18n.lang','语言'),('zh_CN','i18n.lbl','标签'),('zh_CN','i18n.op.addlang','增加语言'),('zh_CN','i18n.op.export','导出多语言贡献给svnadmin项目组'),('zh_CN','i18n.op.submit','提交'),('zh_CN','i18n.title','语言管理'),('zh_CN','login.btn.login','登 录'),('zh_CN','login.btn.reset','重 置'),('zh_CN','login.error.wrongpassword','密码错误 '),('zh_CN','main.link.i18n','语言'),('zh_CN','main.link.logout','退出'),('zh_CN','main.link.pj','项目'),('zh_CN','main.link.user','用户'),('zh_CN','pj.btn.submit','提交'),('zh_CN','pj.des','描述'),('zh_CN','pj.error.path','路径不可以为空'),('zh_CN','pj.error.pj','项目不可以为空'),('zh_CN','pj.error.url','URL不可以为空'),('zh_CN','pj.op.delete','删除'),('zh_CN','pj.op.delete.confirm','确认删除?'),('zh_CN','pj.op.setauth','设置权限'),('zh_CN','pj.op.setgr','设置用户组'),('zh_CN','pj.op.setuser','设置用户'),('zh_CN','pj.path','路径'),('zh_CN','pj.pj','项目'),('zh_CN','pj.title','项目管理'),('zh_CN','pj.type','类型'),('zh_CN','pj.type.http','http'),('zh_CN','pj.type.http.mutil','http(多库)'),('zh_CN','pj.type.svn','svn'),('zh_CN','pj.url','URL'),('zh_CN','pjauth.btn.submit','保存'),('zh_CN','pjauth.error.grusr','请选择用户组或用户'),('zh_CN','pjauth.error.pj','项目不可以为空'),('zh_CN','pjauth.error.res','资源不可以为空'),('zh_CN','pjauth.op.delete','删除'),('zh_CN','pjauth.op.delete.confirm','确认删除?'),('zh_CN','pjauth.res','资源'),('zh_CN','pjauth.res.select','选择资源'),('zh_CN','pjauth.rw','权限'),('zh_CN','pjauth.rw.none','没有权限'),('zh_CN','pjauth.rw.r','可读'),('zh_CN','pjauth.rw.rw','可读可写'),('zh_CN','pjauth.title','权限管理'),('zh_CN','pjgr.btn.submit','提交'),('zh_CN','pjgr.error.gr','组号不可以为空'),('zh_CN','pjgr.error.pj','项目不可以为空'),('zh_CN','pjgr.op.delete','删除'),('zh_CN','pjgr.op.delete.confirm','确认删除?'),('zh_CN','pjgr.op.setuser','设置用户'),('zh_CN','pjgr.title','用户组管理'),('zh_CN','pjgrusr.error.usr','用户不可以为空'),('zh_CN','pjgrusr.op.delete','删除'),('zh_CN','pjgrusr.op.delete.confirm','确认删除?'),('zh_CN','pjgrusr.op.submit','增加用户'),('zh_CN','pjgrusr.title','项目组用户管理'),('zh_CN','pjusr.error.pj','项目不可以为空'),('zh_CN','pjusr.error.psw','项目新密码不可以为空'),('zh_CN','pjusr.error.usr','用户不可以为空'),('zh_CN','pjusr.info','(注意:这里设置的用户密码只对这个项目有效)'),('zh_CN','pjusr.op.delete','删除'),('zh_CN','pjusr.op.delete.confirm','确认删除?'),('zh_CN','pjusr.op.submit','提交'),('zh_CN','pjusr.psw.psw','项目新密码'),('zh_CN','pjusr.title','项目用户管理'),('zh_CN','pjusr.usr.select','选择用户'),('zh_CN','pj_gr.des','描述'),('zh_CN','pj_gr.gr','用户组'),('zh_CN','rep.btn.go','刷新'),('zh_CN','svn.auth.error','认证失败'),('zh_CN','svn.notFoundResp','找不到仓库 路径{0}'),('zh_CN','sys.error.timeout','超时或未登录'),('zh_CN','sys.lbl.no','NO.'),('zh_CN','usr.error.psw','密码不可以为空'),('zh_CN','usr.error.usr','用户不可以为空'),('zh_CN','usr.name','姓名'),('zh_CN','usr.op.delete','删除'),('zh_CN','usr.op.delete.confirm','确认删除?'),('zh_CN','usr.op.listauth','查看权限'),('zh_CN','usr.op.submit','提交'),('zh_CN','usr.psw','密码'),('zh_CN','usr.role','角色'),('zh_CN','usr.role.select','选择角色'),('zh_CN','usr.title','用户管理'),('zh_CN','usr.usr','用户名'),('zh_CN','zh_CN','中文');
/*!40000 ALTER TABLE `i18n` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pj`
--

DROP TABLE IF EXISTS `pj`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pj` (
  `pj` varchar(50) NOT NULL,
  `path` varchar(200) NOT NULL,
  `url` varchar(200) NOT NULL,
  `type` varchar(10) NOT NULL,
  `des` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`pj`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pj`
--

LOCK TABLES `pj` WRITE;
/*!40000 ALTER TABLE `pj` DISABLE KEYS */;
INSERT INTO `pj` VALUES ('app','/code/repository/app','http://192.168.3.127/repository/app','http-mutil','app project'),('dead','/code/svndata/dead','http://192.168.3.127/svndata/dead','http-mutil','rekoe desc');
/*!40000 ALTER TABLE `pj` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pj_gr`
--

DROP TABLE IF EXISTS `pj_gr`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pj_gr` (
  `pj` varchar(50) NOT NULL,
  `gr` varchar(50) NOT NULL,
  `des` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`pj`,`gr`),
  CONSTRAINT `FK_Relationship_2` FOREIGN KEY (`pj`) REFERENCES `pj` (`pj`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pj_gr`
--

LOCK TABLES `pj_gr` WRITE;
/*!40000 ALTER TABLE `pj_gr` DISABLE KEYS */;
INSERT INTO `pj_gr` VALUES ('app','developer','developer'),('app','manager','manager'),('app','tester','tester'),('dead','developer','developer'),('dead','manager','manager'),('dead','tester','tester');
/*!40000 ALTER TABLE `pj_gr` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pj_gr_auth`
--

DROP TABLE IF EXISTS `pj_gr_auth`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pj_gr_auth` (
  `pj` varchar(50) NOT NULL,
  `gr` varchar(50) NOT NULL,
  `res` varchar(200) NOT NULL,
  `rw` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`pj`,`res`,`gr`),
  KEY `FK_Reference_6` (`pj`,`gr`),
  CONSTRAINT `FK_Reference_6` FOREIGN KEY (`pj`, `gr`) REFERENCES `pj_gr` (`pj`, `gr`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pj_gr_auth`
--

LOCK TABLES `pj_gr_auth` WRITE;
/*!40000 ALTER TABLE `pj_gr_auth` DISABLE KEYS */;
INSERT INTO `pj_gr_auth` VALUES ('app','developer','[app:/]','rw'),('app','manager','[app:/]','rw'),('app','tester','[app:/]','rw');
/*!40000 ALTER TABLE `pj_gr_auth` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pj_gr_usr`
--

DROP TABLE IF EXISTS `pj_gr_usr`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pj_gr_usr` (
  `pj` varchar(50) NOT NULL,
  `gr` varchar(50) NOT NULL,
  `usr` varchar(50) NOT NULL,
  PRIMARY KEY (`pj`,`usr`,`gr`),
  KEY `FK_Reference_10` (`pj`,`gr`),
  KEY `FK_Reference_9` (`usr`),
  CONSTRAINT `FK_Reference_10` FOREIGN KEY (`pj`, `gr`) REFERENCES `pj_gr` (`pj`, `gr`),
  CONSTRAINT `FK_Reference_9` FOREIGN KEY (`usr`) REFERENCES `usr` (`usr`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pj_gr_usr`
--

LOCK TABLES `pj_gr_usr` WRITE;
/*!40000 ALTER TABLE `pj_gr_usr` DISABLE KEYS */;
/*!40000 ALTER TABLE `pj_gr_usr` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pj_usr`
--

DROP TABLE IF EXISTS `pj_usr`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pj_usr` (
  `pj` varchar(50) NOT NULL,
  `usr` varchar(50) NOT NULL,
  `psw` varchar(50) NOT NULL,
  PRIMARY KEY (`usr`,`pj`),
  KEY `FK_Reference_5` (`pj`),
  CONSTRAINT `FK_Reference_5` FOREIGN KEY (`pj`) REFERENCES `pj` (`pj`),
  CONSTRAINT `FK_Reference_7` FOREIGN KEY (`usr`) REFERENCES `usr` (`usr`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pj_usr`
--

LOCK TABLES `pj_usr` WRITE;
/*!40000 ALTER TABLE `pj_usr` DISABLE KEYS */;
/*!40000 ALTER TABLE `pj_usr` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pj_usr_auth`
--

DROP TABLE IF EXISTS `pj_usr_auth`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pj_usr_auth` (
  `pj` varchar(50) NOT NULL,
  `usr` varchar(50) NOT NULL,
  `res` varchar(200) NOT NULL,
  `rw` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`pj`,`res`,`usr`),
  KEY `FK_Reference_8` (`usr`),
  CONSTRAINT `FK_Reference_11` FOREIGN KEY (`pj`) REFERENCES `pj` (`pj`),
  CONSTRAINT `FK_Reference_8` FOREIGN KEY (`usr`) REFERENCES `usr` (`usr`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pj_usr_auth`
--

LOCK TABLES `pj_usr_auth` WRITE;
/*!40000 ALTER TABLE `pj_usr_auth` DISABLE KEYS */;
INSERT INTO `pj_usr_auth` VALUES ('app','admin','[app:/]','rw'),('dead','admin','[dead:/]','rw');
/*!40000 ALTER TABLE `pj_usr_auth` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usr`
--

DROP TABLE IF EXISTS `usr`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `usr` (
  `usr` varchar(50) NOT NULL,
  `psw` varchar(50) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `role` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`usr`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usr`
--

LOCK TABLES `usr` WRITE;
/*!40000 ALTER TABLE `usr` DISABLE KEYS */;
INSERT INTO `usr` VALUES ('*','*',NULL,NULL),('admin','am9obg**','admin','admin'),('koux','MTIz','kouxian','admin');
/*!40000 ALTER TABLE `usr` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-03-21 18:59:46
