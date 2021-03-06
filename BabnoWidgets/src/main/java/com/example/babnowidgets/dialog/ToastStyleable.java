package com.example.babnowidgets.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.FontRes;
import androidx.annotation.StyleRes;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.widget.TextViewCompat;

import com.example.babnowidgets.R;
import com.example.babnowidgets.util.ViewUtils;

import org.jetbrains.annotations.NotNull;

@SuppressLint("ViewConstructor")
public class ToastStyleable extends LinearLayout {

    private int cornerRadius;
    private int backgroundColor;
    private int strokeColor;
    private int strokeWidth;
    private int iconStart;
    private int iconEnd;
    private int textColor;
    private int font;
    private int length;
    private int style;
    private float textSize;
    private boolean isTextSizeFromStyleXml = false;
    private boolean solidBackground;
    private boolean textBold;
    private String text;
    private TypedArray typedArray;
    private TextView textView;
    private int gravity;
    private Toast toast;
    private LinearLayout rootLayout;

    public static ToastStyleable makeText(Context context, String text, int lenght, @StyleRes int style) {
        return new ToastStyleable(context, text, lenght, style);
    }

    public static ToastStyleable makeText(Context context, String text, @StyleRes int style) {
        return new ToastStyleable(context, text, Toast.LENGTH_SHORT, style);
    }


    private ToastStyleable(Context context, String text, int lenght, @StyleRes int style) {
        super(context);
        this.text = text;
        this.length = lenght;
        this.style = style;
    }

    private ToastStyleable(Builder builder) {
        super(builder.context);
        this.backgroundColor = builder.backgroundColor;
        this.cornerRadius = builder.cornerRadius;
        this.iconEnd = builder.iconEnd;
        this.iconStart = builder.iconStart;
        this.strokeColor = builder.strokeColor;
        this.strokeWidth = builder.strokeWidth;
        this.solidBackground = builder.solidBackground;
        this.textColor = builder.textColor;
        this.textSize = builder.textSize;
        this.textBold = builder.textBold;
        this.font = builder.font;
        this.text = builder.text;
        this.gravity = builder.gravity;
        this.length = builder.length;
    }

    private void inflateToastLayout() {
        View view = inflate(getContext(), R.layout.styleable_layout, null);
        rootLayout = (LinearLayout) view.getRootView();
        textView = rootLayout.findViewById(R.id.textview);
        if (style > 0)
            typedArray = getContext().obtainStyledAttributes(style, R.styleable.ToastStyleable);

        makeShape();
        makeTextView();
        makeIcon();

        if (typedArray != null)
            typedArray.recycle();
    }

    private void makeShape() {
        loadShapeAttributes();
        GradientDrawable gradientDrawable = (GradientDrawable) rootLayout.getBackground().mutate();
        gradientDrawable.setAlpha(getResources().getInteger(R.integer.defaultBackgroundAlpha));

        if (strokeWidth > 0) gradientDrawable.setStroke(strokeWidth, strokeColor);
        if (cornerRadius > -1) gradientDrawable.setCornerRadius(cornerRadius);
        if (backgroundColor != 0) gradientDrawable.setColor(backgroundColor);
        if (solidBackground)
            gradientDrawable.setAlpha(getResources().getInteger(R.integer.fullBackgroundAlpha));

        rootLayout.setBackground(gradientDrawable);

    }

    private void makeTextView() {
        loadTextViewAttributes();
        textView.setText(text);
        if (textColor != 0) textView.setTextColor(textColor);
        if (textSize > 0) textView.setTextSize(textSize);
        if (font > 0)
            textView.setTypeface(ResourcesCompat.getFont(getContext(), font), textBold ? Typeface.BOLD : Typeface.NORMAL);
        if (textBold && font == 0) textView.setTypeface(textView.getTypeface(), Typeface.BOLD);

    }

    private void makeIcon() {
        loadIconAttributes();
        int paddingVertical = (int) getResources().getDimension(R.dimen.toast_vertical_padding);
        int paddingHorizontal1 = (int) getResources().getDimension(R.dimen.toast_horizontal_padding_icon_side);
        int paddingNoIcon = (int) getResources().getDimension(R.dimen.toast_horizontal_padding_empty_side);
        int iconSize = (int) getResources().getDimension(R.dimen.icon_size);

        if (iconStart != 0) {
            Drawable drawable = ContextCompat.getDrawable(getContext(), iconStart);
            if (drawable != null) {
                drawable.setBounds(0, 0, iconSize, iconSize);
                TextViewCompat.setCompoundDrawablesRelative(textView, drawable, null, null, null);
                if (ViewUtils.isRTL()) {
                    rootLayout.setPadding(paddingNoIcon, paddingVertical, paddingHorizontal1, paddingVertical);
                } else {
                    rootLayout.setPadding(paddingHorizontal1, paddingVertical, paddingNoIcon, paddingVertical);
                }
            }
        }
        if (iconEnd != 0) {
            Drawable drawable = ContextCompat.getDrawable(getContext(), iconEnd);
            if (drawable != null) {
                drawable.setBounds(0, 0, iconSize, iconSize);
                TextViewCompat.setCompoundDrawablesRelative(textView, null, null, drawable, null);
                if (ViewUtils.isRTL()) {
                    rootLayout.setPadding(paddingHorizontal1, paddingVertical, paddingNoIcon, paddingVertical);
                } else {
                    rootLayout.setPadding(paddingNoIcon, paddingVertical, paddingHorizontal1, paddingVertical);
                }
            }
        }

        if (iconStart != 0 && iconEnd != 0) {
            Drawable drawableLeft = ContextCompat.getDrawable(getContext(), iconStart);
            Drawable drawableRight = ContextCompat.getDrawable(getContext(), iconEnd);
            if (drawableLeft != null && drawableRight != null) {
                drawableLeft.setBounds(0, 0, iconSize, iconSize);
                drawableRight.setBounds(0, 0, iconSize, iconSize);
                textView.setCompoundDrawables(drawableLeft, null, drawableRight, null);
                rootLayout.setPadding(paddingHorizontal1, paddingVertical, paddingHorizontal1, paddingVertical);
            }
        }

    }


