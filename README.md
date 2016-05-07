# 1、SVN统一管理平台
现状：                          
	主要用于统一管理SVN，以及SVN中的项目、用户、权限等。已经存在的类似工具，参考Svn Admin。
	目前已经实现用户权限配置、项目添加管理等功能。正在实现用户组管理、邮件提醒功能。	
	正在不断完善。。。                                                                   
目标：                                       
	实现在通过 web浏览器管理Svn的项目，管理项目的用户，管理项目的权限。使得管理配置Svn更简便，再也不需要每次都到服务器修改配置文件。
# 2、环境搭建
## 1、所需要的基础开发软件工具
* svn服务端[需要管理这个东西]
* Java[基于这个语言开发的]
* Tomcat[Web容器，你懂得]
* Eclipse[集成开发工具，这个可以是别的]
* maven[项目依赖管理工具]
* ehcache[缓存工具]
* freemarker[静态页生成工具]
* quartz Scheduler[定时任务工具]
* shiro[权限管理工具]
* nutz[mvc框架，主要就是用来展示这货的。hi 羊驼炒鸭梨 ，你要给力奥！]
* mysql[数据库，这个可以是其他的任何关系型数据库，只要JDBC可以连接基本没有压力]
* 恩，基本就是上面这些东西啦。

## 2、开始环境搭建         
   首先，要使用git将github上面的这个项目更新到本地。这个步骤，可以是使用git直接更新也可以使用eclipse中的egit直接在开发环境中更新到本地[mvn eclipse:eclipse -Dwtpversion=1.0]。                          
   编译代码，下载相关的依赖jar包。憋紧张，这个过程是集成开发工具配合maven完成的。你只需要静静的看着。        
   然后，创建数据库[create database rk_svnadmin]，然后，将config.properties文件中的数据库地址、用户名和密码修改完成。之后，启动项目。                    
   静静的等待吧，骚年！                                       
 Tomcat启动完成之后，在浏览器中输入http://localhost:8080/rk_svnadmin/user/login                    
 ok.看到登录页面了吧？接下来输入用户名：admin 密码：123 点击登录，被问我怎么知道的，在代码中有，不信自己去看。                                                          
   环境搭建完毕，接下来，骚年，尽情的释放你的洪荒之力吧！！                                                                      
   
##
登陆后台后 在 [项目管理]-[基本设置]

仓库路径 : /data/svn

访问url: http://192.168.x.x/svn/

说明: /data/svn 路径需要设置apache 用户权限 

chown -R apache:apache

chmod -R +w

apache 用户需要设置可执行shell权限

vim /etc/password

```
apache:x:48:48:Apache:/var/www:/bin/bash
```

第一次创建项目 会在/data/svn 目录下生成 authz httpd.conf passwd.http 三个文件 其中 需要把 

```
Include /data/svn/httpd.conf 
```

添加在 /etc/httpd/conf/httpd.conf 下 然后重启service httpd restart

## 3、附录
  Centos安装apache+svn结合的SVN服务器请参考[Centos安装apache+svn结合的SVN服务器](fl.md)                        
  管理系统配置请参考[管理系统配置请参考](https://github.com/yuexiaoyun/svnadmin/blob/master/doc/SvnAdmin_Manual_zh_CN.pdf)
