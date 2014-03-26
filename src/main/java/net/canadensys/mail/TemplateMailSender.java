package net.canadensys.mail;

import java.io.IOException;
import java.util.Map;

import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * Class responsible to send HTML email built from Freemarker template
 * @author canandesys
 *
 */
public class TemplateMailSender {
	
	//get log4j handler
	private static final Logger LOGGER = Logger.getLogger(TemplateMailSender.class);
	
	private JavaMailSender mailSender;

	private Configuration freemarkerConfig;
	
	private String from;
		
	public boolean sendMessage(final String toEmail, final String subject, final Map<String,Object> model, final String templateName){
		
		if(templateName == null){
			LOGGER.fatal("FTL tempale is null. Maybe a wrong locale?");
			return false;
		}
		
		try {			
			final Template template = freemarkerConfig.getTemplate(templateName);
			MimeMessagePreparator preparator = new MimeMessagePreparator() {
		         public void prepare(MimeMessage mimeMessage) throws Exception {
		            MimeMessageHelper message = new MimeMessageHelper(mimeMessage,"utf-8");
		            message.setTo(toEmail);
		            message.setFrom(from);
		            message.setSubject(subject);
		            String text = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
		            message.setText(text, true);
		         }
		     };
		     mailSender.send(preparator);
		} catch (IOException e) {
			LOGGER.fatal("Email sending error",e);
			return false;
		}
		catch (MailException e) {
			LOGGER.fatal("Email sending error",e);
			return false;
		}
		return true;
	}

	public void setMailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	public void setFreemarkerConfig(Configuration freemarkerConfig) {
		this.freemarkerConfig = freemarkerConfig;
	}

	public void setFrom(String from) {
		this.from = from;
	}
}
