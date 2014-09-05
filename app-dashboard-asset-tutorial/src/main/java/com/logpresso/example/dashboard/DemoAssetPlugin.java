package com.logpresso.example.dashboard;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Requires;
import org.araqne.msgbus.Request;
import org.araqne.msgbus.Response;
import org.araqne.msgbus.handler.MsgbusMethod;
import org.araqne.msgbus.handler.MsgbusPlugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Manage MariaDB service code table.
 * 
 * @author xeraph
 * 
 */
@MsgbusPlugin
@Component(name = "demo-asset-plugin")
public class DemoAssetPlugin {
	private final Logger slog = LoggerFactory.getLogger(DemoAssetPlugin.class);

}
