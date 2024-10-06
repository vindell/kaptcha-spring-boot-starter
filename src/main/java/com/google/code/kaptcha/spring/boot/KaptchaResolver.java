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
package com.google.code.kaptcha.spring.boot;

import com.google.code.kaptcha.spring.boot.exception.CaptchaIncorrectException;
import com.google.code.kaptcha.spring.boot.exception.CaptchaTimeoutException;
import com.google.code.kaptcha.util.Config;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Date;

public interface KaptchaResolver {

	default void init(Config config){

	}

	default void init(String captchaStoreKey, String captchaDataStoreKey, long captchaTimeout){

	}
	
	/**
	 * Valid the current captcha via the given request.
	 * @param request request to be used for resolution
	 * @param capText the old captcha value
	 * @return the result
	 */
	boolean validCaptcha(HttpServletRequest request, String capText) throws CaptchaIncorrectException, CaptchaTimeoutException;

	/**
	 * Set the current captcha to the given one.
	 * @param request request to be used for captcha modification
	 * @param response response to be used for captcha modification
	 * @param capText the new captcha value
	 */
	void setCaptcha(HttpServletRequest request, HttpServletResponse response, String capText, Date capDate);
	
}
