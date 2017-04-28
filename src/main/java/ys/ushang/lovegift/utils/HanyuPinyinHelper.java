package ys.ushang.lovegift.utils;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * ����ת����ƴ��
 * ֧�ֶ�����
 */
public class HanyuPinyinHelper {
	
	private StringBuffer buffer = new StringBuffer();
	private List<String> list = new ArrayList<String>();
	private Properties p = new Properties();
	private boolean isSimple=false;

	public HanyuPinyinHelper() {
		init();
	}

	public void init() {
		try {
			ClassLoader cl=HanyuPinyinHelper.class.getClassLoader();
			if(cl!=null){
				p.load(cl.getResourceAsStream("/assets/hanyu_pinyin.txt"));
			}else {
				p.load(cl.getSystemResourceAsStream("/assets/hanyu_pinyin.txt"));
			}		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String[] getHanyuPinyins(char c) {
		int codePointOfChar = c;
		String codepointHexStr = Integer.toHexString(codePointOfChar)
				.toUpperCase();
		String str = (String) p.get(codepointHexStr);
		return str.split(",");
	}

	/**
	 * @param str ��Ҫת�����ַ���
	 * @param isSimple true��ƴ��falseȫƴ
	 * @return ���ַ���ת������������
	 */
	public List<String> hanyuPinYinConvert(String str,boolean isSimple) {
		if (str == null || "".equals(str))
			return null;
		this.isSimple=isSimple;
		list.clear();
		buffer.delete(0, buffer.length());
		convert(0, str);
		return list;
	}
	
	
	/**
	 * @param str ��Ҫת�����ַ���
	 * @return ���ַ���ת�����������ϣ�����ȫƴ�ͼ�ƴ
	 */
	public List<String> hanyuPinYinConvert(String str) {
		if (str == null || "".equals(str))
			return null;
		list.clear();
		buffer.delete(0, buffer.length());
		this.isSimple=true;
		convert(0, str);
		buffer.delete(0, buffer.length());
		this.isSimple=false;
		convert(0, str);
		return list;
	}

	private void convert(int n, String str) {
		if (n == str.length()) {// �ݹ����
			String temp=buffer.toString();
			if(!list.contains(temp)){
				list.add(buffer.toString());
			}
			return;
		} else {
			char c = str.charAt(n);
			if (0x3007 == c || (0x4E00 <= c && c <= 0x9FA5)) {// ������ַ�������UNICODE��Χ
				String[] arrayStrings = getHanyuPinyins(c);
				if (arrayStrings == null) {
					buffer.append(c);
					convert(n + 1, str);
				} else if (arrayStrings.length == 0) {
					buffer.append(c);
					convert(n + 1, str);
				} else if (arrayStrings.length == 1) {
					if(isSimple){
						if(!"".equals(arrayStrings[0])){
							buffer.append(arrayStrings[0].charAt(0));
						}
					}else{
						buffer.append(arrayStrings[0]);
					}
					convert(n + 1, str);
				} else {
					int len;
					for (int i = 0; i < arrayStrings.length; i++) {
						len = buffer.length();
						if(isSimple){
							if(!"".equals(arrayStrings[i])){
								buffer.append(arrayStrings[i].charAt(0));
							}
						}else{
							buffer.append(arrayStrings[i]);
						}
						convert(n + 1, str);
						buffer.delete(len, buffer.length());
					}
				}
			} else {// ������
				buffer.append(c);
				convert(n + 1, str);
			}
		}
	}
	
	public static void main(String[] args) {
		HanyuPinyinHelper helper=new HanyuPinyinHelper();
		List<String> list=helper.hanyuPinYinConvert("���ֵ�");
		System.out.println(list);
		list=helper.hanyuPinYinConvert("���ֵ�",true);
		System.out.println(list);
		list=helper.hanyuPinYinConvert("���ֵ�",false);
		System.out.println(list);
	}

}
