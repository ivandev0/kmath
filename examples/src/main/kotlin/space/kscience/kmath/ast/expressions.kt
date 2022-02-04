/*
 * Copyright 2018-2021 KMath contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package space.kscience.kmath.ast

import space.kscience.kmath.asm.compileToExpression
import space.kscience.kmath.expressions.invoke
import space.kscience.kmath.operations.Algebra
import space.kscience.kmath.operations.DoubleField

fun main() {
    val expr = "x+2".parseMath().compileToExpression(DoubleField as Algebra<Double>)
    expr("x" to 42.0)
}
