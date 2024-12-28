package be.zeldown.validator;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

import be.zeldown.validator.annotation.NotNull;
import be.zeldown.validator.exception.ValidatorException;
import be.zeldown.validator.impl.primitive.number.NumberValidator;
import be.zeldown.validator.impl.primitive.string.StringValidator;
import lombok.Getter;

public class Validators {

	@Getter
	private static final List<AValidator<?>> validatorList = new ArrayList<>();

	static {
		Validators.register(NumberValidator.class);
		Validators.register(StringValidator.class);
	}

	public static void register(final @NotNull Class<? extends AValidator<?>> validator) {
		try {
			Validators.validatorList.add(validator.newInstance());
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static void register(final @NotNull AValidator<?> validator) {
		Validators.validatorList.add(validator);
	}

	public static List<AValidator<?>> get(final Object value) {
		final Class<?> type = value == null ? null : value.getClass();
		final List<AValidator<?>> validators = new ArrayList<>();

		for (final AValidator<?> validator : Validators.validatorList) {
			if (!validator.isAssignableFrom(type)) {
				continue;
			}

			validators.add(validator);
		}

		return validators;
	}

	public static ValidatorException validate(final @NotNull Field field, final Object value) {
		return Validators.validate(field, value, true);
	}

	public static ValidatorException validate(final @NotNull Field field, final Object value, final boolean throwException) {
		if (field.getAnnotations().length == 0) {
			return null;
		}

		for (final AValidator<?> validator : Validators.get(value)) {
			final ValidatorException exception = validator.build(field, throwException).parse(value);
			if (exception != null) {
				return exception;
			}
		}

		return null;
	}

	public static ValidatorException validate(final @NotNull Parameter parameter, final Object value) {
		return Validators.validate(parameter, value, true);
	}

	public static ValidatorException validate(final @NotNull Parameter parameter, final Object value, final boolean throwException) {
		if (parameter.getAnnotations().length == 0) {
			return null;
		}

		for (final AValidator<?> validator : Validators.get(value)) {
			final ValidatorException exception = validator.build(parameter, throwException).parse(value);
			if (exception != null) {
				return exception;
			}
		}

		return null;
	}

	public static ValidatorException validate(final @NotNull String target, final @NotNull List<Annotation> annotations, final Object value) {
		return Validators.validate(target, annotations, value, true);
	}

	public static ValidatorException validate(final @NotNull String target, final @NotNull List<Annotation> annotations, final Object value, final boolean throwException) {
		if (annotations.size() == 0) {
			return null;
		}

		for (final AValidator<?> validator : Validators.get(value)) {
			final ValidatorException exception = validator.build(target, annotations, throwException).parse(value);
			if (exception != null) {
				return exception;
			}
		}

		return null;
	}

}