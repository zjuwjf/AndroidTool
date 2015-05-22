package androidtool.popup.handler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class LayoutJFileWriter {
	
	private final static String Package_Tag = "package";
	private final static String Class_Tag = "class";
	private final static String Layout_Tag = "layout";
	private final static String ImportList_Tag = "importList";
	private final static String PairList_Tag = "pairList";
	
	private final static String TemplateDir = "template/";
	private final static String TemplateFile = "ViewSetTemplate.tl";

	public static void writeJFile(final InputStream layoutStream, final OutputStream jFielStream, final String className, final String layoutName, final String mainPackage) throws IOException, TemplateException {

		Configuration cfg = new Configuration();
		cfg.setClassForTemplateLoading(LayoutJFileWriter.class, TemplateDir);
		cfg.setObjectWrapper(new DefaultObjectWrapper());

		Template temp = cfg.getTemplate(TemplateFile);

		final String packageValue = mainPackage;

		final List<String> importList = new LinkedList<String>();

		final String classValue = className;

		final List<ViewPairBean> pairList = LayoutXMLReader.readLayoutXML(layoutStream);
		
		final String layoutValue = layoutName;

		Map<String, Object> root = new HashMap<String, Object>();
		root.put(Package_Tag, packageValue);
		root.put(Class_Tag, classValue);
		root.put(Layout_Tag, layoutValue);

		root.put(ImportList_Tag, importList);
		root.put(PairList_Tag, pairList);

		Writer out = new OutputStreamWriter(jFielStream);
		temp.process(root, out);
		out.flush();
		out.close();
	}
}
