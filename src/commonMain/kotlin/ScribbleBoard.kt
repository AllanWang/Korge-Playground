import com.soywiz.korev.Key
import com.soywiz.korge.input.keys
import com.soywiz.korge.input.mouse
import com.soywiz.korge.view.Container
import com.soywiz.korge.view.Graphics
import com.soywiz.korge.view.graphics
import com.soywiz.korim.color.Colors
import com.soywiz.korim.color.RGBA
import com.soywiz.korim.vector.Context2d
import com.soywiz.korma.geom.Point
import com.soywiz.korma.geom.PointArrayList
import com.soywiz.korma.geom.vector.LineCap
import com.soywiz.korma.geom.vector.LineJoin
import com.soywiz.korma.geom.vector.rect

private data class Scribble(val color: RGBA, val thickness: Double, val points: PointArrayList)

private data class Scribbles(val lines: MutableList<Scribble> = mutableListOf()) {

    var color: RGBA = Colors.BLACK

    var size: Double = 3.0

    fun down(point: Point) {
        lines.add(Scribble(color = color, thickness = size, points = PointArrayList(point)))
    }

    fun add(point: Point) {
        lines.last().points.add(point)
    }

    fun undo() {
        if (lines.isNotEmpty()) lines.removeAt(lines.lastIndex)
    }

    fun clear() {
        lines.clear()
    }
}

private val scribble = Scribbles()

private fun Graphics.drawScribbles() = dirty {
    clear()
    // TODO add variable canvas dimensions
    fill(Colors.WHITE) {
        rect(0.0, 0.0, 512.0, 512.0)
    }

    fun Scribble.draw() {
        stroke(color, info = Context2d.StrokeInfo(
                thickness = thickness,
                startCap = LineCap.ROUND,
                endCap = LineCap.ROUND,
                lineJoin = LineJoin.ROUND)
        ) {
            var first = true
            points.fastForEach { x, y ->
                if (first) {
                    moveTo(x, y)
                    first = false
                } else {
                    lineTo(x, y)
                }
            }
        }
    }

    scribble.lines.forEach { it.draw() }
}

fun Container.scribbleGraphics() = graphics {
    drawScribbles()
    println("Load graphics $width $height")

    keys {
        // TODO for some reason TYPED doesn't work; expected TYPED to mean down + up
        up(Key.BACKSPACE) {
            println("Undo")
            scribble.undo()
            drawScribbles()
        }
    }

    mouse {
        var down = false
        down {
            println("Down ${it.currentPosLocal}")
            down = true
            scribble.down(it.currentPosLocal)
        }
        moveAnywhere {
            if (down) {
                scribble.add(it.currentPosLocal)
                drawScribbles()
            }
        }
        upAnywhere {
            if (down) {
                down = false
                scribble.add(it.currentPosLocal)
                drawScribbles()
            }
        }
    }
}
