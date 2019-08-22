package com.guagua.live.sdk;

/**
 * <p>Copyright: Copyright (c) 2016</p>
 * <p/>
 * <p>Company: 浙江齐聚科技有限公司<a href="www.guagua.cn">www.guagua.cn</a></p>
 *
 * @description 敏感词接口
 *
 * @author Xue Wenchao
 * @version 1.0.0
 * @modify
 */
public interface SensitivewordFilterInterface {
    boolean isContainSensitiveWord(String words);
}