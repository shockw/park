package com.github.wxpay.sdk;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class MyConfig extends WXPayConfig {

	private byte[] certData;

	public MyConfig() throws Exception {
		String certPath = "/Users/shock/cert/1531695731_20190411_cert/apiclient_cert.p12";
		File file = new File(certPath);
		InputStream certStream = new FileInputStream(file);
		this.certData = new byte[(int) file.length()];
		certStream.read(this.certData);
		certStream.close();
	}

	@Override
	String getAppID() {
		return "wxa96b9ecd145f1b06";
	}

	@Override
	String getMchID() {
		return "1531695731";
	}

	@Override
	String getKey() {
		return "zwang2017zwang2017zwang2017zwang";
	}

	@Override
	InputStream getCertStream() {
		ByteArrayInputStream certBis = new ByteArrayInputStream(this.certData);
		return certBis;
	}

	public int getHttpConnectTimeoutMs() {
		return 8000;
	}

	public int getHttpReadTimeoutMs() {
		return 10000;
	}

	@Override
	IWXPayDomain getWXPayDomain() {
		IWXPayDomain iwxPayDomain = new IWXPayDomain() {
			@Override
			public void report(String domain, long elapsedTimeMillis, Exception ex) {

			}

			@Override
			public DomainInfo getDomain(WXPayConfig config) {
				return new IWXPayDomain.DomainInfo(WXPayConstants.DOMAIN_API, true);
			}
		};
		return iwxPayDomain;
	}

}
