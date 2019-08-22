package com.guagua.live.utils;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import com.guagua.live.sdk.SensitivewordFilterInterface;
import com.pix.utils.CloseUtils;
import com.pix.utils.TextUtils;

public class SensitivewordFilter implements SensitivewordFilterInterface {
	private static final String TAG = "SensitivewordFilter";
	public static int MATCH_TYPE_MIN = 1; //��Сƥ�����
	public static int MATCH_TYPE_MAX = 2; //���ƥ�����
	private Map<String, Object> sensitiveWordMap = null;
	public static final String FILE_PATH = "d:/badword.txt";
	private Thread reloadDFATread;
	private static SensitivewordFilter mInstance = new SensitivewordFilter();

	/**
	 * ���캯������ʼ�����дʿ�
	 */
	private SensitivewordFilter() {
//		filePathList = new ArrayList<String>();
	}

	public static SensitivewordFilter getInstance() {
		return mInstance;
	}

	/**
	 * ��ʼ���Ƿ��ʿ�
	 * @return: void
	 */
	public void initWords() {
		long time = System.currentTimeMillis();
		load();
		System.out.println("SensitivewordFilter initWords use " + (System.currentTimeMillis() - time));
	}

	/**
	 * �жϴ�����ַ����Ƿ�����Ƿ��ַ�
	 * @param words
	 * @return: boolean
	 */
	public boolean isContainSensitiveWord(String words) {
		if (sensitiveWordMap == null)
			return false;

		if (TextUtils.isEmpty(words))
			return false;

		words = words.trim();
		//words = words.toLowerCase();

		//���˵����е������ַ���Ӣ���ַ���ֻ�к���
		String words_chn = words.replaceAll("[^\u4e00-\u9fa5]", "");
		if (!TextUtils.isEmpty(words_chn) && hasSensitiveWord(words_chn, MATCH_TYPE_MIN)) {
			return true;
		}
		//ֻ��Ӣ��
		String words_eng = words.replaceAll("[^a-zA-Z ]", "");
		if (!TextUtils.isEmpty(words_eng) && hasSensitiveWord(words_eng, MATCH_TYPE_MIN)) {
			return true;
		}
		//ֻ������
		String words_num = words.replaceAll("[^0-9]", "");
		if (!TextUtils.isEmpty(words_num) && hasSensitiveWord(words_num, MATCH_TYPE_MIN)) {
			return true;
		}
		//��ĸ�������
		String words_num_eng = words.replaceAll("[^a-zA-Z0-9]", "");
		if (!TextUtils.isEmpty(words_num_eng) && hasSensitiveWord(words_num_eng, MATCH_TYPE_MIN)) {
			return true;
		}
		//������ĸ���
		String words_chn_eng = words.replaceAll("[^\u4e00-\u9fa5a-zA-Z]", "");
		if (!TextUtils.isEmpty(words_chn_eng) && hasSensitiveWord(words_chn_eng, MATCH_TYPE_MIN)) {
			return true;
		}
		//ȫ��
		if (!TextUtils.isEmpty(words) && hasSensitiveWord(words, MATCH_TYPE_MIN)) {
			return true;
		}

		return false;
	}

	/**
	 * �ж��������Ƿ������д�
	 * @param textWord
	 * @param matchType
	 * @return
	 * @return: boolean
	 */
	private boolean hasSensitiveWord(String textWord, int matchType) {
		if(TextUtils.isEmpty(textWord))
			return false;
		
		int size = textWord.length();
		for (int i = 0; i < size; i++) {
			int length = checkSensitiveWord(textWord, i, matchType); //�ж��Ƿ���������ַ�
			if (length > 0) {
				return true;
			}
		}
		
		return false;
	}

	/**
	 * ����������Ƿ���������ַ������������£�<br>
	 * @param txt
	 * @param beginIndex
	 * @param matchType
	 * @return��������ڣ��򷵻����д��ַ��ĳ��ȣ������ڷ���0
	 */
	@SuppressWarnings({ "rawtypes" })
	private int checkSensitiveWord(String txt, int beginIndex, int matchType) {
		boolean flag = false; //���дʽ�����ʶλ���������д�ֻ��1λ�����
		int matchFlag = 0; //ƥ���ʶ��Ĭ��Ϊ0
		String word = "";
		Map<String, Object> nowMap = getSensitiveWordMap();
		for (int i = beginIndex; i < txt.length(); i++) {
			word = txt.substring(i, i + 1);
			nowMap = (Map) nowMap.get(word); //��ȡָ��key
			if (nowMap != null) { //���ڣ����ж��Ƿ�Ϊ���һ��
				matchFlag++; //�ҵ���Ӧkey��ƥ���ʶ+1 
				if ("1".equals(nowMap.get("isEnd"))) { //���Ϊ���һ��ƥ�����,����ѭ��������ƥ���ʶ��
					flag = true; //������־λΪtrue   
					if (SensitivewordFilter.MATCH_TYPE_MIN == matchType) { //��С����ֱ�ӷ���,���������������
						break;
					}
				}
			}
			else { //�����ڣ�ֱ�ӷ���
				break;
			}
		}
		if (matchFlag < 1 || !flag) { //���ȱ�����ڵ���1��Ϊ�� 
			matchFlag = 0;
		}
		return matchFlag;
	}

	private void load() {
		if (reloadDFATread != null && reloadDFATread.isAlive()) {
			return;
		}

		reloadDFATread = new Thread() {
			@Override
			public void run() {
				reloadDFA();
			}
		};
		reloadDFATread.start();
	}

