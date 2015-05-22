package androidtool.popup.handler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import freemarker.template.TemplateException;

public class XML2JFileHandler implements IActionHandler {

	private final static String AndroidManifest_Tag = "AndroidManifest.xml";
	private final static String AndroidGen_Tag = "gen";
	private final static String XML_Subfix_Tag = ".xml";
	private final static String Java_Subfix_Tag = ".java";

	private final IPlugLogger plugLogger;

	public XML2JFileHandler(IPlugLogger logger) {
		this.plugLogger = logger;
	}

	@Override
	public void handle(String projectDir, String filePath) {
		if (checkValid(projectDir, filePath)) {
			final String genPath = getGenPath(projectDir);
			final String mainPackage = getMainPackage(projectDir);

			final String layoutName = getLayoutName(filePath);
			final String className = getClassName(filePath);

			final File outputDir = new File(new File(genPath), mainPackage.replace(".", "//"));
			outputDir.mkdirs();
			
			final File outFile = new File(outputDir, className + Java_Subfix_Tag);

			try {
				LayoutJFileWriter.writeJFile(new FileInputStream(filePath), new FileOutputStream(outFile), className, layoutName, mainPackage);

				plugLogger.log("Convert To ViewHolder Successfully!");

			} catch (FileNotFoundException e) {
				e.printStackTrace();
				plugLogger.log(e.getMessage());
			} catch (IOException e) {
				e.printStackTrace();
				plugLogger.log(e.getMessage());
			} catch (TemplateException e) {
				e.printStackTrace();
				plugLogger.log(e.getMessage());
			}

		} else {
			plugLogger.log("Convert To ViewHolder Failed!");
		}
	}

	private String getGenPath(String projectDir) {
		if (projectDir != null) {
			final File file = new File(new File(projectDir), AndroidGen_Tag);
			if (file.exists()) {
				return file.getAbsolutePath();
			}
		}
		return null;
	}

	private String getLayoutName(final String filePath) {
		if (filePath != null) {
			final File file = new File(filePath);
			if (file.exists()) {
				final String name = file.getName();
				return name.substring(0, name.length() - XML_Subfix_Tag.length());
			}
		}
		return null;
	}

	private String getClassName(final String filePath) {
		final String layoutName = getLayoutName(filePath);
		return StringUtils.toUpperCase(layoutName, 0);
	}

	private String getMainPackage(String projectDir) {
		final String androidManifestPath = getAndroidManifest(projectDir);
		if (androidManifestPath != null) {
			/***
			 * <manifest
			 * xmlns:android="http://schemas.android.com/apk/res/android"
			 * package="com.yangfuhai.asimplecachedemo" android:versionCode="1"
			 * android:versionName="1.0" >
			 */
			try {
				return LayoutXMLReader.readMainPackage(new FileInputStream(androidManifestPath));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	private boolean checkValid(final String projectDir, String filePath) {
		if (projectDir != null && filePath != null) {
			final File dir = new File(projectDir);
			final File file = new File(filePath);
			if (dir.isDirectory() && file.isFile()) {
				if (isAndroidProject(projectDir)) {
					if (filePath.endsWith(XML_Subfix_Tag) && filePath.startsWith(projectDir)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	private String getAndroidManifest(final String projectDir) {
		if (projectDir != null) {
			final File file = new File(new File(projectDir), AndroidManifest_Tag);
			if (file.exists()) {
				return file.getAbsolutePath();
			}
		}
		return null;
	}

	private boolean isAndroidProject(final String projectDir) {
		if (getAndroidManifest(projectDir) == null) {
			plugLogger.log("Not Found AndroidManifest!");
			return false;
		}
		if (getMainPackage(projectDir) == null) {
			plugLogger.log("Not Found Main Package!");
			return false;
		}
		if (getLayoutName(projectDir) == null) {
			plugLogger.log("Not A Vaild Layout XML!");
			return false;
		}
		if (getClassName(projectDir) == null) {
			plugLogger.log("Not A Valid Class Name!");
			return false;
		}
		if (getGenPath(projectDir) == null) {
			plugLogger.log("Not A Valid gen Directory!");
			return false;
		}
		return true;
	}

}
