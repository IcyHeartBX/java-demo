package com.pix.sensitiveword;

import com.guagua.live.utils.SensitivewordFilter;

public class TestMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SensitivewordFilter.getInstance().initWords();
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				while(true) {
					try {
						Thread.sleep(5000);
						boolean flag = SensitivewordFilter.getInstance().isContainSensitiveWord("ÖÐ1¹ú1ÈË1");
						System.out.println("bad word is:" + flag);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}
		}).start();
		
	}

}
