package br.com.bloodlink;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Mapa ilustrativo para a demonstração (sem chave de API).
 * Na versão real, seria substituído pelo Google Maps SDK com marcadores dos hemocentros.
 */
public class MapaView extends View {
    public interface Listener {
        void aoTocarBanco(BancoSangue banco);
    }

    private static final int RAIO_MAX_KM = 8;

    private final Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final RectF rect = new RectF();
    private final Path caminho = new Path();
    private List<BancoSangue> bancos = new ArrayList<>();
    private int raioKm = 5;
    private Listener listener;
    private final float dp;

    public MapaView(Context context, AttributeSet attrs) {
        super(context, attrs);
        dp = getResources().getDisplayMetrics().density;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void setBancos(List<BancoSangue> bancos, int raioKm) {
        this.bancos = bancos;
        this.raioKm = raioKm;
        invalidate();
    }

    private float pxPorKm() {
        return (Math.min(getWidth(), getHeight()) / 2f - 14 * dp) / RAIO_MAX_KM;
    }

    private PointF posicao(BancoSangue b) {
        double rad = Math.toRadians(b.anguloGraus);
        float d = (float) b.distanciaKm * pxPorKm();
        float x = getWidth() / 2f + (float) Math.cos(rad) * d;
        float y = getHeight() / 2f - (float) Math.sin(rad) * d;
        return new PointF(x, y);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float w = getWidth();
        float h = getHeight();
        float cx = w / 2f;
        float cy = h / 2f;

        canvas.drawColor(0xFFE6EEE3);

        // parque
        p.setStyle(Paint.Style.FILL);
        p.setColor(0xFFC8E6C9);
        rect.set(w * 0.66f, h * 0.60f, w * 0.94f, h * 0.92f);
        canvas.drawRoundRect(rect, 12 * dp, 12 * dp, p);

        // rio
        p.setStyle(Paint.Style.STROKE);
        p.setColor(0xFFBBDEFB);
        p.setStrokeWidth(16 * dp);
        caminho.reset();
        caminho.moveTo(0, h * 0.85f);
        caminho.quadTo(w * 0.35f, h * 0.55f, w * 0.6f, h * 0.95f);
        canvas.drawPath(caminho, p);

        // ruas
        p.setColor(0xFFFFFFFF);
        p.setStrokeWidth(9 * dp);
        canvas.drawLine(0, h * 0.30f, w, h * 0.30f, p);
        canvas.drawLine(0, h * 0.68f, w, h * 0.55f, p);
        canvas.drawLine(w * 0.25f, 0, w * 0.25f, h, p);
        canvas.drawLine(w * 0.62f, 0, w * 0.75f, h, p);
        p.setStrokeWidth(5 * dp);
        canvas.drawLine(w * 0.05f, 0, w * 0.45f, h, p);

        // raio de alcance das notificações
        p.setStyle(Paint.Style.FILL);
        p.setColor(0x22B71C1C);
        canvas.drawCircle(cx, cy, raioKm * pxPorKm(), p);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(2 * dp);
        p.setColor(0x88B71C1C);
        canvas.drawCircle(cx, cy, raioKm * pxPorKm(), p);

        // bancos de sangue
        p.setTextAlign(Paint.Align.CENTER);
        for (BancoSangue b : bancos) {
            PointF pt = posicao(b);
            p.setStyle(Paint.Style.FILL);
            p.setColor(Niveis.cor(b.menorNivel()));
            canvas.drawCircle(pt.x, pt.y, 17 * dp, p);
            p.setStyle(Paint.Style.STROKE);
            p.setStrokeWidth(3 * dp);
            p.setColor(0xFFFFFFFF);
            canvas.drawCircle(pt.x, pt.y, 17 * dp, p);
            p.setStyle(Paint.Style.FILL);
            p.setTextSize(24 * dp);
            p.setFakeBoldText(true);
            canvas.drawText("+", pt.x, pt.y + 8.5f * dp, p);
            p.setFakeBoldText(false);
        }

        // usuário
        p.setStyle(Paint.Style.FILL);
        p.setColor(0x552196F3);
        canvas.drawCircle(cx, cy, 16 * dp, p);
        p.setColor(0xFF2196F3);
        canvas.drawCircle(cx, cy, 8 * dp, p);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(3 * dp);
        p.setColor(0xFFFFFFFF);
        canvas.drawCircle(cx, cy, 8 * dp, p);

        // aviso
        p.setStyle(Paint.Style.FILL);
        p.setTextAlign(Paint.Align.LEFT);
        p.setTextSize(11 * dp);
        p.setColor(0xFF757575);
        canvas.drawText("Mapa ilustrativo (demo)", 8 * dp, h - 8 * dp, p);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if (e.getAction() == MotionEvent.ACTION_UP) {
            for (BancoSangue b : bancos) {
                PointF pt = posicao(b);
                if (Math.hypot(e.getX() - pt.x, e.getY() - pt.y) < 28 * dp) {
                    performClick();
                    if (listener != null) listener.aoTocarBanco(b);
                    return true;
                }
            }
        }
        return true;
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }
}
