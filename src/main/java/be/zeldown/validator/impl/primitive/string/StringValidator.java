package be.zeldown.validator.impl.primitive.string;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import be.zeldown.validator.AValidator;
import be.zeldown.validator.exception.ValidatorException;
import lombok.NonNull;

public class StringValidator extends AValidator<String> {

	public StringValidator() {
		super(String.class);
	}

	@Override
	protected void validate(final String value) {
		super.get(Length.class, length -> super.except(() -> value.length() >= length.min() && value.length() <= length.max(), new StringValidatorLengthException(this, length, value)));
		super.get(Regex.class, regex -> super.except(() -> value.matches(regex.value()), new StringValidatorRegexException(this, regex, value)));
		super.get(Empty.class, empty -> super.except(() -> value.isEmpty(), new StringValidatorEmptyException(this, empty, value)));
		super.get(NotEmpty.class, notEmpty -> super.except(() -> !value.isEmpty(), new StringValidatorNotEmptyException(this, notEmpty, value)));
	}

	/* [ Annotations ] */
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD, ElementType.PARAMETER})
	public @interface Length {

		double min() default -Double.MAX_VALUE;
		double max() default Double.MAX_VALUE;

	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD, ElementType.PARAMETER})
	public @interface Regex {

		String value();

	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD, ElementType.PARAMETER})
	public @interface Empty {}

	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD, ElementType.PARAMETER})
	public @interface NotEmpty {}

	/* [ Exceptions ] */
	public class StringValidatorLengthException extends ValidatorException {

		public StringValidatorLengthException(final @NonNull AValidator<?> validator, final @NonNull Annotation annotation, final Object value) {
			super(validator, annotation, value, "the target \"{target}\"'s length must be between {min} and {max} but found {value}");
		}

		@Override
		public String format(final @NonNull String text) {
			final Length length = (Length) super.getAnnotation();
			return super.format(text).replace("{min}", String.valueOf(length.min())).replace("{max}", String.valueOf(length.max()));
		}

	}

	public class StringValidatorRegexException extends ValidatorException {

		public StringValidatorRegexException(final @NonNull AValidator<?> validator, final @NonNull Annotation annotation, final Object value) {
			super(validator, annotation, value, "the target \"{target}\" must match the regex pattern \"{regex}\" but found {value}");
		}

		@Override
		public String format(final @NonNull String text) {
			final Regex regex = (Regex) super.getAnnotation();
			return super.format(text).replace("{regex}", regex.value());
		}

	}

	public class StringValidatorEmptyException extends ValidatorException {

		public StringValidatorEmptyException(final @NonNull AValidator<?> validator, final @NonNull Annotation annotation, final Object value) {
			super(validator, annotation, value, "the target \"{target}\" must be empty but found {value}");
		}

	}

	public class StringValidatorNotEmptyException extends ValidatorException {

		public StringValidatorNotEmptyException(final @NonNull AValidator<?> validator, final @NonNull Annotation annotation, final Object value) {
			super(validator, annotation, value, "the target \"{target}\" must not be empty");
		}

	}

}