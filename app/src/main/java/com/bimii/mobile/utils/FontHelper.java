package com.bimii.mobile.utils;

import android.content.Context;
import android.graphics.Typeface;

import java.lang.reflect.Field;

public final class FontHelper {

	public static Typeface kidsBookFont;

	public static void init(final Context _context) {
		kidsBookFont = Typeface.createFromAsset(_context.getAssets(), "kids_book.ttf");
		setDefaultFont(_context);
	}

	protected static void setDefaultFont(Context context) {
		final Typeface regular = Typeface.createFromAsset(context.getAssets(),
				"kids_book.ttf");
		replaceFont("SERIF", regular);
	}

	protected static void replaceFont(String staticTypefaceFieldName,
									  final Typeface newTypeface) {
		try {
			final Field staticField = Typeface.class
					.getDeclaredField(staticTypefaceFieldName);
			staticField.setAccessible(true);
			staticField.set(null, newTypeface);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

}
