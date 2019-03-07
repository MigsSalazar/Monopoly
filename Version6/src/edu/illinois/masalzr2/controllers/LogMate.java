package edu.illinois.masalzr2.controllers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Deprecated
public class LogMate {
	
	public static final Logger LOG = new Logger();
	
	public static class Logger{
		
		private File output;
		private String s = File.separator;
		private BufferedWriter writeout;
		private boolean active = true;
		private int counter = 0;
		
		public Logger() {
			newLog();
		}
		
		public void newLog() {
			
			if( writeout != null ) {
				try {
					writeout.close();
				} catch (IOException e) {
					MonopolyExceptionHandler.uncaughtException(e, Thread.currentThread());
				}
			}
			
			File temp = new File(System.getProperty("user.dir") + s + "logs" );
			
			if(!temp.exists()) {
				temp.mkdirs();
			}
			
			SimpleDateFormat today = new SimpleDateFormat("MM-dd-yyyy_hh.mm.ss");
			
			output = new File(temp + s + "log " + today.format(new Date()) + ".log" );
			
			try{
				writeout = new BufferedWriter( new FileWriter( output ) );
				active = true;
			}catch(IOException e) {
				MonopolyExceptionHandler.uncaughtException(e, Thread.currentThread());
			}
		}
		
		public void newEntry(String s) throws Exception{
			try {
				counter++;
				if( !active ) {
					newLog();
				}
				writeout.append(s);
				writeout.newLine();
				if(counter > 20) {
					flush();
					counter = 0;
				}
			}catch(IOException e) {
				MonopolyExceptionHandler.uncaughtException(e, Thread.currentThread());
			}
		}
		
		public void flush(){
			try {
				if( !active ) {
					newLog();
				}
				writeout.flush();
			}catch(IOException e) {
				MonopolyExceptionHandler.uncaughtException(e, Thread.currentThread());
			}
		}
		
		public void finish(){
			try {
				if( !active ) {
					return;
				}
				writeout.close();
				active = false;
			}catch(IOException e) {
				MonopolyExceptionHandler.uncaughtException(e, Thread.currentThread());
			}
		}
	}
	
	
	
}
