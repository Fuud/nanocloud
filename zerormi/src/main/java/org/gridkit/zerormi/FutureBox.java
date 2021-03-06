/**
 * Copyright 2011-2012 Alexey Ragozin
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
package org.gridkit.zerormi;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author Alexey Ragozin (alexey.ragozin@gmail.com)
 */
class FutureBox<V> implements Future<V>, Box<V> {

	private final FutureTask<V> ft = new FutureTask<V>(new Callable<V>() {
		@Override
		public V call() throws Exception {
			synchronized(FutureBox.this) {
				if (!finalized) {
					throw new Error("Unexpected call time");
				}
				else {
					if (error != null) {
						throw error;
					}
					else {
						return value;
					}
				}
			}
		}
	});
	
	private boolean finalized;
	private V value;
	private Exception error;

	@Override
	public synchronized void setData(V data) {
		if (finalized) {
			throw new IllegalStateException("Box is closed");
		}
		else {
			finalized = true;
			value = data;
			ft.run();
		}		
	}

	@Override
	public synchronized void setError(Exception e) {
		if (finalized) {
			throw new IllegalStateException("Box is closed");
		}
		else {
			finalized = true;
			error = e;
			ft.run();
		}		
	}

	@Override
	public boolean cancel(boolean mayInterruptIfRunning) {
		return ft.cancel(mayInterruptIfRunning);
	}
	
	@Override
	public boolean isCancelled() {
		return ft.isCancelled();
	}
	
	@Override
	public boolean isDone() {
		return ft.isDone();
	}
	
	@Override
	public V get() throws InterruptedException, ExecutionException {
		return ft.get();
	}

	@Override
	public V get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
		return ft.get(timeout, unit);
	}
}
