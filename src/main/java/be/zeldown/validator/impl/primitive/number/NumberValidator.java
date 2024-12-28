package be.zeldown.validator.impl.primitive.number;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Arrays;

import be.zeldown.validator.AValidator;
import be.zeldown.validator.exception.ValidatorException;
import lombok.NonNull;

public class NumberValidator extends AValidator<Number> {

	public NumberValidator() {
		super(Number.class, int.class, long.class, float.class, double.class, short.class, byte.class);
	}

	@Override
	protected void validate(final Number value) {
		super.get(Range.class, range -> super.except(() -> value.doubleValue() >= range.min() && value.doubleValue() <= range.max(), new NumberValidatorRangeException(this, range, value)));
		super.get(Equals.class, equals -> super.except(() -> Arrays.stream(equals.value()).anyMatch(v -> v == value.doubleValue()), new NumberValidatorEqualsException(this, equals, value)));
		super.get(Greater.class, greater -> super.except(() -> value.doubleValue() > greater.value(), new NumberValidatorGreaterException(this, greater, value)));
		super.get(GreaterOrEquals.class, greater -> super.except(() -> value.doubleValue() >= greater.value(), new NumberValidatorGreaterOrEqualsException(this, greater, value)));
		super.get(Lower.class, lower -> super.except(() -> value.doubleValue() < lower.value(), new NumberValidatorLowerException(this, lower, value)));
		super.get(LowerOrEquals.class, lower -> super.except(() -> value.doubleValue() <= lower.value(), new NumberValidatorLowerOrEqualsException(this, lower, value)));
		super.get(Modulo.class, modulo -> super.except(() -> value.doubleValue() % modulo.value() == 0, new NumberValidatorModuloException(this, modulo, value)));
		super.get(Negative.class, negative -> super.except(() -> value.doubleValue() < 0, new NumberValidatorNegativeException(this, negative, value)));
		super.get(Positive.class, positive -> super.except(() -> value.doubleValue() > 0, new NumberValidatorPositiveException(this, positive, value)));
		super.get(Zero.class, zero -> super.except(() -> value.doubleValue() == 0, new NumberValidatorZeroException(this, zero, value)));
		super.get(NotZero.class, notZero -> super.except(() -> value.doubleValue() != 0, new NumberValidatorNotZeroException(this, notZero, value)));
		super.get(Odd.class, odd -> super.except(() -> value.longValue() % 2 != 0, new NumberValidatorOddException(this, odd, value)));
		super.get(Even.class, even -> super.except(() -> value.longValue() % 2 == 0, new NumberValidatorEvenException(this, even, value)));
	}

