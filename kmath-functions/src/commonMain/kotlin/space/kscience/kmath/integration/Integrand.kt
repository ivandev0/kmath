/*
 * Copyright 2018-2021 KMath contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package space.kscience.kmath.integration

import kotlin.reflect.KClass

public interface IntegrandFeature {
    override fun toString(): String
}

public interface Integrand {
    public val features: Set<IntegrandFeature>
    public fun <T : IntegrandFeature> getFeature(type: KClass<T>): T?
}

public inline fun <reified T : IntegrandFeature> Integrand.getFeature(): T? = getFeature(T::class)

public class IntegrandValue<out T : Any>(public val value: T) : IntegrandFeature {
    override fun toString(): String = "Value($value)"
}

public class IntegrandRelativeAccuracy(public val accuracy: Double) : IntegrandFeature {
    override fun toString(): String = "TargetRelativeAccuracy($accuracy)"
}

public class IntegrandAbsoluteAccuracy(public val accuracy: Double) : IntegrandFeature {
    override fun toString(): String = "TargetAbsoluteAccuracy($accuracy)"
}

public class IntegrandCallsPerformed(public val calls: Int) : IntegrandFeature {
    override fun toString(): String = "Calls($calls)"
}

public val Integrand.calls: Int get() = getFeature<IntegrandCallsPerformed>()?.calls ?: 0

public class IntegrandMaxCalls(public val maxCalls: Int) : IntegrandFeature {
    override fun toString(): String = "MaxCalls($maxCalls)"
}

public class IntegrandIterationsRange(public val range: IntRange) : IntegrandFeature {
    override fun toString(): String = "Iterations(${range.first}..${range.last})"
}
