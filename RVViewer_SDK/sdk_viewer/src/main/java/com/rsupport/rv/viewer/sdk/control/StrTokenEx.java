package com.rsupport.rv.viewer.sdk.control;

import java.util.Vector;

public class StrTokenEx {
	private Vector m_vecElem;
	private String m_strEmpty; // 내용이 없을때 리턴되는 값

	private String m_strDelimiter; // 구분자

	private int m_curPos; // iterate할때 위치

	public StrTokenEx(String str, String delimiter) {
		__init(str, delimiter);
	}

	public boolean hasMoreElements() {
		if (m_vecElem.size() > m_curPos + 1)
			return true;
		else
			return false;
	}

	public String nextElement() {
		String val;
		if (hasMoreElements()) {
			m_curPos++;
			val = (String) m_vecElem.get(m_curPos);
		} else {
			val = m_strEmpty;
		}
		return val;
	}

	public String at(int index) {
		if (index >= m_vecElem.size()) {
			return m_strEmpty;
		}

		return (String) m_vecElem.get(index);
	}

	private void __init(String str, String delimiter) {
		str = str.trim();
		m_strEmpty = "";
		m_strDelimiter = delimiter;
		m_curPos = -1;

		m_vecElem = new Vector();
		if (str.equals("") || delimiter.length() == 0)
			return;

		int start = 0;
		int pos = 0;
		int length = delimiter.length();
		int index = 0;

		while (pos != -1) {
			pos = str.indexOf(delimiter, start);
			if (pos != -1) {
				if (pos == start) {
					m_vecElem.add("");
				} else {
					m_vecElem.add(str.substring(start, pos));
				}
			} else if (str.length() > start) {
				m_vecElem.add(str.substring(start));
			} else {
				break;
			}
			start = pos + length;
		}
	}

	public final int getSize() {
		return m_vecElem.size();
	}
}
