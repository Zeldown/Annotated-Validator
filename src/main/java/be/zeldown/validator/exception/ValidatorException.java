package be.zeldown.validator.exception;

import java.lang.annotation.Annotation;

import be.zeldown.validator.AValidator;
import lombok.Getter;
import lombok.NonNull;

@Getter
public abstract class ValidatorException extends RuntimeException {

	private final AValidator<?> validator;
	private final Annotation    annotation;
	private final Object        value;
	private final String 	    rawMessage;

	public ValidatorException(final @NonNull AValidator<?> validator, final @NonNull Annotation annotation, final Object value, final @NonNull String message) {
		this.validator  = validator;
		this.annotation = annotation;
		this.value      = value;
		this.rawMessage = message.replace("{target}", validator.getTarget()).replace("{value}", value == null ? "null" : value.toString());
	}

	public String format(final @NonNull String text) {
		return text.replace("{target}", this.validator.getTarget()).replace("{value}", this.value == null ? "null" : this.value.toString());
	}

	@Override
	public String getMessage() {
		return "[" + this.validator.getClass().getSimpleName() + "][" + this.annotation.annotationType().getSimpleName() + "] " + this.format(this.rawMessage);
	}

}