    private void loadShapeAttributes() {
        if (style == 0) {
            return;
        }

        int defaultBackgroundColor = ContextCompat.getColor(getContext(), R.color.default_background_color);
        int defaultCornerRadius = (int) getResources().getDimension(R.dimen.default_corner_radius);

        solidBackground = typedArray.getBoolean(R.styleable.ToastStyleable_cv_st_SolidBackground, false);
        backgroundColor = typedArray.getColor(R.styleable.ToastStyleable_cv_st_ColorBackground, defaultBackgroundColor);
        cornerRadius = (int) typedArray.getDimension(R.styleable.ToastStyleable_cv_st_Radius, defaultCornerRadius);
        length = typedArray.getInt(R.styleable.ToastStyleable_cv_st_Length, 0);
        gravity = typedArray.getInt(R.styleable.ToastStyleable_cv_st_Gravity, Gravity.BOTTOM);

        if (gravity == 1) {
            gravity = Gravity.CENTER;
        } else if (gravity == 2) {
            gravity = Gravity.TOP;
        }

        if (typedArray.hasValue(R.styleable.ToastStyleable_cv_st_StrokeColor) && typedArray.hasValue(R.styleable.ToastStyleable_cv_st_StrokeWidth)) {
            strokeWidth = (int) typedArray.getDimension(R.styleable.ToastStyleable_cv_st_StrokeWidth, 0);
            strokeColor = typedArray.getColor(R.styleable.ToastStyleable_cv_st_StrokeColor, Color.TRANSPARENT);
        }
    }

    private void loadTextViewAttributes() {
        if (style == 0) {
            return;
        }

        textColor = typedArray.getColor(R.styleable.ToastStyleable_cv_st_TextColor, textView.getCurrentTextColor());
        textBold = typedArray.getBoolean(R.styleable.ToastStyleable_cv_st_TextBold, false);
        textSize = (int) typedArray.getDimension(R.styleable.ToastStyleable_cv_st_TextSize, 0);
        font = typedArray.getResourceId(R.styleable.ToastStyleable_cv_st_Font, 0);
        isTextSizeFromStyleXml = textSize > 0;
    }

    private void loadIconAttributes() {
        if (style == 0) {
            return;
        }
        iconStart = typedArray.getResourceId(R.styleable.ToastStyleable_cv_st_IconStart, 0);
        iconEnd = typedArray.getResourceId(R.styleable.ToastStyleable_cv_st_IconEnd, 0);
    }

    private void createAndShowToast() {
        inflateToastLayout();
        toast = new Toast(getContext());
        toast.setGravity(gravity, 0, gravity == Gravity.CENTER ? 0 : toast.getYOffset());
        toast.setDuration(length == Toast.LENGTH_LONG ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT);
        toast.setView(rootLayout);
        toast.show();
    }

    public void show() {
        createAndShowToast();
    }

    public void cancel() {
        if (toast != null) {
            toast.cancel();
        }
    }

    public static class Builder {
        private int cornerRadius = -1;
        private int backgroundColor;
        private int strokeColor;
        private int strokeWidth;
        private int iconStart;
        private int iconEnd;
        private int textColor;
        private int font;
        private int length;
        private float textSize;
        private boolean solidBackground;
        private boolean textBold;
        private String text;
        private int gravity = Gravity.BOTTOM;
        private final Context context;

        public Builder(@NotNull Context context) {
            this.context = context;
        }

        public Builder text(String text) {
            this.text = text;
            return this;
        }

        public Builder textColor(@ColorInt int textColor) {
            this.textColor = textColor;
            return this;
        }

        public Builder textBold() {
            this.textBold = true;
            return this;
        }

        public Builder textSize(float textSize) {
            this.textSize = textSize;
            return this;
        }


        /*font A font resource id like R.font.somefont as introduced with the new font api in Android 8*/

        public Builder font(@FontRes int font) {
            this.font = font;
            return this;
        }

        public Builder backgroundColor(@ColorInt int backgroundColor) {
            this.backgroundColor = backgroundColor;
            return this;
        }

        public Builder solidBackground() {
            this.solidBackground = true;
            return this;
        }

        public Builder stroke(int strokeWidth, @ColorInt int strokeColor) {
            this.strokeWidth = ViewUtils.toDp(context, strokeWidth);
            this.strokeColor = strokeColor;
            return this;
        }

        public Builder cornerRadius(int cornerRadius) {
            this.cornerRadius = ViewUtils.toDp(context, cornerRadius);
            return this;
        }

        public Builder iconStart(@DrawableRes int iconStart) {
            this.iconStart = iconStart;
            return this;
        }

        public Builder iconEnd(@DrawableRes int iconEnd) {
            this.iconEnd = iconEnd;
            return this;
        }

        public Builder gravity(int gravity) {
            this.gravity = gravity;
            return this;
        }

        public Builder length(int length) {
            this.length = length;
            return this;
        }

        public void show() {
            if (context == null) {
                errorMessage();
            }
            ToastStyleable toast = new ToastStyleable(this);
            toast.show();
        }

        private void errorMessage() {
            throw new RuntimeException("This Toast was not created with Toast.makeText()");
        }

    }

}