	private Map<String, Object> getSensitiveWordMap() {
		return sensitiveWordMap;
	}

	//����map
	private void reloadDFA() {
		Map<String, Object> map = new HashMap<String, Object>();
//		for (int i = 0; i < filePathList.size(); i++) {
//			Set<String> words = getSensitiveWordList(filePathList.get(i));
//			addSensitiveWordToHashMap(map, words);
//		}
		Set<String> words = getSensitiveWordList(FILE_PATH);
		System.out.println("words:" + words);
		addSensitiveWordToHashMap(map, words);
		sensitiveWordMap = map;
	}

	//��ȡ���дʿ�
	private Set<String> getSensitiveWordList(String path) {
		return getSensitiveWordListFromFile(path);
	}

	//��assetsĿ¼���ļ��ж�ȡ���дʿ�
	private Set<String> getSensitiveWordListFromAsset(String path) {
		Set<String> data = new HashSet<String>();
		InputStream is = null;
		BufferedReader br = null;

		try {
			is = new FileInputStream(new File(path));
			br = new BufferedReader(new InputStreamReader(is));

			String line = null;
			while (!TextUtils.isEmpty((line = br.readLine()))) {
				data.add(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			CloseUtils.close(br);
			CloseUtils.close(is);
		}
		return data;
	}

	//���ļ�ϵͳ���ļ��ж�ȡ���дʿ�
	private Set<String> getSensitiveWordListFromFile(String path) {
		Set<String> data = new HashSet<String>();

		FileReader fr = null;
		BufferedReader br = null;
		try {
			File file = new File(path);
			if (file.exists()) {
				fr = new FileReader(file);
				br = new BufferedReader(fr);

				String line = null;
				while (!TextUtils.isEmpty((line = br.readLine()))) {
					data.add(line);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			
			CloseUtils.close(fr);
			CloseUtils.close(br);
		}
		return data;
	}

	/**
	 * ��ȡ���дʿ⣬�����дʷ���HashSet�У�����һ��DFA�㷨ģ�ͣ�<br>
	 * �� = {
	 *      isEnd = 0
	 *      �� = {<br>
	 *      	 isEnd = 1
	 *           �� = {isEnd = 0
	 *                �� = {isEnd = 1}
	 *                }
	 *           ��  = {
	 *           	   isEnd = 0
	 *           		�� = {
	 *           			 isEnd = 1
	 *           			}
	 *           	}
	 *           }
	 *      }
	 *  �� = {
	 *      isEnd = 0
	 *      �� = {
	 *      	isEnd = 0
	 *      	�� = {
	 *              isEnd = 0
	 *              �� = {
	 *                   isEnd = 1
	 *                  }
	 *              }
	 *      	}
	 *      }
	 * @date 2014��4��20�� ����3:04:20
	 * @param keyWordSet  ���дʿ�
	 * @version 1.0
	 */
	private Map<String, Object> addSensitiveWordToHashMap(Map<String, Object> sensitiveWordMap, Set<String> keyWordSet) {
		String key = null;
		Map<String, Object> nowMap = null;
		Map<String, Object> newWorMap = null;
		//����keyWordSet
		Iterator<String> iterator = keyWordSet.iterator();
		while (iterator.hasNext()) {
			key = iterator.next(); //�ؼ���
			//key = key.toLowerCase();
			nowMap = sensitiveWordMap;
			for (int i = 0; i < key.length(); i++) {
				String keyChar = key.substring(i, i + 1); //ת����char��
				Object wordMap = nowMap.get(keyChar); //��ȡ

				if (wordMap != null) { //������ڸ�key��ֱ�Ӹ�ֵ
					nowMap = (Map) wordMap;
				}
				else { //���������򹹽�һ��map��ͬʱ��isEnd����Ϊ0����Ϊ���������һ��
					newWorMap = new HashMap<String, Object>();
					newWorMap.put("isEnd", "0"); //�������һ��
					nowMap.put(keyChar, newWorMap);
					nowMap = newWorMap;
				}
				//Is the last one?
				if (i == key.length() - 1) {
					nowMap.put("isEnd", "1"); //���һ��
				}
			}
		}

		return sensitiveWordMap;
	}

	public enum Scheme {
		FILE("file"), ASSETS("assets"), UNKNOWN("");

		private String scheme;
		private String uriPrefix;

		Scheme(String scheme) {
			this.scheme = scheme;
			uriPrefix = scheme + "://";
		}

		/**
		 * Defines scheme of incoming URI
		 *
		 * @param uri URI for scheme detection
		 * @return Scheme of incoming URI
		 */
		public static Scheme ofUri(String uri) {
			if (uri != null) {
				for (Scheme s : values()) {
					if (s.belongsTo(uri)) {
						return s;
					}
				}
			}
			return UNKNOWN;
		}

		private boolean belongsTo(String uri) {
			return uri.toLowerCase(Locale.US).startsWith(uriPrefix);
		}

		/** Appends scheme to incoming path */
		public String wrap(String path) {
			return uriPrefix + path;
		}

		/** Removed scheme part ("scheme://") from incoming URI */
		public String crop(String uri) {
			if (!belongsTo(uri)) {
				throw new IllegalArgumentException(String.format("URI [%1$s] doesn't have expected scheme [%2$s]", uri, scheme));
			}
			return uri.substring(uriPrefix.length());
		}
	}
}
