package scientifik.kmath.operations

@DslMarker
annotation class KMathContext

/**
 * Marker interface for any algebra
 */
interface Algebra<T> {
    fun unaryOperation(operation: String, arg: T): T
    fun binaryOperation(operation: String, left: T, right: T): T
}

inline operator fun <A : Algebra<*>, R> A.invoke(block: A.() -> R): R = run(block)

/**
 * Space-like operations without neutral element
 */
interface SpaceOperations<T> : Algebra<T> {
    /**
     * Addition operation for two context elements
     */
    fun add(a: T, b: T): T

    /**
     * Multiplication operation for context element and real number
     */
    fun multiply(a: T, k: Number): T

    //Operation to be performed in this context. Could be moved to extensions in case of KEEP-176
    operator fun T.unaryMinus(): T = multiply(this, -1.0)

    operator fun T.plus(b: T): T = add(this, b)
    operator fun T.minus(b: T): T = add(this, -b)
    operator fun T.times(k: Number) = multiply(this, k.toDouble())
    operator fun T.div(k: Number) = multiply(this, 1.0 / k.toDouble())
    operator fun Number.times(b: T) = b * this

    override fun unaryOperation(operation: String, arg: T): T = when (operation) {
        PLUS_OPERATION -> arg
        MINUS_OPERATION -> -arg
        else -> error("Unary operation $operation not defined in $this")
    }

    override fun binaryOperation(operation: String, left: T, right: T): T = when (operation) {
        PLUS_OPERATION -> add(left, right)
        MINUS_OPERATION -> left - right
        else -> error("Binary operation $operation not defined in $this")
    }

    companion object {
        const val PLUS_OPERATION = "+"
        const val MINUS_OPERATION = "-"
        const val NOT_OPERATION = "!"
    }
}


/**
 * A general interface representing linear context of some kind.
 * The context defines sum operation for its elements and multiplication by real value.
 * One must note that in some cases context is a singleton class, but in some cases it
 * works as a context for operations inside it.
 *
 * TODO do we need non-commutative context?
 */
interface Space<T> : SpaceOperations<T> {
    /**
     * Neutral element for sum operation
     */
    val zero: T
}

/**
 * Operations on ring without multiplication neutral element
 */
interface RingOperations<T> : SpaceOperations<T> {
    /**
     * Multiplication for two field elements
     */
    fun multiply(a: T, b: T): T

    operator fun T.times(b: T): T = multiply(this, b)

    override fun binaryOperation(operation: String, left: T, right: T): T = when (operation) {
        TIMES_OPERATION -> multiply(left, right)
        else -> super.binaryOperation(operation, left, right)
    }

    companion object{
        const val TIMES_OPERATION = "*"
    }
}

/**
 * The same as {@link Space} but with additional multiplication operation
 */
interface Ring<T> : Space<T>, RingOperations<T> {
    /**
     * neutral operation for multiplication
     */
    val one: T

//    operator fun T.plus(b: Number) = this.plus(b * one)
//    operator fun Number.plus(b: T) = b + this
//
//    operator fun T.minus(b: Number) = this.minus(b * one)
//    operator fun Number.minus(b: T) = -b + this
}

/**
 * All ring operations but without neutral elements
 */
interface FieldOperations<T> : RingOperations<T> {
    fun divide(a: T, b: T): T

    operator fun T.div(b: T): T = divide(this, b)

    override fun binaryOperation(operation: String, left: T, right: T): T = when (operation) {
        DIV_OPERATION -> divide(left, right)
        else -> super.binaryOperation(operation, left, right)
    }

    companion object{
        const val DIV_OPERATION = "/"
    }
}

/**
 * Four operations algebra
 */
interface Field<T> : Ring<T>, FieldOperations<T> {
    operator fun Number.div(b: T) = this * divide(one, b)
}
