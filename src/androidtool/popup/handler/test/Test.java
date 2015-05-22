package androidtool.popup.handler.test;

import androidtool.popup.handler.IActionHandler;
import androidtool.popup.handler.IPlugLogger;
import androidtool.popup.handler.XML2JFileHandler;

public class Test {
	
	public static void main(final String [] args) {
		final String dir = "D:\\j2ee\\mogutt\\TTAndroidClient\\mgandroid-teamtalk";
		final String file = "\\res\\layout\\tt_activity_message.xml";
		
		final IActionHandler handler = new XML2JFileHandler(new IPlugLogger() {
			public void log(String log) {
				System.err.println(log);
			}
		});
		
		handler.handle(dir, dir+file);
	}
	
}
