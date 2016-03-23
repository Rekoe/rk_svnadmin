1. create database platform
2. mvn clean
3. mvn eclipse:eclipse -Dwtpversion=1.0
4. mvn package -Dmaven.test.skip=true
5. mvn dependency:tree 可以查看依赖树

Centos安装apache+svn结合的SVN服务器

1、yum安装下列的一些包：

yum install apr apr-util httpd httpd-devel subversion mod_dav_svn mod_auth_mysql  

2、建立svn的目录

mkdir -p /code/svndata

3、创建一个库就叫SVN：

svnadmin create /code/svndata/svn


正常安全以上软件后，会在/etc/httpd/modules目录下生成mod_dav_svn.so、mod_authz_svn.so两个模块
cp /etc/httpd/conf/httpd.conf /etc/httpd/conf/httpd.conf.bak

6、配置apache的httpd.conf
vim /etc/httpd/conf/httpd.conf

添加

LoadModule dav_svn_module     modules/mod_dav_svn.so  

LoadModule authz_svn_module   modules/mod_authz_svn.so 


chown -R apache:apache /code/svndata/svn/

chmod -R 755 /code/svndata/svn/


7、要关闭selinux的保护设置为禁用：

vi /etc/selinux/config

修改SELINUX=disabled

保存

不重启Linux服务器关闭SeLinux的方法

# setenforce 0

8、关闭防火墙：

/etc/init.d/iptables stop


9、测试：

http://服务器IP/svn

输入用户名和密码可以登录表示成功！

启动httpd

service httpd start