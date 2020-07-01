package zhou.hao.email;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import zhou.hao.BinanSocket.utility.Global;
import zhou.hao.BinanSocket.utility.MLog;

public class EmailProcessor {
	public static void send(String recipient, String subject, String content) throws Exception{
		Properties properties = new Properties();
        properties.put("mail.transport.protocol", "smtp");// 连接协议
        properties.put("mail.smtp.host", "smtp.qq.com");// 主机名
        properties.put("mail.smtp.port", 465);// 端口号
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.ssl.enable", "true");// 设置是否使用ssl安全连接 ---一般都使用
//        properties.put("mail.debug", "true");// 设置是否显示debug信息 true 会在控制台显示相关信息
        // 得到回话对象
        Session session = Session.getInstance(properties);
        // 获取邮件对象
        Message message = new MimeMessage(session);
        // 设置发件人邮箱地址
        message.setFrom(new InternetAddress("774941077@qq.com"));
        // 设置收件人邮箱地址 
//        message.setRecipients(Message.RecipientType.TO, new InternetAddress[]{new InternetAddress("xxx@qq.com"),new InternetAddress("xxx@qq.com"),new InternetAddress("xxx@qq.com")});
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));//一个收件人
        // 设置邮件标题
        message.setSubject(subject);
        // 设置邮件内容
        message.setText(content);
        // 得到邮差对象
        Transport transport = session.getTransport();
        // 连接自己的邮箱账户
        transport.connect("XXXX@qq.com", "XXXX");// 密码为QQ邮箱开通的stmp服务后得到的客户端授权码
        // 发送邮件
        transport.sendMessage(message, message.getAllRecipients());
        transport.close();
	}
	
	public static void asynSend(String recipient, String subject, String content) throws Exception{
		new EmailThread(recipient, subject, content).start();
	}
	
	public static void main(String[] args) throws Exception{
		EmailProcessor.asynSend(Global.emailNotification, "test", "test");
	}
}

class EmailThread extends Thread{
	private List<String> recipients = null;
	private  String subject;
	private String content;
	public EmailThread(String recipient, String subject, String content) {
		this.recipients = new ArrayList<>();
		this.recipients.add(recipient);
		this.subject = subject;
		this.content = content;
	}
	
	public void run() {
		try {
			if(null != recipients) {
				EmailProcessor.send(recipients.get(0), subject, content);
			}
		} catch (Exception e) {
			// TODO: handle exception
			if(null != recipients) {
				MLog.writeLineCTA("发送邮件异常: " + e.getMessage());
			}
		}
	}
}

