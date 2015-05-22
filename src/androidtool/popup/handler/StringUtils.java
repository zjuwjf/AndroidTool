package androidtool.popup.handler;

public class StringUtils {

	public static String toUpperCase(final String origin, final int... indexs) {
		if (origin != null && indexs != null) {
			final char[] chars = origin.toCharArray();
			
			for(int index : indexs) {
				if(index >= 0 && index <chars.length) {
					final char ch = chars[index];
					chars[index] = Character.toUpperCase(ch);
				}
			}
			
			return new StringBuilder().append(chars).toString();
		}
		return null;
	}
}
