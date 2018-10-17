package com.icloud.cronin.peter.util;

import android.content.Context;
import android.widget.EditText;

import com.throrinstudio.android.common.libs.validator.Validate;
import com.throrinstudio.android.common.libs.validator.validator.NotEmptyValidator;

public class ValidationUtil {

	public static boolean  checkValidField(Context c,EditText editText) {
		NotEmptyValidator notEmptyValidator=new NotEmptyValidator(c);
		Validate validate =new Validate(editText);
		validate.addValidator(notEmptyValidator);
		return validate.isValid();
	}
	
}
