1. create database rk_svnadmin
2. mvn eclipse:eclipse -Dwtpversion=1.0

服务器启动完手动执行alter

```
alter table pj_gr add constraint FK_Relationship_2 foreign key (pj) references pj (pj) on delete restrict on update restrict;
alter table pj_gr_auth add constraint FK_Reference_6 foreign key (pj, gr) references pj_gr (pj, gr) on delete restrict on update restrict;
alter table pj_gr_usr add constraint FK_Reference_10 foreign key (pj, gr) references pj_gr (pj, gr) on delete restrict on update restrict;
alter table pj_gr_usr add constraint FK_Reference_9 foreign key (usr) references usr (usr) on delete restrict on update restrict;
alter table pj_usr add constraint FK_Reference_5 foreign key (pj) references pj (pj) on delete restrict on update restrict;
alter table pj_usr add constraint FK_Reference_7 foreign key (usr) references usr (usr) on delete restrict on update restrict;
alter table pj_usr_auth add constraint FK_Reference_11 foreign key (pj) references pj (pj) on delete restrict on update restrict;
alter table pj_usr_auth add constraint FK_Reference_8 foreign key (usr) references usr (usr) on delete restrict on update restrict;
```

Centos安装apache+svn结合的SVN服务器

#1、yum安装下列的一些包：

yum install apr apr-util httpd httpd-devel subversion mod_dav_svn mod_auth_mysql  

#2、建立svn的目录

mkdir -p /code/svndata

#3、创建一个库就叫SVN：

svnadmin create /code/svndata/svn


正常安全以上软件后，会在/etc/httpd/modules目录下生成mod_dav_svn.so、mod_authz_svn.so两个模块
cp /etc/httpd/conf/httpd.conf /etc/httpd/conf/httpd.conf.bak

#4、配置apache的httpd.conf
vim /etc/httpd/conf/httpd.conf

添加

LoadModule dav_svn_module     modules/mod_dav_svn.so  

LoadModule authz_svn_module   modules/mod_authz_svn.so 


chown -R apache:apache /code/svndata/svn/

chmod -R 755 /code/svndata/svn/


#5、要关闭selinux的保护设置为禁用：

vi /etc/selinux/config

修改SELINUX=disabled

保存

不重启Linux服务器关闭SeLinux的方法

setenforce 0

#6、关闭防火墙：

/etc/init.d/iptables stop


#7、测试：

http://服务器IP/svn

输入用户名和密码可以登录表示成功！

#启动httpd

service httpd start

管理系统配置请参考[管理系统配置请参考](https://github.com/yuexiaoyun/svnadmin/blob/master/doc/SvnAdmin_Manual_zh_CN.pdf)