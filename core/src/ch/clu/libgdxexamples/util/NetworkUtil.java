package ch.clu.libgdxexamples.util;

public class NetworkUtil {
	public static void main(String[] args) {
		int a = -1333803349;
		String ipString = hostOrderToString(a);
		System.out.println(ipString);
	}

	public static String hostOrderToString(int ip) {
		String hexString = Integer.toHexString(ip);
		String[] split = hexString.split("(?<=\\G..)");
		String result = "";
		for (int i = 0; i < split.length; i++) {
			result += Integer.decode("0x" + split[i]);
			if (i < split.length - 1) {
				result += ".";
			}
		}
		return result;
	}
}
