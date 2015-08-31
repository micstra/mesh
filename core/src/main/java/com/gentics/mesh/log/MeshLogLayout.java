package com.gentics.mesh.log;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.gentics.mesh.cli.MeshNameProvider;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.LayoutBase;

public class MeshLogLayout extends LayoutBase<ILoggingEvent> {

	private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

	public String doLayout(ILoggingEvent event) {
		StringBuffer sbuf = new StringBuffer(128);
		String dateString = format.format(new Date(event.getTimeStamp()));
		sbuf.append(dateString);
		sbuf.append(" ");

		sbuf.append("[");
		sbuf.append(MeshNameProvider.getInstance().getName());
		sbuf.append("] ");
		sbuf.append(StringUtils.rightPad(event.getLevel().toString(), 5));
		sbuf.append(" [");
		sbuf.append(event.getThreadName());
		sbuf.append("] ");
		sbuf.append(event.getLoggerName());
		sbuf.append(" - ");
		sbuf.append(event.getFormattedMessage());
		sbuf.append("\n");
		return sbuf.toString();
	}
}