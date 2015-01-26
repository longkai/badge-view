package org.heisenberglab.badge;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

/**
 * A badge view which shows text or empty badge on four edge corner.
 * Note: only support one children!
 * <p/>
 * Created by longkai on 10/1/15.
 */
public class BadgeView extends ViewGroup {
  private Paint paint;

  private int badgeColor;
  private int badgeTextColor;
  private int badgeTextSize;
  private int normalBadgeRadius;
  private int smallBadgeRadius;
  private int badgeMargin;

  private String badgeText;

  private BadgeLocation badgeLocation;

  private boolean showBadge;
  private boolean showTextBadge;

  private Rect textRect = new Rect();

  public BadgeView(Context context) {
    this(context, null, 0);
  }

  public BadgeView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public BadgeView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    bootstrap(attrs, defStyleAttr, 0);
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public BadgeView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
    bootstrap(attrs, defStyleAttr, defStyleRes);
  }

  private void bootstrap(AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    final Resources res = getResources();
    badgeColor = Color.parseColor("#f44336"); // red-500
    badgeTextColor = res.getColor(android.R.color.white);
    badgeTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, res.getDisplayMetrics());
    normalBadgeRadius = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, res.getDisplayMetrics());
    smallBadgeRadius = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, res.getDisplayMetrics());
    badgeLocation = BadgeLocation.TOP_RIGHT;
    // default no badge!
    showBadge = false;
    showTextBadge = false;

    if (attrs != null) {
      TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.BadgeView, defStyleAttr,
          defStyleRes);

      badgeColor = array.getColor(R.styleable.BadgeView_badge_color, badgeColor);
      badgeText = array.getString(R.styleable.BadgeView_badge_text);
      badgeTextColor = array.getColor(R.styleable.BadgeView_badge_text_color, badgeTextColor);
      badgeTextSize = array.getDimensionPixelSize(R.styleable.BadgeView_badge_text_size, badgeTextSize);
      normalBadgeRadius = array.getDimensionPixelSize(R.styleable.BadgeView_normal_badge_radius, normalBadgeRadius);
      smallBadgeRadius = array.getDimensionPixelSize(R.styleable.BadgeView_small_badge_radius, smallBadgeRadius);
      badgeMargin = array.getDimensionPixelSize(R.styleable.BadgeView_badge_margin, badgeMargin);
      badgeLocation = BadgeLocation.values()[array.getInt(R.styleable.BadgeView_badge_location,
          BadgeLocation.TOP_RIGHT.ordinal())];

      showBadge = array.getBoolean(R.styleable.BadgeView_show_badge, showBadge);
      showTextBadge = array.getBoolean(R.styleable.BadgeView_show_text_badge, showTextBadge);

      array.recycle();
    }

    paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    paint.setTextSize(badgeTextSize);
  }

  @Override protected void dispatchDraw(Canvas canvas) {
    super.dispatchDraw(canvas);
    // draw over children!
    drawBadge(canvas);
  }

  private void drawBadge(Canvas canvas) {
    int radius = showBadge ? smallBadgeRadius : showTextBadge ? normalBadgeRadius : 0;
    if (radius == 0) // no badge to draw!
      return;

    int x;
    int y;
    switch (badgeLocation) {
      case TOP_LEFT:
        x = badgeMargin + radius;
        y = badgeMargin + radius;
        break;
      default:
      case TOP_RIGHT:
        x = getWidth() - badgeMargin - radius;
        y = badgeMargin + radius;
        break;
      case BOTTOM_LEFT:
        x = badgeMargin + radius;
        y = getHeight() - badgeMargin - radius;
        break;
      case BOTTOM_RIGHT:
        x = getWidth() - badgeMargin - radius;
        y = getHeight() - badgeMargin - radius;
        break;
    }
    paint.setColor(badgeColor);
    canvas.drawCircle(x, y, radius, paint);
    if (badgeText != null && showTextBadge) {
      paint.setColor(badgeTextColor);
      paint.setTextSize(badgeTextSize);
      paint.getTextBounds(badgeText, 0, badgeText.length(), textRect);
      x -= textRect.width() / 2;
      y += textRect.height() / 2;
      canvas.drawText(badgeText, 0, badgeText.length(), x, y, paint);
    }
  }

  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    int width = MeasureSpec.getSize(widthMeasureSpec);
    int height = MeasureSpec.getSize(heightMeasureSpec);

    View child = getChildAt(0);
    if (child != null && child.getVisibility() != GONE) {
      child.measure(widthMeasureSpec, heightMeasureSpec);

      if (MeasureSpec.getMode(widthMeasureSpec) != MeasureSpec.EXACTLY) {
        width = child.getMeasuredWidth();
      }
      if (MeasureSpec.getMode(heightMeasureSpec) != MeasureSpec.EXACTLY) {
        height = child.getMeasuredHeight();
      }
    }

    setMeasuredDimension(width, height);
  }

  @Override protected void onLayout(boolean changed, int l, int t, int r, int b) {
    if (changed) {
      View child = getChildAt(0);
      if (child != null && child.getVisibility() != View.GONE) {
        child.layout(0, 0, child.getMeasuredWidth(), child.getMeasuredHeight());
      }
    }
  }

  public int getBadgeColor() {
    return badgeColor;
  }

  public void setBadgeColor(int badgeColor) {
    this.badgeColor = badgeColor;
  }

  public int getBadgeTextColor() {
    return badgeTextColor;
  }

  public void setBadgeTextColor(int badgeTextColor) {
    this.badgeTextColor = badgeTextColor;
  }

  public int getBadgeTextSize() {
    return badgeTextSize;
  }

  public void setBadgeTextSize(int badgeTextSize) {
    this.badgeTextSize = badgeTextSize;
  }

  public int getNormalBadgeRadius() {
    return normalBadgeRadius;
  }

  public void setNormalBadgeRadius(int normalBadgeRadius) {
    this.normalBadgeRadius = normalBadgeRadius;
  }

  public int getSmallBadgeRadius() {
    return smallBadgeRadius;
  }

  public void setSmallBadgeRadius(int smallBadgeRadius) {
    this.smallBadgeRadius = smallBadgeRadius;
  }

  public int getBadgeMargin() {
    return badgeMargin;
  }

  public void setBadgeMargin(int badgeMargin) {
    this.badgeMargin = badgeMargin;
  }

  public String getBadgeText() {
    return badgeText;
  }

  public void setBadgeText(String badgeText) {
    this.badgeText = badgeText;
  }

  public BadgeLocation getBadgeLocation() {
    return badgeLocation;
  }

  public void setBadgeLocation(BadgeLocation badgeLocation) {
    this.badgeLocation = badgeLocation;
  }

  public void hideBadge() {
    showBadge = false;
    showTextBadge = false;
    badgeText = null;
    invalidate();
  }

  public void showBadge() {
    showTextBadge = false;
    badgeText = null;
    if (!showBadge) {
      showBadge = true;
      invalidate();
    }
  }

  public void showBadge(String text) {
    showBadge = false;
    if (!showTextBadge || !TextUtils.equals(badgeText, text)) {
      showTextBadge = true;
      badgeText = text;
      invalidate();
    }
  }

  public void addBadgeView(View badge) {
    removeAllViews();
    addView(badge);
  }

  public enum BadgeLocation {
    TOP_LEFT,
    TOP_RIGHT,
    BOTTOM_LEFT,
    BOTTOM_RIGHT,
  }
}
