package com.google.code.kaptcha.spring.boot;


import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.spring.boot.servlet.KaptchaJakartaServlet;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

@Configuration
@ConditionalOnClass({ Producer.class })
@EnableConfigurationProperties(KaptchaProperties.class)
public class KaptchaAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean(KaptchaResolver.class)
	public KaptchaResolver kaptchaResolver(KaptchaProperties properties) {
		
		KaptchaResolver kaptchaResolver = new SessionKaptchaResolver(); 
		// 初始化参数
		kaptchaResolver.init(properties.getCaptchaStoreKey(), properties.getCaptchaDateStoreKey(), properties.getCaptchaTimeout());
		
		return kaptchaResolver;
	}
	
	// 验证码
	@Bean
	@ConditionalOnMissingBean(name = "kaptchaServlet")
	public ServletRegistrationBean<KaptchaJakartaServlet> servletRegistrationBean(KaptchaProperties properties, KaptchaResolver kaptchaResolver) {

		ServletRegistrationBean<KaptchaJakartaServlet> registrationBean = new ServletRegistrationBean<KaptchaJakartaServlet>();
		
		KaptchaJakartaServlet kaptchaServlet = new KaptchaJakartaServlet(kaptchaResolver);

		registrationBean.setServlet(kaptchaServlet);
		
		// 默认参数
		registrationBean.addInitParameter(Constants.KAPTCHA_BORDER, "no");
		registrationBean.addInitParameter(Constants.KAPTCHA_BORDER_COLOR, "black");
		registrationBean.addInitParameter(Constants.KAPTCHA_TEXTPRODUCER_FONT_COLOR, "black");
		registrationBean.addInitParameter(Constants.KAPTCHA_TEXTPRODUCER_CHAR_SPACE, "5");
		registrationBean.addInitParameter(Constants.KAPTCHA_TEXTPRODUCER_CHAR_LENGTH, "4");

		if (!CollectionUtils.isEmpty(properties.getParameters())) {
			registrationBean.setInitParameters(properties.getParameters());
		}

		registrationBean.addUrlMappings(properties.getPattern());

		return registrationBean;
	}
	
}
