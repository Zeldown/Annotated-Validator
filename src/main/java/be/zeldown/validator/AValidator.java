package be.zeldown.validator;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import be.zeldown.validator.annotation.NotNull;
import be.zeldown.validator.exception.ValidatorException;
import be.zeldown.validator.exception.impl.NotNullValidatorException;
import lombok.Getter;
import lombok.NonNull;

@Getter
public abstract class AValidator<T> {

	private final Class<?>[] types;

	private String target;
	private List<Annotation> annotations;
	private boolean throwException;

	protected AValidator(final @NonNull Class<?> @NonNull... types) {
		this.types = types;
	}

	public final @NonNull AValidator<T> build(final @NonNull Field field) {
		this.build(field.getName(), field, true);
		return this;
	}

	public final @NonNull AValidator<T> build(final @NonNull Field field, final boolean throwException) {
		this.build(field.getName(), field, throwException);
		return this;
	}

	public final @NonNull AValidator<T> build(final @NonNull Parameter parameter) {
		this.build(parameter.getName(), parameter, true);
		return this;
	}

	public final @NonNull AValidator<T> build(final @NonNull Parameter parameter, final boolean throwException) {
		this.build(parameter.getName(), parameter, throwException);
		return this;
	}

	public final @NonNull AValidator<T> build(final @NonNull String target, final @NonNull AnnotatedElement parameter) {
		this.build(target, Arrays.asList(parameter.getAnnotations()), true);
		return this;
	}

	public final @NonNull AValidator<T> build(final @NonNull String target, final @NonNull AnnotatedElement parameter, final boolean throwException) {
		this.build(target, Arrays.asList(parameter.getAnnotations()), throwException);
		return this;
	}

	public final @NonNull AValidator<T> build(final @NonNull String target, final @NonNull List<Annotation> annotations) {
		this.build(target, annotations, true);
		return this;
	}

	public final @NonNull AValidator<T> build(final @NonNull String target, final @NonNull List<Annotation> annotations, final boolean throwException) {
		this.target = target;
		this.annotations = annotations;
		this.throwException = throwException;
		return this;
	}

	public final boolean isAssignableFrom(final Class<?> type) {
		if (type == null) {
			return true;
		}

		for (final Class<?> clazz : this.types) {
			if (clazz.isAssignableFrom(type)) {
				return true;
			}
		}

		return false;
	}

	public final ValidatorException parse(final Object value) throws ValidatorException {
		if (this.annotations == null) {
			throw new IllegalStateException("The validator " + this.getClass().getSimpleName() + " is not initialized.");
		}

		if (this.annotations.isEmpty()) {
			return null;
		}

		if (this.types.length == 0) {
			throw new IllegalStateException("The validator " + this.getClass().getSimpleName() + " does not have any type.");
		}

		if (value == null) {
			try {
				this.get(NotNull.class, annotation -> {
					throw new NotNullValidatorException(this, annotation, value);
				});

				this.validate(null);
				return null;
			} catch (final ValidatorException exception) {
				if (this.throwException) {
					throw exception;
				}
				return exception;
			}
		}

		for (final Class<?> type : this.types) {
			if (!type.isAssignableFrom(value.getClass())) {
				continue;
			}

			final T castedValue = (T) value;
			try {
				this.validate(castedValue);
				return null;
			} catch (final ValidatorException exception) {
				if (this.throwException) {
					throw exception;
				}
				return exception;
			}
		}

		throw new IllegalStateException("Unable to find a suitable type for the value \"" + value + "\" with the validator " + this.getClass().getSimpleName() + ".");
	}

	/* [ Builder methods ] */
	public final @NonNull AValidator<T> annotations(final @NonNull Annotation @NonNull... annotations) {
		this.annotations = Arrays.asList(annotations);
		return this;
	}

	public final @NonNull AValidator<T> annotations(final @NonNull List<@NonNull Annotation> annotations) {
		this.annotations = annotations;
		return this;
	}

	public final @NonNull AValidator<T> throwException(final boolean throwException) {
		this.throwException = throwException;
		return this;
	}

	/* [ Abstract methods ] */
	protected final ValidatorException except(final @NonNull Supplier<Boolean> condition, final @NonNull ValidatorException exception) throws ValidatorException {
		if (!condition.get()) {
			throw exception;
		}
		return null;
	}

	protected final boolean has(final @NonNull Class<? extends Annotation> annotation) {
		return this.annotations.stream().anyMatch(annotation::isInstance);
	}

	protected final <S extends Annotation> S get(final @NonNull Class<S> annotation) {
		return this.annotations.stream().filter(annotation::isInstance).map(annotation::cast).findFirst().orElse(null);
	}

	protected final <S extends Annotation> void get(final @NonNull Class<S> annotation, final Consumer<S> consumer) {
		this.annotations.stream().filter(annotation::isInstance).map(annotation::cast).findFirst().ifPresent(consumer);
	}

	protected abstract void validate(final T value);

}