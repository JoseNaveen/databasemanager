package com.joey.databasemanager.implalpha;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SimpleCommandLineProcessController extends Controller{

	
	private StateMachine machine;
	private Process process;
	private Executor executor;
	
	public SimpleCommandLineProcessController(StateMachine machine) {
		this.machine = machine;
		executor = Executors.newFixedThreadPool(5);
	}
	
	@Override
	public CompletableFuture<Boolean> startcommand(String[] command) {
		ProcessBuilder builder = new ProcessBuilder(command);
		var ret = new CompletableFuture<Boolean>();
		try {
			process = builder.start();
			Runnable runnable = createProcessOutputReader(process);
			this.executor.execute(createProcessErrOutputReader(process));
			this.executor.execute(runnable);
			process.onExit().thenAccept(proc-> {
				System.out.println("process has exited executing on exit callback");
				int exitvalue = proc.exitValue();
				System.out.println("exit value: " + exitvalue);
				if (exitvalue == 0) {
					//normal termination action
					System.out.println("executing normal termination action");
					this.machine.process(new MessageImpl("onexitoff"), this);
				}
				else {
					System.out.println("executing error termination action");
					this.machine.process(new MessageImpl("onexitoff"), this);
				}
			});
			ret.complete(true);
		}
		catch (IOException e) {	
			ret.completeExceptionally(e);
		}
		return ret;
	}
	
	private Runnable createProcessErrOutputReader(Process process) {
		Runnable runnable = () -> {
			String result = null;
			try {
			BufferedReader reader = 
		            new BufferedReader(new InputStreamReader(process.getErrorStream()));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
				sb.append(System.getProperty("line.separator"));
			}
			result = sb.toString();
			}
			catch (IOException e) {
				e.printStackTrace();
				System.out.println(result);
				return;
			}
		};
		return runnable;
	}
	
	
	

	private Runnable createProcessOutputReader(Process process) {
		Runnable runnable = () -> {
			String result = null;
			try {
			BufferedReader reader = 
		            new BufferedReader(new InputStreamReader(process.getInputStream()));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
				sb.append(System.getProperty("line.separator"));
			}
			result = sb.toString();
			}
			catch (IOException e) {
				e.printStackTrace();
				System.out.println(result);
				return;
			}
		};
		return runnable;
	}

	@Override
	public CompletableFuture<Boolean> stopcommand() {
		var ret = new CompletableFuture<Boolean>();
		if (process.isAlive()) {
			if (process.supportsNormalTermination()) {
				process.destroy();
				try {
				process.waitFor(5000, TimeUnit.MILLISECONDS);
				ret.complete(true);
				return ret;
				}
				catch (InterruptedException e) {
					ret.completeExceptionally(e);
					return ret;
				}
			}
			else {
				process.destroyForcibly();
				try {
					process.waitFor(5000, TimeUnit.MILLISECONDS);
					ret.complete(true);
					return ret;
					}
					catch (InterruptedException e) {
						ret.completeExceptionally(e);
						return ret;
					}
			}
		}
		ret.complete(true);
		return ret;
		
	}

	@Override
	public synchronized void enqueue(Message message) {
		System.out.println("enqueuing event: " + message.getMessage());
		this.machine.process(message, this);
	}

	@Override
	public StateMachine getStateMachine() {
		
		return this.machine;
	}

	
}
