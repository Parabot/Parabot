package org.parabot.core.spoofer;

import java.io.IOException;

import org.parabot.core.ui.components.LogArea;

public class Runtime {
	
	private java.lang.Runtime rt;
	
	private static Runtime cached;
	
	private Runtime(java.lang.Runtime rt){
		this.rt = rt;
	}
	
	
	public void addShutdownHook(Thread t){
		rt.addShutdownHook(t);
	}
	
	public int availableProcessors(){
		return rt.availableProcessors();
	}
	
	public void exit(int i){
		rt.exit(i);
	}
	
	public Process exec(String str) throws IOException{
		LogArea.log("RT:" + str);
		System.out.println("RT:" + str);
		return rt.exec(str);
	}
	
	public Process exec(String[] cmdarray) throws IOException{
		StringBuffer sb = new StringBuffer();
		for(int i = 0; i < cmdarray.length;i++){
			sb.append(cmdarray[i] + (i < cmdarray.length - 1 ? "," : ""));
		}
		LogArea.log("RT: {" + sb + "}");
		System.out.println("RT: {" + sb + "}");
		return rt.exec(cmdarray);
	}
	
	public long freeMemory(){
		return rt.freeMemory();
	}
	
	public void gc(){
		rt.gc();
	}
	
	public long maxMemory(){
		return rt.maxMemory();
	}
	
	public static Runtime getRuntime(){
		if(cached == null)
			cached = new Runtime(java.lang.Runtime.getRuntime());
		return cached;
	}

}