	/* [ Annotations ] */
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD, ElementType.PARAMETER})
	public @interface Range {

		double min() default -Double.MAX_VALUE;
		double max() default Double.MAX_VALUE;

	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD, ElementType.PARAMETER})
	public @interface Equals {

		double[] value();

	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD, ElementType.PARAMETER})
	public @interface Greater {

		double value();

	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD, ElementType.PARAMETER})
	public @interface GreaterOrEquals {

		double value();

	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD, ElementType.PARAMETER})
	public @interface Lower {

		double value();

	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD, ElementType.PARAMETER})
	public @interface LowerOrEquals {

		double value();

	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD, ElementType.PARAMETER})
	public @interface Modulo {

		double value();

	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD, ElementType.PARAMETER})
	public @interface Negative {}

	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD, ElementType.PARAMETER})
	public @interface Positive {}

	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD, ElementType.PARAMETER})
	public @interface Zero {}

	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD, ElementType.PARAMETER})
	public @interface NotZero {}

	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD, ElementType.PARAMETER})
	public @interface Odd {}

	@Retention(RetentionPolicy.RUNTIME)
	@Target({ElementType.FIELD, ElementType.PARAMETER})
	public @interface Even {}

	/* [ Exceptions ] */
	public class NumberValidatorRangeException extends ValidatorException {

		public NumberValidatorRangeException(final @NonNull AValidator<?> validator, final @NonNull Annotation annotation, final Object value) {
			super(validator, annotation, value, "the target \"{target}\" must be between {min} and {max} but found {value}");
		}

		@Override
		public String format(final @NonNull String text) {
			final Range range = (Range) super.getAnnotation();
			return super.format(text).replace("{min}", String.valueOf(range.min())).replace("{max}", String.valueOf(range.max()));
		}

	}

	public class NumberValidatorEqualsException extends ValidatorException {

		public NumberValidatorEqualsException(final @NonNull AValidator<?> validator, final @NonNull Annotation annotation, final Object value) {
			super(validator, annotation, value, "the target \"{target}\" must be equals to {values} but found {value}");
		}

		@Override
		public String format(final @NonNull String text) {
			final Equals equals = (Equals) super.getAnnotation();
			return super.format(text).replace("{values}", Arrays.toString(equals.value()));
		}

	}

	public class NumberValidatorGreaterException extends ValidatorException {

		public NumberValidatorGreaterException(final @NonNull AValidator<?> validator, final @NonNull Annotation annotation, final Object value) {
			super(validator, annotation, value, "the target \"{target}\" must be greater than {greater} but found {value}");
		}

		@Override
		public String format(final @NonNull String text) {
			final Greater greater = (Greater) super.getAnnotation();
			return super.format(text).replace("{greater}", String.valueOf(greater.value()));
		}

	}

	public class NumberValidatorGreaterOrEqualsException extends ValidatorException {

		public NumberValidatorGreaterOrEqualsException(final @NonNull AValidator<?> validator, final @NonNull Annotation annotation, final Object value) {
			super(validator, annotation, value,"the target \"{target}\" must be greater or equals to {greater} but found {value}");
		}

		@Override
		public String format(final @NonNull String text) {
			final GreaterOrEquals greater = (GreaterOrEquals) super.getAnnotation();
			return super.format(text).replace("{greater}", String.valueOf(greater.value()));
		}

	}

	public class NumberValidatorLowerException extends ValidatorException {

		public NumberValidatorLowerException(final @NonNull AValidator<?> validator, final @NonNull Annotation annotation, final Object value) {
			super(validator, annotation, value, "the target \"{target}\" must be lower than {lower} but found {value}");
		}

		@Override
		public String format(final @NonNull String text) {
			final Lower lower = (Lower) super.getAnnotation();
			return super.format(text).replace("{lower}", String.valueOf(lower.value()));
		}

	}

	public class NumberValidatorLowerOrEqualsException extends ValidatorException {

		public NumberValidatorLowerOrEqualsException(final @NonNull AValidator<?> validator, final @NonNull Annotation annotation, final Object value) {
			super(validator, annotation, value, "the target \"{target}\" must be lower or equals to {lower} but found {value}");
		}

		@Override
		public String format(final @NonNull String text) {
			final LowerOrEquals lower = (LowerOrEquals) super.getAnnotation();
			return super.format(text).replace("{lower}", String.valueOf(lower.value()));
		}

	}

	public class NumberValidatorModuloException extends ValidatorException {

		public NumberValidatorModuloException(final @NonNull AValidator<?> validator, final @NonNull Annotation annotation, final Object value) {
			super(validator, annotation, value, "the target \"{target}\" must be a multiple of {modulo} but found {value}");
		}

		@Override
		public String format(final @NonNull String text) {
			final Modulo modulo = (Modulo) super.getAnnotation();
			return super.format(text).replace("{modulo}", String.valueOf(modulo.value()));
		}

	}

	public class NumberValidatorNegativeException extends ValidatorException {

		public NumberValidatorNegativeException(final @NonNull AValidator<?> validator, final @NonNull Annotation annotation, final Object value) {
			super(validator, annotation, value, "the target \"{target}\" must be negative but found {value}");
		}

	}

	public class NumberValidatorPositiveException extends ValidatorException {

		public NumberValidatorPositiveException(final @NonNull AValidator<?> validator, final @NonNull Annotation annotation, final Object value) {
			super(validator, annotation, value, "the target \"{target}\" must be positive but found {value}");
		}

	}

	public class NumberValidatorZeroException extends ValidatorException {

		public NumberValidatorZeroException(final @NonNull AValidator<?> validator, final @NonNull Annotation annotation, final Object value) {
			super(validator, annotation, value, "the target \"{target}\" must be zero but found {value}");
		}

	}

	public class NumberValidatorNotZeroException extends ValidatorException {

		public NumberValidatorNotZeroException(final @NonNull AValidator<?> validator, final @NonNull Annotation annotation, final Object value) {
			super(validator, annotation, value, "the target \"{target}\" must not be zero but found {value}");
		}

	}

	public class NumberValidatorOddException extends ValidatorException {

		public NumberValidatorOddException(final @NonNull AValidator<?> validator, final @NonNull Annotation annotation, final Object value) {
			super(validator, annotation, value, "the target \"{target}\" must be odd but found {value}");
		}

	}

	public class NumberValidatorEvenException extends ValidatorException {

		public NumberValidatorEvenException(final @NonNull AValidator<?> validator, final @NonNull Annotation annotation, final Object value) {
			super(validator, annotation, value, "the target \"{target}\" must be even but found {value}");
		}

	}

}