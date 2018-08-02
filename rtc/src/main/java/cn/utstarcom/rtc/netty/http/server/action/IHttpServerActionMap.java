package cn.utstarcom.rtc.netty.http.server.action;

import java.util.NoSuchElementException;

public interface IHttpServerActionMap {

	/**
	 * According to the action name get IHttpServerAction instance
	 * 
	 * @param IHttpServerAction
	 *            httpServerAction name
	 * 
	 * @return the IHttpServerAction to which the specified key is mapped
	 * 
	 * @exception NoSuchElementException
	 *                if there is no value present
	 */
	public IHttpServerAction get(String actionName);
}
