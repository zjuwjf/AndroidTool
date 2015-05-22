package androidtool.popup.handler;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

/**
 * @author zju_wjf
 * 
 */
public class LayoutXMLReader {
	private static final String Android_Id_Tag = "id";
	private static final String Id_Start_Tag = "@+id/";
	private static final String Package_Tag = "package";

	/**
	 * @param inputStream
	 * @return
	 */
	public static String readMainPackage(final InputStream inputStream) {

		SAXReader saxReader = new SAXReader();
		Document document = null;
		try {
			document = saxReader.read(inputStream);
			final Element element = document.getRootElement();
			final String mainPackage = element.attributeValue(Package_Tag);

			return mainPackage;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * @param inputStream
	 * @return
	 */
	public static List<ViewPairBean> readLayoutXML(final InputStream inputStream) {
		final LinkedList<ViewPairBean> pairs = new LinkedList<ViewPairBean>();

		SAXReader saxReader = new SAXReader();
		Document document = null;
		try {
			document = saxReader.read(inputStream);
			final Element element = document.getRootElement();

			readElement(element, pairs);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return pairs;
	}

	/**
	 * 读取当前Element及其所有只节点的信息
	 * 
	 * @param element
	 * @param pairs
	 */
	private final static void readElement(final Element element, final List<ViewPairBean> pairs) {
		if (element != null) {
			readElementOne(element, pairs);

			@SuppressWarnings("unchecked")
			List<Element> elements = element.elements();
			for (Element childElement : elements) {
				readElement(childElement, pairs);
			}
		}
	}

	/**
	 * 只读取当前Element信息
	 * 
	 * @param element
	 * @param pairs
	 */
	private final static void readElementOne(final Element element, final List<ViewPairBean> pairs) {
		if (element != null) {

			final String id = element.attributeValue(Android_Id_Tag);
			if (id != null && id.startsWith(Id_Start_Tag)) {
				final ViewPairBean newPair = new ViewPairBean();

				final String eleName = element.getName();

				// 首字母大写
				newPair.setViewType(StringUtils.toUpperCase(eleName, 0));
				newPair.setViewId(id.substring(Id_Start_Tag.length()));

				if (newPair.getViewType() != null && newPair.getViewId() != null) {
					pairs.add(newPair);
				}
			}
		}
	}
}
