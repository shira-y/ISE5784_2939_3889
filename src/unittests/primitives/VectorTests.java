/**
 * 
 */
package unittests.primitives;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import primitives.Vector;
import static primitives.Util.isZero;

/**
 * The VectorTests class contains JUnit tests for the methods of the Vector
 * class.
 */
class VectorTests {
	/**
	 * Define vector for testing
	 */
	private static final Vector v1 = new Vector(1, 2, 3);
	/**
	 * Define vector for testing
	 */
	private static final Vector v2 = new Vector(-2, -4, -6);
	/**
	 * Define vector for testing
	 */
	private static final Vector v3 = new Vector(0, 3, -2);
	/**
	 * The allowed difference between 2 values
	 */
	private final double DELTA = 0.000001;

	/**
	 * Test method for {@link primitives.Vector#add(primitives.Vector)}.
	 */
	@Test
	void testAddVector() {
		// ============ Equivalence Partitions Tests ==============
		// TC01: Test that checks if the operation of connecting vectors is correct
		Vector v4 = new Vector(1, 1, 1);
		assertEquals(v1, v4.add(new Vector(0, 1, 2)), "ERROR: AddVector() wrong value");

		// =============== Boundary Values Tests ==================
		// TC11: A test that checks if the addition of 2 vectors is equal to the zero
		// vector
		assertThrows(IllegalArgumentException.class, () -> v1.add(new Vector(-1, -2, -3)),
				"Vector + -itself does not throw an exception");
	}

	/**
	 * Test method for {@link primitives.Vector#scale(float)}.
	 */
	@Test
	void testScale() {

		// ============ Equivalence Partitions Tests ==============
		// TC01:A test that checks whether the multiplication of a vector by a scalar is
		// correct
		assertEquals(v2, v1.scale(-2), "ERROR: testScale() wrong value");

		// =============== Boundary Values Tests ==================
		// TC11:A test that checks the multiplication of a vector by a zero scalar
		assertThrows(IllegalArgumentException.class, () -> v1.scale(0), "ERROR: Scale by 0 must throw exception");
	}

	/**
	 * Test method for {@link primitives.Vector#dotProduct(primitives.Vector)}.
	 */
	@Test
	void testDotProduct() {
		// ============ Equivalence Partitions Tests ==============
		// TC02:The test checks whether the scalar product is correct
		assertEquals(-28, v1.dotProduct(v2), "ERROR: dotProduct() wrong value");

		// =============== Boundary Values Tests ==================
		// TC11:A test that checks whether a scalar product between 2 perpendicular
		// vectors is equal to zero
		assertEquals(0, v1.dotProduct(v3), DELTA, "ERROR: dotProduct() for orthogonal vectors is not zero");
	}

	/**
	 * Test method for {@link primitives.Vector#crossProduct(primitives.Vector)}.
	 */
	@Test
	void testCrossProduct() {
		// ============ Equivalence Partitions Tests ==============
		Vector vr = v1.crossProduct(v3);
		// TC01: Test that length of cross-product is proper (orthogonal vectors taken
		// for simplicity)
		assertEquals(vr.length(), v1.length() * v3.length(), DELTA, "ERROR: crossProduct() wrong result length");
		// TC02: Test cross-product result orthogonality to its operands
		assertEquals(0, vr.dotProduct(v1), DELTA, "crossProduct() result is not orthogonal to 1st operand");
		assertEquals(0, vr.dotProduct(v3), DELTA, "crossProduct() result is not orthogonal to 2nd operand");
		// =============== Boundary Values Tests ==================
		// TC11: test zero vector from cross-product of parallel vectors
		assertThrows(IllegalArgumentException.class, () -> v1.crossProduct(v2), //
				"crossProduct() for parallel vectors does not throw an exception");
	}

	/**
	 * Test method for {@link primitives.Vector#lengthSquared()}.
	 */
	@Test
	void testLengthSquared() {
		// ============ Equivalence Partitions Tests ==============
		// TC01:A test that checks that the squared length of the vector is correct
		assertEquals(14, v1.lengthSquared(), DELTA, "ERROR: lengthSquared() wrong value");
	}

	/**
	 * Test method for {@link primitives.Vector#length()}.
	 */
	@Test
	void testLength() {
		// ============ Equivalence Partitions Tests ==============
		// TC01:A test that checks that the length of the vector is correct
		Vector v1 = new Vector(0, 3, 4);
		assertEquals(5, v1.length(), DELTA, "ERROR: length() wrong value");
	}

	/**
	 * Test method for {@link primitives.Vector#normalize()}.
	 */
	@Test
	void testNormalize() {
		// ============ Equivalence Partitions Tests ==============
		// TC01:A test that checks that the vector is normalized correctly
		// Check that the length of the normalized vector is equal to 1.
		Vector u = v1.normalize();
		assertEquals(1, u.length(), DELTA, "ERROR: the normalized vector is not a unit vector");
		// check that the original and the normalized vectors are co-lined
		Vector zeroVector = new Vector(0, 0, 0);
		assertEquals(zeroVector, v1.crossProduct(u),
				"ERROR: the normalized vector is not parallel to the original one");
	}

	/**
	 * Test method for {@link primitives.Vector#subtract(primitives.Vector)}.
	 */

	@Test
	void testSubtract() {

		// ============ Equivalence Partitions Tests ==============
		// TC01: Test that checks if the operation of subtracting vectors is correct
		assertEquals(new Vector(3, 6, 9), v1.subtract(v2), "ERROR: Vector - Vector does not work correctly");
		// =============== Boundary Values Tests ==================
		// TC01 Test Vector - Itself = Zero Vector
		assertThrows(IllegalArgumentException.class, () -> v1.subtract(new Vector(1, 2, 3)),
				"ERROR: does not throw exception for subtracting a vector from itself");
	}

}
