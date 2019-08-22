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
	public static int MATCH_TYPE_MIN = 1; //最小匹配规则
	public static int MATCH_TYPE_MAX = 2; //最大匹配规则
	private Map<String, Object> sensitiveWordMap = null;
	public static final String FILE_PATH = "d:/badword.txt";
	private Thread reloadDFATread;
	private static SensitivewordFilter mInstance = new SensitivewordFilter();

	/**
	 * 构造函数，初始化敏感词库
	 */
	private SensitivewordFilter() {
//		filePathList = new ArrayList<String>();
	}

	public static SensitivewordFilter getInstance() {
		return mInstance;
	}

	/**
	 * 初始化非法词库
	 * @return: void
	 */
	public void initWords() {
		long time = System.currentTimeMillis();
		load();
		System.out.println("SensitivewordFilter initWords use " + (System.currentTimeMillis() - time));
	}

	/**
	 * 判断传入的字符串是否包含非法字符
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

		//过滤掉所有的特殊字符和英文字符，只有汉字
		String words_chn = words.replaceAll("[^\u4e00-\u9fa5]", "");
		if (!TextUtils.isEmpty(words_chn) && hasSensitiveWord(words_chn, MATCH_TYPE_MIN)) {
			return true;
		}
		//只有英文
		String words_eng = words.replaceAll("[^a-zA-Z ]", "");
		if (!TextUtils.isEmpty(words_eng) && hasSensitiveWord(words_eng, MATCH_TYPE_MIN)) {
			return true;
		}
		//只有数字
		String words_num = words.replaceAll("[^0-9]", "");
		if (!TextUtils.isEmpty(words_num) && hasSensitiveWord(words_num, MATCH_TYPE_MIN)) {
			return true;
		}
		//字母数字组合
		String words_num_eng = words.replaceAll("[^a-zA-Z0-9]", "");
		if (!TextUtils.isEmpty(words_num_eng) && hasSensitiveWord(words_num_eng, MATCH_TYPE_MIN)) {
			return true;
		}
		//汉字字母结合
		String words_chn_eng = words.replaceAll("[^\u4e00-\u9fa5a-zA-Z]", "");
		if (!TextUtils.isEmpty(words_chn_eng) && hasSensitiveWord(words_chn_eng, MATCH_TYPE_MIN)) {
			return true;
		}
		//全部
		if (!TextUtils.isEmpty(words) && hasSensitiveWord(words, MATCH_TYPE_MIN)) {
			return true;
		}

		return false;
	}

	/**
	 * 判断文字中是否有敏感词
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
			int length = checkSensitiveWord(textWord, i, matchType); //判断是否包含敏感字符
			if (length > 0) {
				return true;
			}
		}
		
		return false;
	}

	/**
	 * 检查文字中是否包含敏感字符，检查规则如下：<br>
	 * @param txt
	 * @param beginIndex
	 * @param matchType
	 * @return，如果存在，则返回敏感词字符的长度，不存在返回0
	 */
	@SuppressWarnings({ "rawtypes" })
	private int checkSensitiveWord(String txt, int beginIndex, int matchType) {
		boolean flag = false; //敏感词结束标识位：用于敏感词只有1位的情况
		int matchFlag = 0; //匹配标识数默认为0
		String word = "";
		Map<String, Object> nowMap = getSensitiveWordMap();
		for (int i = beginIndex; i < txt.length(); i++) {
			word = txt.substring(i, i + 1);
			nowMap = (Map) nowMap.get(word); //获取指定key
			if (nowMap != null) { //存在，则判断是否为最后一个
				matchFlag++; //找到相应key，匹配标识+1 
				if ("1".equals(nowMap.get("isEnd"))) { //如果为最后一个匹配规则,结束循环，返回匹配标识数
					flag = true; //结束标志位为true   
					if (SensitivewordFilter.MATCH_TYPE_MIN == matchType) { //最小规则，直接返回,最大规则还需继续查找
						break;
					}
				}
			}
			else { //不存在，直接返回
				break;
			}
		}
		if (matchFlag < 1 || !flag) { //长度必须大于等于1，为词 
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

	//生成map
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

	//读取敏感词库
	private Set<String> getSensitiveWordList(String path) {
		return getSensitiveWordListFromFile(path);
	}

	//从assets目录的文件中读取敏感词库
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

	//从文件系统的文件中读取敏感词库
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
	 * 读取敏感词库，将敏感词放入HashSet中，构建一个DFA算法模型：<br>
	 * 中 = {
	 *      isEnd = 0
	 *      国 = {<br>
	 *      	 isEnd = 1
	 *           人 = {isEnd = 0
	 *                民 = {isEnd = 1}
	 *                }
	 *           男  = {
	 *           	   isEnd = 0
	 *           		人 = {
	 *           			 isEnd = 1
	 *           			}
	 *           	}
	 *           }
	 *      }
	 *  五 = {
	 *      isEnd = 0
	 *      星 = {
	 *      	isEnd = 0
	 *      	红 = {
	 *              isEnd = 0
	 *              旗 = {
	 *                   isEnd = 1
	 *                  }
	 *              }
	 *      	}
	 *      }
	 * @date 2014年4月20日 下午3:04:20
	 * @param keyWordSet  敏感词库
	 * @version 1.0
	 */
	private Map<String, Object> addSensitiveWordToHashMap(Map<String, Object> sensitiveWordMap, Set<String> keyWordSet) {
		String key = null;
		Map<String, Object> nowMap = null;
		Map<String, Object> newWorMap = null;
		//迭代keyWordSet
		Iterator<String> iterator = keyWordSet.iterator();
		while (iterator.hasNext()) {
			key = iterator.next(); //关键字
			//key = key.toLowerCase();
			nowMap = sensitiveWordMap;
			for (int i = 0; i < key.length(); i++) {
				String keyChar = key.substring(i, i + 1); //转换成char型
				Object wordMap = nowMap.get(keyChar); //获取

				if (wordMap != null) { //如果存在该key，直接赋值
					nowMap = (Map) wordMap;
				}
				else { //不存在则，则构建一个map，同时将isEnd设置为0，因为他不是最后一个
					newWorMap = new HashMap<String, Object>();
					newWorMap.put("isEnd", "0"); //不是最后一个
					nowMap.put(keyChar, newWorMap);
					nowMap = newWorMap;
				}
				//Is the last one?
				if (i == key.length() - 1) {
					nowMap.put("isEnd", "1"); //最后一个
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
