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
package com.google.code.kaptcha.spring.boot.exception;

/**
 * 验证码已经过期异常
 * @author <a href="https://github.com/hiwepy">hiwepy</a>
 */
@SuppressWarnings("serial")
public class CaptchaTimeoutException extends Exception {

	public CaptchaTimeoutException() {
        super("captcha expired");
    }

    public CaptchaTimeoutException(String message) {
        super(message);
    }
    
}
