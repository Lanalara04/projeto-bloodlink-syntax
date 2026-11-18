package br.com.bloodlink;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/** Medidor semicircular (velocímetro) do nível de estoque de um tipo sanguíneo. */
public class GaugeView extends View {
    private final Paint fundo = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint arco = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint texto = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final RectF oval = new RectF();
    private int valor = 0;

    public GaugeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        fundo.setStyle(Paint.Style.STROKE);
        fundo.setStrokeCap(Paint.Cap.ROUND);
        fundo.setColor(0xFFE0E0E0);
        arco.setStyle(Paint.Style.STROKE);
        arco.setStrokeCap(Paint.Cap.ROUND);
        texto.setTextAlign(Paint.Align.CENTER);
        texto.setFakeBoldText(true);
        texto.setColor(0xFF212121);
    }

    public void setValor(int valor) {
        this.valor = Math.max(0, Math.min(100, valor));
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float w = getWidth();
        float h = getHeight();
        float stroke = Math.min(w, h * 2) * 0.13f;
        float cx = w / 2f;
        float cy = h - stroke;
        float r = Math.min(w / 2f - stroke / 2f, cy - stroke / 2f);
        oval.set(cx - r, cy - r, cx + r, cy + r);

        fundo.setStrokeWidth(stroke);
        arco.setStrokeWidth(stroke);
        arco.setColor(Niveis.cor(valor));

        canvas.drawArc(oval, 180f, 180f, false, fundo);
        if (valor > 0) {
            canvas.drawArc(oval, 180f, 180f * valor / 100f, false, arco);
        }
        texto.setTextSize(r * 0.55f);
        canvas.drawText(valor + "%", cx, cy - r * 0.05f, texto);
    }
}
