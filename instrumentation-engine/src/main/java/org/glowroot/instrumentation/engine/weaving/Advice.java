/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.glowroot.instrumentation.engine.weaving;

import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Ordering;
import com.google.common.collect.Sets;
import com.google.common.primitives.Ints;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.immutables.value.Value;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.Method;

import org.glowroot.instrumentation.api.weaving.Advice.Pointcut;
import org.glowroot.instrumentation.engine.weaving.ClassLoaders.LazyDefinedClass;

@Value.Immutable
abstract class Advice {

    static final Ordering<Advice> ordering = new AdviceOrdering();

    abstract Pointcut pointcut();

    abstract Type adviceType();

    abstract @Nullable Pattern pointcutClassNamePattern();

    abstract @Nullable Pattern pointcutClassAnnotationPattern();

    abstract @Nullable Pattern pointcutSubTypeRestrictionPattern();

    abstract @Nullable Pattern pointcutSuperTypeRestrictionPattern();

    abstract @Nullable Pattern pointcutMethodNamePattern();

    abstract @Nullable Pattern pointcutMethodAnnotationPattern();

    abstract List<Object> pointcutMethodParameterTypes(); // items can be either String or Pattern

    abstract @Nullable Type travelerType();

    abstract @Nullable Method isEnabledAdvice();

    abstract @Nullable Method onBeforeAdvice();

    abstract @Nullable Method onReturnAdvice();

    abstract @Nullable Method onThrowAdvice();

    abstract @Nullable Method onAfterAdvice();

    abstract ImmutableList<AdviceParameter> isEnabledParameters();

    abstract ImmutableList<AdviceParameter> onBeforeParameters();

    abstract ImmutableList<AdviceParameter> onReturnParameters();

    abstract ImmutableList<AdviceParameter> onThrowParameters();

    abstract ImmutableList<AdviceParameter> onAfterParameters();

    abstract boolean hasBindThreadContext();

    abstract boolean hasBindOptionalThreadContext();

    abstract boolean reweavable();

    abstract @Nullable Advice nonBootstrapLoaderAdvice();

    abstract @Nullable LazyDefinedClass nonBootstrapLoaderAdviceClass();

    @Value.Derived
    ImmutableSet<Type> classMetaTypes() {
        Set<Type> metaTypes = Sets.newHashSet();
        metaTypes.addAll(getClassMetaTypes(isEnabledParameters()));
        metaTypes.addAll(getClassMetaTypes(onBeforeParameters()));
        metaTypes.addAll(getClassMetaTypes(onReturnParameters()));
        metaTypes.addAll(getClassMetaTypes(onThrowParameters()));
        metaTypes.addAll(getClassMetaTypes(onAfterParameters()));
        return ImmutableSet.copyOf(metaTypes);
    }

    @Value.Derived
    ImmutableSet<Type> methodMetaTypes() {
        Set<Type> metaTypes = Sets.newHashSet();
        metaTypes.addAll(getMethodMetaTypes(isEnabledParameters()));
        metaTypes.addAll(getMethodMetaTypes(onBeforeParameters()));
        metaTypes.addAll(getMethodMetaTypes(onReturnParameters()));
        metaTypes.addAll(getMethodMetaTypes(onThrowParameters()));
        metaTypes.addAll(getMethodMetaTypes(onAfterParameters()));
        return ImmutableSet.copyOf(metaTypes);
    }

    private static Set<Type> getClassMetaTypes(List<AdviceParameter> parameters) {
        Set<Type> types = Sets.newHashSet();
        for (AdviceParameter parameter : parameters) {
            if (parameter.kind() == ParameterKind.CLASS_META) {
                types.add(parameter.type());
            }
        }
        return types;
    }

    private static Set<Type> getMethodMetaTypes(List<AdviceParameter> parameters) {
        Set<Type> types = Sets.newHashSet();
        for (AdviceParameter parameter : parameters) {
            if (parameter.kind() == ParameterKind.METHOD_META) {
                types.add(parameter.type());
            }
        }
        return types;
    }

    enum ParameterKind {
        RECEIVER, METHOD_ARG, METHOD_ARG_ARRAY, METHOD_NAME, RETURN, OPTIONAL_RETURN, THROWABLE,
        TRAVELER, CLASS_META, METHOD_META, THREAD_CONTEXT, OPTIONAL_THREAD_CONTEXT, SPECIAL
    }

    private static class AdviceOrdering extends Ordering<Advice> {

        @Override
        public int compare(Advice left, Advice right) {
            // FIXME
            // empty timer names used to be placed at the end (this applied to "Optional transaction
            // overrides only" which was good so that those can update things after transaction is
            // started in advice for the same method)
            return Ints.compare(left.pointcut().order(), right.pointcut().order());
        }
    }

    @Value.Immutable
    interface AdviceParameter {

        ParameterKind kind();

        Type type();

        int argIndex();
    }
}
