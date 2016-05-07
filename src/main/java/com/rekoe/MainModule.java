package com.rekoe;

import org.nutz.mvc.annotation.ChainBy;
import org.nutz.mvc.annotation.Encoding;
import org.nutz.mvc.annotation.Fail;
import org.nutz.mvc.annotation.IocBy;
import org.nutz.mvc.annotation.Localization;
import org.nutz.mvc.annotation.Modules;
import org.nutz.mvc.annotation.SetupBy;
import org.nutz.mvc.annotation.Views;
import org.nutz.mvc.impl.NutActionChainMaker;
import org.nutz.mvc.ioc.provider.ComboIocProvider;
import org.nutz.plugins.view.freemarker.FreemarkerViewMaker;

@Modules(scanPackage = true, packages = { "com.rekoe.module" })
@IocBy(type = ComboIocProvider.class, args = { "*json", "/ioc", "*anno", "com.rekoe", "*tx", "*org.nutz.integration.quartz.QuartzIocLoader", "*org.nutz.plugins.view.freemarker.FreemarkerIocLoader", "*async" })
@SetupBy(MvcSetup.class)
@Fail(">>:/admin/common/unauthorized.rk")
@Encoding(input = "UTF-8", output = "UTF-8")
@ChainBy(type = NutActionChainMaker.class, args = { "com/rekoe/mvc/mvc-chains.js" })
@Views({ FreemarkerViewMaker.class })
@Localization(value = "msg/", defaultLocalizationKey = "zh-CN")
public class MainModule {

}
