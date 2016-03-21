/* system_user */
INSERT INTO system_user(ID,NAME,PASSWORD,SALT,DESCRIPTION,IS_LOCKED,CREATE_DATE,REGISTER_IP,OPENID,PROVIDERID,is_system) VALUES 
(1,'admin',NULL,NULL,"超级管理员",0,'2014-02-01 00:12:40','127.0.0.1','admin','local',1);
/*  .system_role   		*/
INSERT INTO system_role(ID,NAME,DESCRIPTION) VALUES
(1,'admin','超级管理员：拥有全部权限的角色');
/*  .system_permission   		*/
INSERT INTO `system_permission` VALUES (1,'*:*:*','全部权限','1',1);
/*  .permission_category   		*/
INSERT INTO `permission_category` VALUES ('1','超级权限',1);
/*  .system_user_role   		*/
INSERT INTO system_user_role(USERID,ROLEID) VALUES 
(1,1);
/*  .system_role_permission  		*/
INSERT INTO system_role_permission(ROLEID,PERMISSIONID) VALUES 
(1,1)