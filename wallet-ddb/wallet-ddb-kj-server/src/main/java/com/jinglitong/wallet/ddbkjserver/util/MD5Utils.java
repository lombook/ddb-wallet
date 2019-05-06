package com.jinglitong.wallet.ddbkjserver.util;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.util.*;


public class MD5Utils {

	
	 private static final String hexDigIts[] = {"0","1","2","3","4","5","6","7","8","9","a","b","c","d","e","f"};

	 public static final String KEY = "!#&Gh$as&a*hd";
	 
	 
	 
	    public static String makeSign(String json) throws Exception{
	    	Map<String, Object> map =json2Map(json);
			String s = ToSortedUrlParams(map);
			s = s + "&key=" + KEY;
			return MD5Encode(s,"utf8").toUpperCase();
		}

	    public static boolean toSign(String json) throws Exception{
	    	Map<String, Object> map =json2Map(json);
	    	String oldKey = (String) map.get("sign");
	    	map.remove("sign");
			String s = ToSortedUrlParams(map);
			s = s + "&key=" + KEY;
			String newKey =MD5Encode(s,"utf8").toUpperCase();
			if(newKey.equals(oldKey)) {
				return true;
			}else {
				return false;
			}
		}
	    /**
	     * MD5加密
	     * @param origin 字符
	     * @param charsetname 编码
	     * @return
	     */
	    public static String MD5Encode(String origin, String charsetname){
	        String resultString = null;
	        try{
	            resultString = new String(origin);
	            MessageDigest md = MessageDigest.getInstance("MD5");
	            if(null == charsetname || "".equals(charsetname)){
	                resultString = byteArrayToHexString(md.digest(resultString.getBytes()));
	            }else{
	                resultString = byteArrayToHexString(md.digest(resultString.getBytes(charsetname)));
	            }
	        }catch (Exception e){

	        }
	        return resultString;
	    }


	    public static String byteArrayToHexString(byte b[]){
	        StringBuffer resultSb = new StringBuffer();
	        for(int i = 0; i < b.length; i++){
	            resultSb.append(byteToHexString(b[i]));
	        }
	        return resultSb.toString();
	    }

	    public static String byteToHexString(byte b){
	        int n = b;
	        if(n < 0){
	            n += 256;
	        }
	        int d1 = n / 16;
	        int d2 = n % 16;
	        return hexDigIts[d1] + hexDigIts[d2];
	    }
	    
	    
	    /**
	     * 
	     * 功能说明:对map排序
	     * @param map
	     * @return
	     */
		public static String ToSortedUrlParams(Map<String, Object> map){
			List<Map.Entry<String, Object>> list = new ArrayList<>();
	        for(Map.Entry<String, Object> entry : map.entrySet()){
	             list.add(entry);
	        }
	        list.sort(new Comparator<Map.Entry<String, Object>>(){
	               @Override
	               public int compare(Map.Entry<String, Object> o1, Map.Entry<String, Object> o2) {
	                    return o1.getKey().compareTo(o2.getKey());
				   }
	        }); 
	        StringBuffer stb = new StringBuffer();
	        for(Map.Entry<String, Object> entry: list){
				if(!entry.getKey().equals("sign")){
	              stb.append(entry.getKey());
				  stb.append("=");
				  stb.append(entry.getValue());
				  stb.append("&");
				}
	        }
			stb.delete(stb.length()-1,stb.length());
			return stb.toString();
		}
		/**
		 * 
		 * 功能说明:json转map
		 * @param json
		 * @return
		 * @throws Exception
		 */
		public static Map json2Map(String json) throws Exception{
	    	Map map=new HashMap();  
			JSONObject jsonObject = new JSONObject(json);
			Iterator iterator = jsonObject.keys();
			while (iterator.hasNext()) {
				String key = (String)iterator.next();
				String value = jsonObject.get(key).toString();
				if(value.startsWith("{")&&value.endsWith("}")){  
		            map.put(key, json2Map(value));  
		        }else{  
		            map.put(key, value); 
		        }  
			}
		return map;
	}
	public static void main(String[] args) throws Exception {
		
		System.out.println(makeSign("{\"currencyPrice\":\"10\",\"flowId\":\"fdfdfdfdfdfdfd1123\",\"userId\":\"00002cadc1f548929fc2a48d8e361618\",\"nonceStr\":\"3\"}"));
	}
}
