// Generated by delombok at Sun Feb 26 12:31:38 KST 2017
package scouter.bytebuddy.matcher;

import scouter.bytebuddy.description.method.MethodDescription;
import scouter.bytebuddy.description.method.MethodList;
import scouter.bytebuddy.description.type.TypeDefinition;

/**
 * An element matcher that checks if a type description declares methods of a given property.
 *
 * @param <T> The exact type of the annotated element that is matched.
 */
public class DeclaringMethodMatcher<T extends TypeDefinition> extends ElementMatcher.Junction.AbstractBase<T> {
    /**
     * The field matcher to apply to the declared fields of the matched type description.
     */
    private final ElementMatcher<? super MethodList<?>> matcher;

    /**
     * Creates a new matcher for a type's declared methods.
     *
     * @param matcher The method matcher to apply to the declared methods of the matched type description.
     */
    public DeclaringMethodMatcher(ElementMatcher<? super MethodList<? extends MethodDescription>> matcher) {
        this.matcher = matcher;
    }

    @Override
    public boolean matches(T target) {
        return matcher.matches(target.getDeclaredMethods());
    }

    @Override
    public String toString() {
        return "declaresMethods(" + matcher + ")";
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    @javax.annotation.Generated("lombok")
    public boolean equals(final java.lang.Object o) {
        if (o == this) return true;
        if (!(o instanceof DeclaringMethodMatcher)) return false;
        final DeclaringMethodMatcher<?> other = (DeclaringMethodMatcher<?>) o;
        if (!other.canEqual((java.lang.Object) this)) return false;
        final java.lang.Object this$matcher = this.matcher;
        final java.lang.Object other$matcher = other.matcher;
        if (this$matcher == null ? other$matcher != null : !this$matcher.equals(other$matcher)) return false;
        return true;
    }

    @java.lang.SuppressWarnings("all")
    @javax.annotation.Generated("lombok")
    protected boolean canEqual(final java.lang.Object other) {
        return other instanceof DeclaringMethodMatcher;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("all")
    @javax.annotation.Generated("lombok")
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final java.lang.Object $matcher = this.matcher;
        result = result * PRIME + ($matcher == null ? 43 : $matcher.hashCode());
        return result;
    }
}