package com.reache.jeemanage.modules.park;

import com.reache.jeemanage.common.config.Global;

public class Constant {
	public static final String SUCCESS_RESULT = "\"{\\\"result\\\":1,\\\"success\\\":true}\"";
	public static final String ERROR_RESULT = "\"{\\\"result\\\":0,\\\"success\\\":false}\"";
	public static final String LOCAL_DIR = Global.getConfig("control.dir");
	public static final String IN_URL = Global.getConfig("control.inUrl");
	public static final String OUT_URL = Global.getConfig("control.outUrl");
	public static final String FTP_IN = Global.getConfig("control.ftpIn");
	public static final String FTP_OUT = Global.getConfig("control.ftpOut");
	public static final int FTP_PORT = Integer.valueOf(Global.getConfig("control.ftpPort"));
}
