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
/*  .add_indexs  		*/
alter table pj_gr add constraint FK_Relationship_2 foreign key (pj) references pj (pj) on delete restrict on update restrict;
/*  .add_indexs_pj_gr_auth 		*/
alter table pj_gr_auth add constraint FK_Reference_6 foreign key (pj, gr) references pj_gr (pj, gr) on delete restrict on update restrict;
/*  .add_indexs_pj_gr_usr 		*/
alter table pj_gr_usr add constraint FK_Reference_10 foreign key (pj, gr) references pj_gr (pj, gr) on delete restrict on update restrict;
/*  .add_indexs_pj_gr_usr_1 		*/
alter table pj_gr_usr add constraint FK_Reference_9 foreign key (usr) references usr (usr) on delete restrict on update restrict;
/*  .add_indexs_pj_gr_usr_2 		*/
alter table pj_usr add constraint FK_Reference_5 foreign key (pj) references pj (pj) on delete restrict on update restrict;
/*  .add_indexs_pj_gr_usr_3 		*/
alter table pj_usr add constraint FK_Reference_7 foreign key (usr) references usr (usr) on delete restrict on update restrict;
/*  .add_indexs_pj_usr_auth_1 		*/
alter table pj_usr_auth add constraint FK_Reference_11 foreign key (pj) references pj (pj) on delete restrict on update restrict;
/*  .add_indexs_pj_usr_auth_2 		*/
alter table pj_usr_auth add constraint FK_Reference_8 foreign key (usr) references usr (usr) on delete restrict on update restrict;
