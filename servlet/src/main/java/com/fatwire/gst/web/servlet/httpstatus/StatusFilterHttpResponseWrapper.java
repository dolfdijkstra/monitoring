package com.fatwire.gst.web.servlet.httpstatus;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p>
 * A filter that transforms 2 special headers into a response status or response header. In a ContentServer XML element you can call ics.StreamHeader("X-Fatwire-Status","404") to set the response status for Satellite Server to 404. 
 * </p>
 *
 *
 * @version 15 June 2009
 * @author Dolf Dijkstra
 *
 */
public class StatusFilterHttpResponseWrapper extends HttpServletResponseWrapper {

	private static final String X_FATWIRE_STATUS = "X-Fatwire-Status";
	private static final String X_FATWIRE_HEADER = "X-Fatwire-Header";

	private static Log log = LogFactory.getLog(CustomHeaderFilter.class);

	private int status = -1;

	/**
	 * Class constructor instatiating the response object
	 */
	public StatusFilterHttpResponseWrapper(HttpServletResponse origResponse) {
		super(origResponse);

	}

	/**
	 * This method sets the response header value and names. It just proxies the
	 * custom response header information if the environment is CS
	 * (ContentServer). If the environment is SS (Satellite Server) then the
	 * custom header information supplied as "X-FatWire-Header" and
	 * "X-FatWire-Status" is parsed and set in the response header accordingly
	 *
	 * @param hdrName
	 *            Response header name
	 * @param hdrValue
	 *            Response header value
	 */
	public void setHeader(String hdrName, String hdrValue) {
		if (log.isDebugEnabled()) {
			log.debug("original setHeader " + hdrName + ": " + hdrValue);
		}

		if (X_FATWIRE_STATUS.equalsIgnoreCase(hdrName)) {
			try {
				status = Integer.parseInt(hdrValue);
			} catch (Throwable t) {
				log.warn("Exception parsing  the " + hdrName + " header. "
						+ t.getMessage());
			}
			if (status > 300) {
				// TODO is sendError needed for codes > 400?
				// TODO is sendRedirectNeeded for 302 or 301?
				if (log.isDebugEnabled()) {
					log.debug("setStatus to  " + status + " from " + hdrName);
				}
				if (this.isCommitted()) {
					log.debug("wanted to setStatus to  " + status + " from "
							+ hdrName
							+ " but the response is already committed");
				}

				super.setStatus(status);
				// ignore the header all together after the setStatus, so
				// we are not
				// leaking to the public

			} else if (status != -1) {
				log.debug("ignoring status header with value " + status
						+ " from " + hdrName);
			}
		} else if (X_FATWIRE_HEADER.equalsIgnoreCase(hdrName)) {
			// splitting header name/value based on passed in header value,
			// pipe seperated;
			String[] headers = hdrValue.split("\\|");
			if (headers.length == 2 && headers[0] != null && headers[1] != null) {
				super.setHeader(headers[0], headers[1]);
			} else {
				log.debug(hdrName
						+ " could not be split into something useful. "
						+ hdrValue);
			}

		} else {
			super.setHeader(hdrName, hdrValue);
		}

	}

	@Override
	public void setStatus(int sc) {
		if (status == -1) {
			// only set it if we have not overridden it
			super.setStatus(sc);
		} else {
			if (log.isTraceEnabled()) {
				log.trace("setStatus " + sc + " is being ignored because "
						+ X_FATWIRE_STATUS + " header set it to " + status);
			}

		}
	}

}// end of BufferedHttpResponseWrapper

