package com.lepu.serial.uitl;


public class StringtoHexUitl {

	public static byte[] toByteArray(String s) {
		byte[] buf = new byte[s.length() / 2];
		int j = 0;
		for (int i = 0; i < buf.length; i++) {
			buf[i] = (byte) ((Character.digit(s.charAt(j++), 16) << 4) | Character
					.digit(s.charAt(j++), 16));
		}
		return buf;
	}

	/**
	 * byte[]转HexStr
	 *
	 * @param byteArray
	 * @return
	 */
	public static String byteArrayToHexStr(byte[] byteArray) {
		if (byteArray == null) {
			return null;
		}
		char[] hexArray = "0123456789ABCDEF".toCharArray();
		char[] hexChars = new char[byteArray.length * 2];
		for (int j = 0; j < byteArray.length; j++) {
			int v = byteArray[j] & 0xFF;
			hexChars[j * 2] = hexArray[v >>> 4];
			hexChars[j * 2 + 1] = hexArray[v & 0x0F];
		}
		return new String(hexChars);
	}


}
