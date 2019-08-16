package com.reache.jeemanage.modules.park;

/**
 * 存取车令牌管理 主要为分令牌占用，令牌释放及令牌状态查询
 * 
 * @author shock
 *
 */
public class TokenManager {
	private static volatile Boolean idle = true;
	private static final Object lock = new Object();

	/**
	 * 令牌占用，如果idle为true，则占用成功，否则占用失败
	 * 占用成功后，idle状态修改为false。
	 * 
	 * @return
	 */
	public static Boolean occupy() {
		synchronized (lock) {
			if (idle == true) {
				idle = false;
				return true;
			} else {
				return false;
			}

		}
	}
	/**
	 * 令牌释放，如果idel为false，则释放成功，释放成功后状态修改为true
	 * @return
	 */
	public static Boolean release() {
		synchronized (lock) {
			if (idle == false) {
				idle = true;
				return true;
			} else {
				return false;
			}

		}
	}
	/**
	 * 获取令牌是否可用的状态
	 * @return
	 */
	public static Boolean getStatus() {
		return idle;
	}

}
