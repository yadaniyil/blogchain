package com.yadaniil.blogchain

import com.yadaniil.blogchain.mining.fragments.calculator.CalculatorPresenter
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }
}

class HashrateExponentTest {

    @Test
    fun hashrateExponent_isCorrect() {
        val calculatorPresenter = CalculatorPresenter()
        // Bitcoin
        assertEquals("Gh/s", calculatorPresenter.calculateHashrateExponent("10620217936458838842"))

        // Bitcoin Cash
        assertEquals("Gh/s", calculatorPresenter.calculateHashrateExponent("201036385683691385"))

        // Ethereum
        assertEquals("Mh/s", calculatorPresenter.calculateHashrateExponent("108910624724623"))

        // Ethereum Classic
        assertEquals("Mh/s", calculatorPresenter.calculateHashrateExponent("6353843053688"))

        // LBRY
        assertEquals("Mh/s", calculatorPresenter.calculateHashrateExponent("4632120018452"))

        // Monero
        assertEquals("h/s", calculatorPresenter.calculateHashrateExponent("268421148"))

        // Hash
        assertEquals("h/s", calculatorPresenter.calculateHashrateExponent("3038935"))
    }
}
