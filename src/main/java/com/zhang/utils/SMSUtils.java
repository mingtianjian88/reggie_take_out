package com.zhang.utils;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

/**
 * 短信发送工具类
 */
public class SMSUtils {

	/**
	 * 发送短信
	 * @param email
	 * @param authCode
	 */
	public static void sendMessage(String email, String authCode){
		try {
			SimpleEmail mail = new SimpleEmail();
			mail.setHostName("smtp.qq.com");//发送邮件的服务器,这个是qq邮箱的，不用修改
			mail.setAuthentication("572474382@qq.com", "xjaaxhqugfuabegh");//第一个参数是对应的邮箱用户名一般就是自己的邮箱第二个参数就是SMTP的密码,我们上面获取过了
			mail.setFrom("572474382@qq.com","zhang");  //发送邮件的邮箱和发件人
			mail.setSSLOnConnect(true); //使用安全链接
			mail.addTo(email);//接收的邮箱
			mail.setSubject("验证码");//设置邮件的主题
			mail.setMsg("尊敬的用户:你好!\n 登陆验证码为:" + authCode+"\n"+"     (有效期为一分钟)");//设置邮件的内容
			mail.send();//发送
		} catch (EmailException e) {
			e.printStackTrace();
		}

	}
}
