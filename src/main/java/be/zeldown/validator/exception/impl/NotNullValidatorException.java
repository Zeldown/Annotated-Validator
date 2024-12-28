package be.zeldown.validator.exception.impl;

import java.lang.annotation.Annotation;

import be.zeldown.validator.AValidator;
import be.zeldown.validator.exception.ValidatorException;
import lombok.NonNull;

public class NotNullValidatorException extends ValidatorException {

	public NotNullValidatorException(final @NonNull AValidator<?> validator, final @NonNull Annotation annotation, final Object value) {
		super(validator, annotation, value, "the target \"{target}\" must not be null");
	}

	@Override
	public String format(final @NonNull String text) {
		return text;
	}

}