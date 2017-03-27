// Generated by delombok at Sun Feb 26 12:31:38 KST 2017
package scouter.bytebuddy.implementation;

import scouter.bytebuddy.description.method.MethodDescription;
import scouter.bytebuddy.dynamic.scaffold.InstrumentedType;
import scouter.bytebuddy.implementation.bytecode.ByteCodeAppender;
import scouter.bytebuddy.implementation.bytecode.Removal;
import scouter.bytebuddy.implementation.bytecode.StackManipulation;
import scouter.bytebuddy.implementation.bytecode.member.MethodReturn;
import scouter.bytebuddy.implementation.bytecode.member.MethodVariableAccess;
import scouter.bytebuddy.jar.asm.MethodVisitor;

/**
 * This implementation will create a new method which simply calls its super method. If no such method is defined,
 * an exception will be thrown. Constructors are considered to have a super method if the direct super class defines
 * a constructor with an identical signature. Default methods are invoked as such if they are non-ambiguous. Static
 * methods can have a (pseudo) super method if a type that defines such a method is rebased. Rebased types can also
 * shadow constructors or methods of an actual super class. Besides implementing constructors, this implementation
 * is useful when a method of a super type is not supposed to be altered but should be equipped with additional
 * annotations. Furthermore, this implementation allows to hard code a super method call to be performed after
 * performing another {@link Implementation}.
 */
public enum SuperMethodCall implements Implementation.Composable {
    /**
     * The singleton instance.
     */
    INSTANCE;

    @Override
    public InstrumentedType prepare(InstrumentedType instrumentedType) {
        return instrumentedType;
    }

    @Override
    public ByteCodeAppender appender(Target implementationTarget) {
        return new Appender(implementationTarget, Appender.TerminationHandler.RETURNING);
    }

    @Override
    public Implementation andThen(Implementation implementation) {
        return new Compound(WithoutReturn.INSTANCE, implementation);
    }


    /**
     * A super method invocation where the return value is dropped instead of returning from the method.
     */
    protected enum WithoutReturn implements Implementation {
        /**
         * The singleton instance.
         */
        INSTANCE;

        @Override
        public InstrumentedType prepare(InstrumentedType instrumentedType) {
            return instrumentedType;
        }

        @Override
        public ByteCodeAppender appender(Target implementationTarget) {
            return new Appender(implementationTarget, Appender.TerminationHandler.DROPPING);
        }
    }


    /**
     * An appender for implementing a {@link SuperMethodCall}.
     */
    protected static class Appender implements ByteCodeAppender {
        /**
         * The target of the current implementation.
         */
        private final Target implementationTarget;
        /**
         * The termination handler to apply after invoking the super method.
         */
        private final TerminationHandler terminationHandler;

        /**
         * Creates a new appender.
         *
         * @param implementationTarget The implementation target of the current type creation.
         * @param terminationHandler   The termination handler to apply after invoking the super method.
         */
        protected Appender(Target implementationTarget, TerminationHandler terminationHandler) {
            this.implementationTarget = implementationTarget;
            this.terminationHandler = terminationHandler;
        }

        @Override
        public Size apply(MethodVisitor methodVisitor, Implementation.Context implementationContext, MethodDescription instrumentedMethod) {
            StackManipulation superMethodCall = implementationTarget.invokeDominant(instrumentedMethod.asSignatureToken());
            if (!superMethodCall.isValid()) {
                throw new IllegalStateException("Cannot call super (or default) method for " + instrumentedMethod);
            }
            StackManipulation.Size stackSize = new StackManipulation.Compound(MethodVariableAccess.allArgumentsOf(instrumentedMethod).prependThisReference(), superMethodCall, terminationHandler.of(instrumentedMethod)).apply(methodVisitor, implementationContext);
            return new Size(stackSize.getMaximalSize(), instrumentedMethod.getStackSize());
        }

        /**
         * A handler that determines how to handle the method return value.
         */
        protected enum TerminationHandler {
            /**
             * A termination handler that returns the value of the super method invocation.
             */
            RETURNING {
                @Override
                protected StackManipulation of(MethodDescription methodDescription) {
                    return MethodReturn.of(methodDescription.getReturnType());
                }
            },
            
            /**
             * A termination handler that simply pops the value of the super method invocation off the stack.
             */
            DROPPING {
                @Override
                protected StackManipulation of(MethodDescription methodDescription) {
                    return Removal.of(methodDescription.getReturnType());
                }
            };

            /**
             * Creates a stack manipulation that represents this handler's behavior.
             *
             * @param methodDescription The method for which this handler is supposed to create a stack
             *                          manipulation for.
             * @return The stack manipulation that implements this handler.
             */
            protected abstract StackManipulation of(MethodDescription methodDescription);
        }

        @java.lang.Override
        @java.lang.SuppressWarnings("all")
        @javax.annotation.Generated("lombok")
        public boolean equals(final java.lang.Object o) {
            if (o == this) return true;
            if (!(o instanceof SuperMethodCall.Appender)) return false;
            final SuperMethodCall.Appender other = (SuperMethodCall.Appender) o;
            if (!other.canEqual((java.lang.Object) this)) return false;
            final java.lang.Object this$implementationTarget = this.implementationTarget;
            final java.lang.Object other$implementationTarget = other.implementationTarget;
            if (this$implementationTarget == null ? other$implementationTarget != null : !this$implementationTarget.equals(other$implementationTarget)) return false;
            final java.lang.Object this$terminationHandler = this.terminationHandler;
            final java.lang.Object other$terminationHandler = other.terminationHandler;
            if (this$terminationHandler == null ? other$terminationHandler != null : !this$terminationHandler.equals(other$terminationHandler)) return false;
            return true;
        }

        @java.lang.SuppressWarnings("all")
        @javax.annotation.Generated("lombok")
        protected boolean canEqual(final java.lang.Object other) {
            return other instanceof SuperMethodCall.Appender;
        }

        @java.lang.Override
        @java.lang.SuppressWarnings("all")
        @javax.annotation.Generated("lombok")
        public int hashCode() {
            final int PRIME = 59;
            int result = 1;
            final java.lang.Object $implementationTarget = this.implementationTarget;
            result = result * PRIME + ($implementationTarget == null ? 43 : $implementationTarget.hashCode());
            final java.lang.Object $terminationHandler = this.terminationHandler;
            result = result * PRIME + ($terminationHandler == null ? 43 : $terminationHandler.hashCode());
            return result;
        }
    }
}
