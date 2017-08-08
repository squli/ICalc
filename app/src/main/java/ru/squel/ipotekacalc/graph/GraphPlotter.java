package ru.squel.ipotekacalc.graph;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ru.squel.ipotekacalc.data.MonthlyData;

/**
 * Created by sq on 01.08.2017.
 * Для отображения графика внутри фрагмента
 */
public class GraphPlotter extends View {

    public static final String LOG_TAG = GraphPlotter.class.getSimpleName();

    /// данные для построителя графика
    private ArrayList<MonthlyData> data;

    private Bitmap bitmap; // Область рисования для вывода или сохранения
    private Canvas bitmapCanvas; // Используется для рисования на Bitmap
    private final Paint paintScreen; // Используется для вывода Bitmap на экран
    private final Paint paintLine; // Используется для рисования линий на Bitmap

    /// здесь три кривые - всего, за проценты, за долг
    private final Map<String, Path> pathMap = new HashMap<>();
    /// здесь точки
    private final ArrayList<Point> percentDots = new ArrayList<>();
    private final ArrayList<Point> debtDots = new ArrayList<>();
    private final ArrayList<Point> payDots = new ArrayList<>();

    private int sizeX = 0;
    private int sizeY = 0;

    public GraphPlotter(Context context, AttributeSet attrs) {
        super(context, attrs);
        paintScreen = new Paint();
        paintLine = new Paint();
        paintLine.setAntiAlias(true);           //сглаживание коаев
        paintLine.setColor(Color.BLACK);        //по умолчанию черный цвет
        paintLine.setStyle(Paint.Style.STROKE); // сплошная линия
        paintLine.setStrokeWidth(5);            // толщина линиии по умолчанию
        paintLine.setStrokeCap(Paint.Cap.ROUND); // Закругленные концы
    }

    /**
     * Усиновка данных для графика
     * @param data
     */
    public void setData(ArrayList<MonthlyData> data) {
        this.data = data;
    }

    // Создание объектов Bitmap и Canvas на основании размеров View
    @Override
    public void onSizeChanged(int w, int h, int oldW, int oldH) {

        this.sizeX = getWidth();
        this.sizeY = getHeight();
        bitmap = Bitmap.createBitmap(getWidth(), getHeight(),
                Bitmap.Config.ARGB_8888);
        bitmapCanvas = new Canvas(bitmap);
        bitmap.eraseColor(Color.WHITE); // Bitmap стирается белым цветом
    }

    // Стирание рисунка
    public void clear() {
        bitmap.eraseColor(Color.WHITE); // Очистка изображения
        invalidate(); // Перерисовать изображение
    }

    // Перерисовка при обновлении на экране
    @Override
    protected void onDraw(Canvas canvas) {
        // Перерисовка фона
        canvas.drawBitmap(bitmap, 0, 0, paintScreen);

        // Для каждой выводимой линии
        Path path = new Path();
        // отображение рамки
        RectF rectf = new RectF(0,0,sizeX,sizeY);
        path.addRect(rectf, Path.Direction.CW);
        paintLine.setColor(Color.BLACK);
        canvas.drawPath(path, paintLine);

        calculateDots();
        drawLegend(canvas);

        paintLine.setColor(Color.GREEN);
        for (Point p : payDots) {
            canvas.drawPoint(p.x, p.y, paintLine);
        }

        paintLine.setColor(Color.RED);
        for (Point p : percentDots) {
            canvas.drawPoint(p.x, p.y, paintLine);
        }

        paintLine.setColor(Color.BLUE);
        for (Point p : debtDots) {
            canvas.drawPoint(p.x, p.y, paintLine);
        }
    }

    /**
     * Пересчет данных о платежах в точки для рисования
     */
    private void calculateDots() {
        int baseSize = Math.min(sizeX, sizeY);
        double x_step = (double)(baseSize) / MonthlyData.getTotalMonthes();

        Integer i = 0;

        for (MonthlyData dot : data) {
            double payDot_y = dot.getDoubleMonthlyPay();
            double debtDot_y = dot.getDoubleMonthlyDebt();
            double percentDot_y = dot.getDoubleMonthlyPercent();

            double maxY = (int)(1.2 * payDot_y);

            int y_pay = (int) (baseSize * payDot_y / maxY);
            int y_debt = (int) (baseSize * debtDot_y / maxY);
            int y_perc = (int) (baseSize * percentDot_y / maxY);

            percentDots.add(new Point((int)Math.round(x_step*i), baseSize - y_perc));
            debtDots.add(new Point((int)Math.round(x_step*i), baseSize - y_debt));
            payDots.add(new Point((int)Math.round(x_step*i), baseSize - y_pay));
            i += 1;
        }
    }

    private void drawLegend(Canvas canvas) {
        String s = null;
        int xOffset = 10;

        Rect tb = new Rect();
        Paint mTextPaint = new Paint();
        float pixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 14, getResources().getDisplayMetrics());
        mTextPaint.setTextSize(pixels);
        mTextPaint.setAntiAlias(true);           //сглаживание коаев
        mTextPaint.setStyle(Paint.Style.STROKE); // сплошная линия
        mTextPaint.setStrokeWidth(3);            // толщина линиии по умолчанию
        mTextPaint.setStrokeCap(Paint.Cap.ROUND); // Закругленные концы

        mTextPaint.setColor(Color.GREEN);
        s = "ВЕСЬ ПЛАТЕЖ = ";
        canvas.drawText(s, xOffset, pixels + 2, mTextPaint);
        mTextPaint.getTextBounds(s, 0, s.length(), tb);
        xOffset += tb.width();
        try {
            Point p = payDots.get(0);
            if (p != null) {
                MonthlyData d = data.get(0);
                if (d != null) {
                    canvas.drawText(d.getMonthlyPay(), 10, p.y + pixels + 2, mTextPaint);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        mTextPaint.setColor(Color.RED);
        s = " ПРОЦЕНТЫ + ";
        canvas.drawText(s, xOffset, pixels + 2, mTextPaint);
        mTextPaint.getTextBounds(s, 0, s.length(), tb);
        xOffset += tb.width();
        try {
            Point p = percentDots.get(0);
            if (p != null) {
                MonthlyData d = data.get(0);
                if (d != null) {
                    canvas.drawText(d.getMonthlyPercent(), 10, p.y + pixels + 2, mTextPaint);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        mTextPaint.setColor(Color.BLUE);
        s = " КРЕДИТ";
        canvas.drawText(s, xOffset, pixels + 2, mTextPaint);
        try {
            Point p = debtDots.get(0);
            if (p != null) {
                MonthlyData d = data.get(0);
                if (d != null) {
                    canvas.drawText(d.getMonthlyDebt(), 10, p.y + pixels + 2, mTextPaint);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
