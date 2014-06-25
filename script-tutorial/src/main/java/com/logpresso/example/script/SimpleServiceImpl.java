/*
 * Copyright 2014 Eediom Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.logpresso.example.script;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Invalidate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(name = "simplescript-service-impl")
@Provides
public class SimpleServiceImpl implements SimpleService {
	private int writeInterval = 20000;
	private String message = "dummy";
	private MessageWriter writer;
	private Thread writeMessageThread;

	@Validate
	@Override
	public void start() {
		writer = new MessageWriter(writeInterval, message);
		writeMessageThread = new Thread(writer, "Simple Message Writer");
		writeMessageThread.start();
	}

	@Invalidate
	@Override
	public void stop() {
		writer.doStop = true;
		synchronized (writer) {
			writer.notifyAll();
		}
	}

	@Override
	public String getMessage() {
		return message;
	}

	@Override
	public void setMessage(String message) {
		this.message = message;
		stop();
		start();
	}
	
	@Override
	public int getInterval() {
		return writeInterval;
	}
	
	@Override
	public void setInterval(int interval) {
		this.writeInterval = interval;
		stop();
		start();
	}
	
	private class MessageWriter implements Runnable {
		private final Logger logger = LoggerFactory.getLogger(MessageWriter.class.getName());
		private final int writeInterval;
		private final String message;
		
		private volatile boolean doStop = false;
		
		public MessageWriter(int writeInterval, String message) {
			this.writeInterval = writeInterval;
			this.message = message;
		}

		@Override
		public void run() {
			while (true) {
				if (doStop)
					break;
				
				logger.info(message);
				try {
					Thread.sleep(writeInterval);
				} catch (InterruptedException e) {
					break;
				}
			}
		}
		
	}

}
