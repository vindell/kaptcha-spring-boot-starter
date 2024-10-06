/*
 * Copyright (c) 2017, hiwepy (https://github.com/hiwepy).
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.google.code.kaptcha.spring.boot.servlet;

import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.spring.boot.KaptchaResolver;
import com.google.code.kaptcha.util.Config;
import jakarta.servlet.Servlet;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;

/**
 * This servlet uses the settings passed into it via the Producer api.
 * @author <a href="https://github.com/hiwepy">hiwepy</a>
 * {@link com.google.code.kaptcha.servlet.KaptchaServlet}
 */
@SuppressWarnings("serial")
public class KaptchaJakartaServlet extends HttpServlet implements Servlet {
	
	private Properties props = new Properties();

	private Producer kaptchaProducer = null;

	private KaptchaResolver captchaResolver;
	
	public KaptchaJakartaServlet(KaptchaResolver captchaResolver) {
		this.captchaResolver = captchaResolver;
	}
	
	/*
	 * (non-Javadoc)
	 *
	 * @see javax.servlet.Servlet#init(javax.servlet.ServletConfig)
	 */
	@Override
	public void init(ServletConfig conf) throws ServletException {
		super.init(conf);

		// Switch off disk based caching.
		ImageIO.setUseCache(false);

		Enumeration<?> initParams = conf.getInitParameterNames();
		while (initParams.hasMoreElements()) {
			String key = (String) initParams.nextElement();
			String value = conf.getInitParameter(key);
			this.props.put(key, value);
		}

		Config config = new Config(this.props);
		this.kaptchaProducer = config.getProducerImpl();
		this.captchaResolver.init(config);
	}

	/** */
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		
		// Set to expire far in the past.
		resp.setDateHeader("Expires", 0);
		// Set standard HTTP/1.1 no-cache headers.
		resp.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
		// Set IE extended HTTP/1.1 no-cache headers (use addHeader).
		resp.addHeader("Cache-Control", "post-check=0, pre-check=0");
		// Set standard HTTP/1.0 no-cache header.
		resp.setHeader("Pragma", "no-cache");

		// return a jpeg
		resp.setContentType("image/jpeg");

		// create the text for the image
		String capText = this.kaptchaProducer.createText();
		
		this.captchaResolver.setCaptcha(req, resp, capText, new Date());

		// create the image with the text
		BufferedImage bi = this.kaptchaProducer.createImage(capText);

		ServletOutputStream out = resp.getOutputStream();

		// write the data out
		ImageIO.write(bi, "jpg", out);
	}
}
