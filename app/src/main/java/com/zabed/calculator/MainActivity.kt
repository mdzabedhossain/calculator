package com.zabed.calculator

import android.app.Activity
import android.os.Bundle
import android.graphics.Color
import android.view.Gravity
import android.widget.Button
import android.widget.GridLayout
import android.widget.LinearLayout
import android.widget.TextView
import java.util.Locale

class MainActivity : Activity() {
    private lateinit var display: TextView
    private var current = "0"
    private var stored: Double? = null
    private var pending: Char? = null
    private var resetOnNext = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        buildUi()
    }

    private fun buildUi() {
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(16, 24, 16, 16)
        }

        val title = TextView(this).apply {
            text = "Calculator"
            textSize = 24f
            setTextColor(Color.WHITE)
            setGravity(Gravity.CENTER_VERTICAL)
            setPadding(20, 0, 20, 0)
            setBackgroundColor(Color.rgb(49, 94, 251))
        }
        root.addView(title, LinearLayout.LayoutParams(-1, 64))

        display = TextView(this).apply {
            text = "0"
            textSize = 42f
            gravity = Gravity.CENTER_VERTICAL or Gravity.END
            setPadding(16, 16, 16, 16)
            setTextColor(Color.DKGRAY)
            setBackgroundColor(Color.rgb(245, 245, 245))
        }
        root.addView(display, LinearLayout.LayoutParams(-1, 0, 1f))

        val grid = GridLayout(this).apply {
            columnCount = 4
            rowCount = 5
        }

        val buttons = arrayOf(
            "C", "⌫", "%", "÷",
            "7", "8", "9", "×",
            "4", "5", "6", "−",
            "1", "2", "3", "+",
            "±", "0", ".", "="
        )

        for (label in buttons) {
            val b = Button(this).apply {
                text = label
                textSize = 22f
                setOnClickListener { press(label) }
            }
            val p = GridLayout.LayoutParams().apply {
                width = 0
                height = 0
                columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                setMargins(4, 4, 4, 4)
            }
            grid.addView(b, p)
        }

        root.addView(grid, LinearLayout.LayoutParams(-1, 0, 2f))
        setContentView(root)
    }

    private fun press(label: String) {
        when (label) {
            "C" -> { current = "0"; stored = null; pending = null; resetOnNext = false }
            "⌫" -> if (!resetOnNext) current = if (current.length > 1) current.dropLast(1) else "0"
            "." -> if (!resetOnNext && !current.contains(".")) current += "."
            "±" -> if (current != "0") current = if (current.startsWith("-")) current.drop(1) else "-$current"
            "%" -> current = format(parse() / 100.0)
            "+", "−", "×", "÷" -> chooseOperator(label[0])
            "=" -> calculate()
            else -> {
                if (resetOnNext) { current = label; resetOnNext = false }
                else current = if (current == "0") label else current + label
            }
        }
        display.text = current
    }

    private fun chooseOperator(op: Char) {
        val value = parse()
        if (stored != null && pending != null && !resetOnNext) {
            stored = apply(stored!!, value, pending!!)
        } else if (stored == null) {
            stored = value
        }
        pending = op
        resetOnNext = true
    }

    private fun calculate() {
        if (stored != null && pending != null) {
            current = format(apply(stored!!, parse(), pending!!))
            stored = null
            pending = null
            resetOnNext = true
        }
    }

    private fun apply(a: Double, b: Double, op: Char): Double = when (op) {
        '+' -> a + b
        '−' -> a - b
        '×' -> a * b
        '÷' -> if (b == 0.0) Double.NaN else a / b
        else -> b
    }

    private fun parse(): Double = current.toDoubleOrNull() ?: Double.NaN

    private fun format(value: Double): String {
        if (value.isNaN() || value.isInfinite()) return "Error"
        return if (value == value.toLong().toDouble()) value.toLong().toString()
        else String.format(Locale.US, "%.10f", value).trimEnd('0').trimEnd('.')
    }
}
