/**
 * Copyright 2012 Alexey Ragozin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.gridkit.vicluster;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;


/**
 * 
 * @author Alexey Ragozin (alexey.ragozin@gmail.com)
 *
 */
public interface ViNode extends ViExecutor, ViConfigurable {

	public String getProp(String propName);
	
	/** 
	 * Freezes all thread associated with node. Supported only for embeded nodes.
	 * @deprecated Use {@link ViHelper#suspend(ViNode)}
	 */
	public void suspend();
	
	/** 
	 * Resumes node freezes with {@link #suspend()}.
	 * @deprecated Use {@link ViHelper#resume(ViNode)}
	 */
	public void resume();
	
	/** 
	 * Ungracefully terminates remote process (or thread group in embeded node). Unlike {@link #shutdown()} no shutdown hooks will be executed in remote VM.
	 * This method may be useful for fault tolerance testing.
	 */
	public void kill();
	
	/** 
	 * Gracefully terminates remote process (or thread group in embeded node).
	 */
	public void shutdown();
	
	public static class Delegate implements ViNode {
		
		private final ViNode delegate;

		public Delegate(ViNode delegate) {
			this.delegate = delegate;
		}

		protected ViNode getDelegate() {
			return delegate;
		}
		
		@Override
		public void setProp(String propName, String value) {
			delegate.setProp(propName, value);
		}

		@Override
		public String getProp(String propName) {
			return delegate.getProp(propName);
		}

		public void setConfigElement(String key, Object value) {
			delegate.setConfigElement(key, value);
		}

		public void setConfigElements(Map<String, Object> config) {
			delegate.setConfigElements(config);
		}

		@Override
		public void suspend() {
			delegate.suspend();
		}

		@Override
		public void setProps(Map<String, String> props) {
			delegate.setProps(props);
		}

		@Override
		public void touch() {
			delegate.touch();
		}

		@Override
		public void resume() {
			delegate.resume();
		}

		@Override
		public void kill() {
			delegate.kill();			
		}

		@Override
		public void shutdown() {
			delegate.shutdown();
		}

		public void addStartupHook(String id, Runnable hook) {
			delegate.addStartupHook(id, hook);
		}

		public void addShutdownHook(String id, Runnable hook) {
			delegate.addShutdownHook(id, hook);
		}

		@Override
		@SuppressWarnings("deprecation")
		public void addStartupHook(String name, Runnable hook, boolean override) {
			delegate.addStartupHook(name, hook, override);
		}

		@Override
		@SuppressWarnings("deprecation")
		public void addShutdownHook(String name, Runnable hook, boolean override) {
			delegate.addShutdownHook(name, hook, override);
		}

		@Override
		public void exec(Runnable task) {
			delegate.exec(task);
		}

		@Override
		public void exec(VoidCallable task) {
			delegate.exec(task);
		}

		@Override
		public <T> T exec(Callable<T> task) {
			return delegate.exec(task);
		}

		@Override
		public Future<Void> submit(Runnable task) {
			return delegate.submit(task);
		}

		@Override
		public Future<Void> submit(VoidCallable task) {
			return delegate.submit(task);
		}

		@Override
		public <T> Future<T> submit(Callable<T> task) {
			return delegate.submit(task);
		}

		@Override
		public <T> List<T> massExec(Callable<? extends T> task) {
			return delegate.massExec(task);
		}

		@Override
		public List<Future<Void>> massSubmit(Runnable task) {
			return delegate.massSubmit(task);
		}

		@Override
		public List<Future<Void>> massSubmit(VoidCallable task) {
			return delegate.massSubmit(task);
		}

		@Override
		public <T> List<Future<T>> massSubmit(Callable<? extends T> task) {
			return delegate.massSubmit(task);
		}
		
		@Override
		public String toString() {
			return delegate.toString();
		}
	}
}
